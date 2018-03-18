package com.app.evenytstore.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

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
                e.printStackTrace();
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }

    CallbackManager callbackManager;
    CognitoCachingCredentialsProvider credentialsProvider;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private LoginInterface callback;


    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient){
        this.mGoogleApiClient = mGoogleApiClient;
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
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode())
            callbackManager.onActivityResult(requestCode, resultCode, data);
        else{
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            GoogleTokenTask task = new GoogleTokenTask();
            task.execute(result.getSignInAccount());

            if(result.isSuccess()){

            }
        }

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

        }else{

        }

    }
}
