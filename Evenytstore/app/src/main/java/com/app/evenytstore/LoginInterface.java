package com.app.evenytstore;

import com.facebook.AccessToken;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by Enrique on 06/07/2017.
 */

public interface LoginInterface {
    public void onSuccessFacebook(AccessToken token);
    public void onSuccessGoogle(GoogleSignInAccount loginResult);

}
