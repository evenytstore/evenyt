package com.app.evenytstore.Fragment;

import com.facebook.AccessToken;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by Enrique on 06/07/2017.
 */

public interface LoginInterface {
    void onSuccessFacebook(AccessToken token);
    void onSuccessGoogle(GoogleSignInAccount loginResult);

}
