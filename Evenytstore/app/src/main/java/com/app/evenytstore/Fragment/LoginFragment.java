package com.app.evenytstore.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.evenytstore.Activity.MainActivity;
import com.app.evenytstore.Activity.TermsConditionsActivity;
import com.app.evenytstore.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class LoginFragment extends Fragment {

    public class GoogleTokenTask extends AsyncTask<GoogleSignInAccount, Void, Void> {
        @Override
        protected Void doInBackground(GoogleSignInAccount... params) {
            GoogleSignInAccount account = params[0];

            try {
                String token = GoogleAuthUtil.getToken(getActivity().getApplicationContext(), account.getAccount(),
                        "audience:server:client_id:"+getString(R.string.server_client_id));
                Map<String, String> logins = new HashMap<String, String>();
                logins.put("accounts.google.com", token);
                credentialsProvider.setLogins(logins);
                callback.onSuccessGoogle(account);
            } catch (IOException e) {
                Dialog dialog = new android.app.AlertDialog.Builder(getActivity())
                        .setTitle("IO Error")
                        .setMessage(e.getMessage())
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_info).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                e.printStackTrace();
            } catch (GoogleAuthException e) {
                Dialog dialog = new android.app.AlertDialog.Builder(getActivity())
                        .setTitle("Google Auth Exception")
                        .setMessage(e.getMessage())
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_info).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                e.printStackTrace();
            }catch (Exception e){
                Dialog dialog = new android.app.AlertDialog.Builder(getActivity())
                        .setTitle("Other Exception")
                        .setMessage(e.getMessage())
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_info).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                e.printStackTrace();
            }

            return null;
        }
    }

    CallbackManager callbackManager;
    CognitoCachingCredentialsProvider credentialsProvider;
    GoogleSignInClient mGoogleSigninClient;
    private static final int RC_SIGN_IN = 9001;
    private LoginInterface callback;


    public void setmGoogleApiClient(GoogleSignInClient mGoogleApiClient){
        this.mGoogleSigninClient = mGoogleApiClient;
    }


    public void setCredentialsProvider(CognitoCachingCredentialsProvider credentialsProvider){
        this.credentialsProvider = credentialsProvider;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        LoginButton login_facebook_button = (LoginButton)view.findViewById(R.id.login_facebook_button);
        login_facebook_button.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));

        SignInButton login_google_button = (SignInButton)view.findViewById(R.id.login_google_button);
        login_google_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSigninClient.getSignInIntent();
                getActivity().startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        TextView terms = (TextView)view.findViewById(R.id.txtTerms);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentActivity activity = getActivity();
                Intent i = new Intent(activity, TermsConditionsActivity.class);
                activity.startActivity(i);
            }
        });

        callback = (LoginInterface)getActivity();

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        //Amazon Cognito
                        Map<String, String> logins = new HashMap<String, String>();
                        logins.put("graph.facebook.com", AccessToken.getCurrentAccessToken().getToken());
                        credentialsProvider.setLogins(logins);

                        logins.get("graph.facebook.com");

                        callback.onSuccessFacebook(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        return view;
    }


    private void handleSignInResult(Task<GoogleSignInAccount> task){
        try {
            GoogleSignInAccount account =task.getResult(ApiException.class);
            String token = GoogleAuthUtil.getToken(getActivity().getApplicationContext(), account.getAccount(),
                    "audience:server:client_id:"+getString(R.string.server_client_id));
            Map<String, String> logins = new HashMap<String, String>();
            logins.put("accounts.google.com", token);
            credentialsProvider.setLogins(logins);
            callback.onSuccessGoogle(account);

        }catch(IOException e){
            Dialog dialog = new android.app.AlertDialog.Builder(getActivity())
                    .setTitle("IO Error")
                    .setMessage(e.getMessage())
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_info).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            e.printStackTrace();
        } catch(ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Dialog dialog = new android.app.AlertDialog.Builder(getActivity())
                    .setTitle("ApiException")
                    .setMessage(e.getMessage())
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_info).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }catch(UserRecoverableAuthException e){
            Dialog dialog = new android.app.AlertDialog.Builder(getActivity())
                    .setTitle("UserRecoverableAuthException")
                    .setMessage(e.getMessage())
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_info).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }catch(GoogleAuthException e){
            Dialog dialog = new android.app.AlertDialog.Builder(getActivity())
                    .setTitle("GoogleAuthException")
                    .setMessage(e.getMessage())
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_info).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode())
            callbackManager.onActivityResult(requestCode, resultCode, data);
        else{
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();
                    callback.onSuccessGoogle(account);
                } else {
                    Dialog dialog = new android.app.AlertDialog.Builder(getActivity())
                            .setTitle("Sign in failed")
                            .setMessage(String.valueOf(result.getStatus().getStatusCode()))
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_info).create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    // Google Sign In failed, update UI appropriately
                    // ...
                }
            }else{

            }
            /*GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            Dialog dialog = new android.app.AlertDialog.Builder(getActivity())
                    .setTitle("Fuck")
                    .setMessage(result.getStatus().getStatusMessage())
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_info).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();


            dialog = new android.app.AlertDialog.Builder(getActivity())
                    .setTitle("Status")
                    .setMessage(result.getStatus().getStatusCode())
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_info).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();



            GoogleTokenTask task = new GoogleTokenTask();
            task.execute(result.getSignInAccount());

            if(result.isSuccess()){

            }else{
                dialog = new android.app.AlertDialog.Builder(getActivity())
                        .setTitle("Success")
                        .setMessage("Failed")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_info).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }*/
        }

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);


    }
}
