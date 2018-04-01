package com.app.evenytstore.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.apigateway.ApiClientException;
import com.app.evenytstore.Fragment.LoginFragment;
import com.app.evenytstore.Fragment.LoginInterface;
import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.Model.DatabaseAccess;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.app.evenytstore.Server.ServerAccess;
import com.app.evenytstore.Utility.DateHandler;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import EvenytServer.model.Customer;


public class InitialActivity extends AppCompatActivity implements LoginInterface {

    GoogleSignInClient mGoogleSignInClient;
    private boolean signed_facebook = false;
    private boolean signed_google = false;
    private int REGISTER = 1;
    private AccessToken token;
    private FirebaseAuth mAuth;
    private GoogleSignInAccount account;
    private FirebaseUser user;

    @Override
    public void onSuccessFacebook(AccessToken token) {
        signed_facebook = true;
        this.token = token;
        handleFacebookAccessToken(token);
    }

    @Override
    public void onSuccessGoogle(GoogleSignInAccount loginResult) {
        signed_google = true;
        this.account = loginResult;
        firebaseAuthWithGoogle(loginResult);
    }


    public class AmazonLoginTask extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            String id = user.getUid();
            if(Shelf.getHashCustomers().containsKey(id)){
                AppSettings.CURRENT_CUSTOMER = Shelf.getHashCustomers().get(id);
                Intent intent = new Intent(InitialActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            }else{
                try {
                    id = id.replace(':','_');
                    Customer customer = ServerAccess.getClient().customersIdCustomerGet(id);
                    customer.setBirthday(DateHandler.toString(DateHandler.toDateServer(customer.getBirthday().substring(0,10))));
                    DatabaseAccess instance = DatabaseAccess.getInstance(getApplicationContext());
                    instance.open();
                    instance.insertCustomer(customer);
                    instance.close();
                    AppSettings.CURRENT_CUSTOMER = customer;
                    Intent intent = new Intent(InitialActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return true;
                }catch(ApiClientException e){
                    id = id.replace('_',':');
                    if(!e.getErrorMessage().contains("not found.")){
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            AppSettings.CURRENT_CUSTOMER = new Customer();
            AppSettings.CURRENT_CUSTOMER.setIdCustomer(id);

            if(signed_facebook){
                //Get profile info
                GraphRequest request = GraphRequest.newMeRequest(
                        token,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d("LoginActivity", response.toString());

                                // Application code
                                try {
                                    if(object.has("email"))
                                        AppSettings.CURRENT_CUSTOMER.setEmail(object.getString("email"));
                                    if(object.has("birthday")){
                                        AppSettings.CURRENT_CUSTOMER.setBirthday(DateHandler.toString(DateHandler.toDateUSA(object.getString("birthday"))));
                                    }
                                    if(object.has("first_name"))
                                        AppSettings.CURRENT_CUSTOMER.setName(object.getString("first_name"));
                                    if(object.has("last_name"))
                                        AppSettings.CURRENT_CUSTOMER.setLastName(object.getString("last_name"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                openMainView();
                            }
                        });
                //Save facebook profile info in case we need it later
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name,last_name,email,birthday");

                request.setParameters(parameters);
                request.executeAsync();
            }else if(signed_google){
                AppSettings.CURRENT_CUSTOMER.setEmail(account.getEmail());
                AppSettings.CURRENT_CUSTOMER.setName(account.getGivenName());
                AppSettings.CURRENT_CUSTOMER.setLastName(account.getFamilyName());

                openMainView();
            }

            return true;
        }


        protected void onPostExecute(Boolean result){
            if(!result){
                Dialog dialog = new AlertDialog.Builder(InitialActivity.this)
                        .setTitle("Error")
                        .setMessage("No se pudo establecer conexión al servidor.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                finish();
            }
        }
    }


    private void openMainView(){
        Intent intent = new Intent(this, InputAddressActivity.class);
        startActivityForResult(intent, REGISTER);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        Intent i = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ImageView icon = (ImageView)findViewById(R.id.icon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Obtain key for Facebook
        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.app.evenytstore",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String kuk = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        LoginFragment login = new LoginFragment();
        login.setmGoogleApiClient(mGoogleSignInClient);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.add(login, "login");
        transaction.add(R.id.fragment_container, login, "login");
        transaction.commit();

        if(i.hasExtra("logout")){
            logout();
        }else{
            if(isLoggedInFacebook()){
                onSuccessFacebook(AccessToken.getCurrentAccessToken());
            }else if(isLoggedInGoogle()){
                //Handled inside isLoggedInGoogle() method
            }
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Task<AuthResult> task = mAuth.signInWithCredential(credential);
        task.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e("Success", "signInWithCredential:success");
                    if(user != null)
                        return;
                    user = mAuth.getCurrentUser();
                    AmazonLoginTask t = new AmazonLoginTask();
                    t.execute();

                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("Error", "signInWithCredential:failure", task.getException());
                    Toast.makeText(InitialActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }

                // ...
            }
        });
        if(task.isComplete()){
            if(task.isSuccessful()){
                // Sign in success, update UI with the signed-in user's information
                Log.e("Success", "signInWithCredential:success");
                if(user != null)
                    return;
                user = mAuth.getCurrentUser();
                AmazonLoginTask t = new AmazonLoginTask();
                t.execute();
            }else {
                // If sign in fails, display a message to the user.
                Log.e("Error", "signInWithCredential:failure", task.getException());
                Toast.makeText(InitialActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
            return;
        }

    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Task<AuthResult> task = mAuth.signInWithCredential(credential);

        task.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e("Success", "signInWithCredential:success");
                    if(user!=null)
                        return;
                    user = mAuth.getCurrentUser();
                    AmazonLoginTask t = new AmazonLoginTask();
                    t.execute();

                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("Error", "signInWithCredential:failure", task.getException());
                    Toast.makeText(InitialActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }

                // ...
            }
        });
        if(task.isComplete()){
            if(task.isSuccessful()){
                // Sign in success, update UI with the signed-in user's information
                Log.e("Success", "signInWithCredential:success");
                if(user!=null)
                    return;
                user = mAuth.getCurrentUser();
                AmazonLoginTask t = new AmazonLoginTask();
                t.execute();
            }else {
                // If sign in fails, display a message to the user.
                Log.e("Error", "signInWithCredential:failure", task.getException());
                Toast.makeText(InitialActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
            return;
        }

    }



    public void logout(){
        user = null;
        account = null;
        token = null;
        mAuth.signOut();
        disconnectFromFacebook();
        disconnectFromGoogle();
    }


    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }


    private void disconnectFromGoogle() {
        mGoogleSignInClient.revokeAccess();
    }


    private boolean isLoggedInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }


    private boolean isLoggedInGoogle() {
        Task<GoogleSignInAccount> test = mGoogleSignInClient.silentSignIn();
        test.addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
            @Override
            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                try{
                    if(account != null)
                        return;
                    account = task.getResult(ApiException.class);
                    onSuccessGoogle(account);
                }catch(ApiException e){
                    Log.e("Api Exception", e.getMessage());
                }

            }
        });
        if(test.isComplete()){
            if(account != null)
                return false;
            account = test.getResult();

            onSuccessGoogle(account);
            return true;
        }

        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REGISTER){
            logout();
        }else{
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("login");
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
