package com.app.evenytstore.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.evenytstore.Activity.TermsConditionsActivity;
import com.app.evenytstore.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;

import java.util.Arrays;


public class LoginFragment extends Fragment {

    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSigninClient;
    private static final int RC_SIGN_IN = 9001;
    private LoginInterface callback;


    public void setmGoogleApiClient(GoogleSignInClient mGoogleApiClient){
        this.mGoogleSigninClient = mGoogleApiClient;
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
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();
                    callback.onSuccessGoogle(account);
                }
            }
        }

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);


    }
}
