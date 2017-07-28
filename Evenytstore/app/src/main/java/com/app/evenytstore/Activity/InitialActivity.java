package com.app.evenytstore.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.amazonaws.mobileconnectors.apigateway.ApiClientException;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
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

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.util.List;

import EvenytServer.model.Customer;


public class InitialActivity extends AppCompatActivity implements LoginInterface {

    CognitoCachingCredentialsProvider credentialsProvider;
    GoogleApiClient mGoogleApiClient;
    private boolean signed_facebook = false;
    private boolean signed_google = false;
    private ProgressDialog progressDialog;
    private AccessToken token;
    private GoogleSignInAccount account;

    @Override
    public void onSuccessFacebook(AccessToken token) {
        signed_facebook = true;
        this.token = token;
        AmazonLoginTask task = new AmazonLoginTask();
        task.execute();
    }

    @Override
    public void onSuccessGoogle(GoogleSignInAccount loginResult) {
        signed_google = true;
        this.account = loginResult;
        AmazonLoginTask task = new AmazonLoginTask();
        task.execute();
    }


    public class AmazonLoginTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            String id = credentialsProvider.getIdentityId();
            if(Shelf.getHashCustomers().containsKey(id)){
                AppSettings.CURRENT_CUSTOMER = Shelf.getHashCustomers().get(id);
                Intent intent = new Intent(InitialActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return null;
            }else{
                try {
                    Customer customer = ServerAccess.getClient().customersIdCustomerGet(id);
                    DatabaseAccess.getInstance(getApplicationContext()).insertCustomer(customer);
                    AppSettings.CURRENT_CUSTOMER = customer;
                    Intent intent = new Intent(InitialActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }catch(ApiClientException e){
                    if(!e.getErrorMessage().equals("Not found."))
                        throw e;
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

            return null;
        }
    }


    private void openMainView(){
        Intent intent = new Intent(this, InputAddressActivity.class);
        startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cuenta");
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

        String identityPool = getString(R.string.identity_pool_id);
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(), // Context
                identityPool, // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        LoginFragment login = new LoginFragment();
        login.setmGoogleApiClient(mGoogleApiClient);
        login.setCredentialsProvider(credentialsProvider);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.add(login, "login");
        transaction.add(R.id.fragment_container, login, "login");
        transaction.commit();

        if(isLoggedInFacebook()){
            onSuccessFacebook(AccessToken.getCurrentAccessToken());
        }else if(isLoggedInGoogle()){
            //Handled inside isLoggedInGoogle() method
        }
    }


    private void syncCognito(){
        CognitoSyncManager syncClient = new CognitoSyncManager(
                getApplicationContext(),
                Regions.US_EAST_1, // Region
                credentialsProvider);

// Create a record in a dataset and synchronize with the server
        Dataset dataset = syncClient.openOrCreateDataset("Evenyt Store");
        dataset.put("myKey", "myValue");
        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
                //Your handler code here
            }
        });
    }


    private boolean isLoggedInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }


    private boolean isLoggedInGoogle() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            if(result.isSuccess()){
                onSuccessGoogle(result.getSignInAccount());
                return true;
            }
            return false;
        }else{
            progressDialog = ProgressDialog.show(this, "", "");
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult result) {
                    progressDialog.dismiss();
                    if(result.isSuccess() && !(signed_google || signed_facebook)){
                        onSuccessGoogle(result.getSignInAccount());
                    }
                }
            });
            return false;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("login");
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
