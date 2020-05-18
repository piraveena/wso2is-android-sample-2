package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import org.oidc.agent.sso.LoginService;
import org.oidc.agent.sso.OAuth2TokenResponse;
import org.oidc.agent.sso.TokenRequest;
import org.oidc.agent.sso.UserInfoRequest;
import org.oidc.agent.sso.UserInfoResponse;

public class UserInfoActivity extends AppCompatActivity {

    private LoginService mLoginService;
    private static final String LOG_TAG = "UserInfoActivity";
    private String mSubject;
    private String mEmail;
    private String mAccessToken;
    private String mIdToken;
    private OAuth2TokenResponse mOAuth2TokenResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
    }

    @Override
    protected void onStart() {

        super.onStart();
        mLoginService = LoginService.getInstance(this);
        handleAuthorizationResponse(getIntent());
    }

    private void handleAuthorizationResponse(Intent intent) {

        mLoginService.handleAuthorization(intent, new TokenRequest.TokenRespCallback() {
            @Override
            public void onTokenRequestCompleted(OAuth2TokenResponse oAuth2TokenResponse) {
                mOAuth2TokenResponse = oAuth2TokenResponse;
                getUserInfo();
            }
        });
    }

    private void getUserInfo(){
        mLoginService.getUserInfo(new UserInfoRequest.UserInfoResponseCallback() {
            @Override
            public void onUserInfoRequestCompleted(UserInfoResponse userInfoResponse) {
                mSubject = userInfoResponse.getSubject();
                mEmail = userInfoResponse.getUserInfoProperty("email");
                Log.i(LOG_TAG, mSubject);
                mIdToken = mOAuth2TokenResponse.idToken;
                mAccessToken = mOAuth2TokenResponse.accessToken;
                JSONObject userinfo = userInfoResponse.getUserInfoProperties();
                Log.i(LOG_TAG, userinfo.toString());
                Log.d(LOG_TAG, String.format("Token Response [ Access Token: %s, ID Token: %s ]",
                        mOAuth2TokenResponse.accessToken, mOAuth2TokenResponse.idToken));
                getUIContent();
            }
        });
    }

    private void getUIContent() {

        try {
            //JSONParser parser = new JSONParser();
            //String[] split = mIdToken.split("\\.");
            //String decodeIDToken = new String(Base64.decode(split[1], Base64.URL_SAFE), "UTF-8");
           // JSONObject json = (JSONObject) parser.parse(decodeIDToken);
            addUiElements();
            findViewById(R.id.logout).setOnClickListener(v ->
                    singleLogout(this)
            );
        } catch (Exception e) {
            //handle the exception.
        }
    }


    private void singleLogout(Context context) {

        mLoginService.logout(context);
        finish();
    }

    private void addUiElements(){

        TextView username = findViewById(R.id.username);
        TextView emailId = findViewById(R.id.emailid);
        TextView accessTokenView = findViewById(R.id.accesstoken);
        TextView idtokenView = findViewById(R.id.idtoken);

        idtokenView.setText(mIdToken);
        username.setText(mSubject);
        emailId.setText(mEmail);
        accessTokenView.setText(mAccessToken);
    }
}

