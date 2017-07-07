package com.app.evenytstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class InitialActivity extends FragmentActivity implements  LoginInterface{

    public static User CURRENT_USER;
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
            CURRENT_USER = new User(id);

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
                                        CURRENT_USER.setName(object.getString("email"));
                                    if(object.has("birthday")){
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                        Calendar cal = Calendar.getInstance();
                                        try {
                                            cal.setTime(sdf.parse(object.getString("birthday")));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        CURRENT_USER.setBirthday(cal);
                                    }
                                    if(object.has("name"))
                                        CURRENT_USER.setEmail(object.getString("name"));
                                    if(object.has("gender"))
                                        CURRENT_USER.setGender(object.getString("gender"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                openMainView();
                            }
                        });
                //Save facebook profile info in case we need it later
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,email,gender,birthday");

                request.setParameters(parameters);
                request.executeAsync();
            }else if(signed_google){
                CURRENT_USER.setEmail(account.getEmail());
                CURRENT_USER.setName(account.getDisplayName());
                openMainView();
            }

            return null;
        }
    }


    private void openMainView(){
        Intent intent = new Intent(this, FinishLoginActivity.class);
        startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

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

        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(), // Context
                "us-east-1:3ae04243-9a73-46a3-a44f-6ecb055e05a6", // Identity Pool ID
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

        Login login = new Login();
        login.setmGoogleApiClient(mGoogleApiClient);
        login.setCredentialsProvider(credentialsProvider);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.add(login, "login");
        transaction.add(R.id.fragment_container, login, "login");
        transaction.commit();

        if(isLoggedInFacebook()){
            onSuccessFacebook(AccessToken.getCurrentAccessToken());
        }else if(isLoggedInGoogle()){

        }
    }


    private void syncCognito(){
        CognitoSyncManager syncClient = new CognitoSyncManager(
                getApplicationContext(),
                Regions.US_EAST_1, // Region
                credentialsProvider);

// Create a record in a dataset and synchronize with the server
        Dataset dataset = syncClient.openOrCreateDataset("myDataset");
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
                    if(result.isSuccess() && !(signed_google || signed_facebook))
                        onSuccessGoogle(result.getSignInAccount());
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
