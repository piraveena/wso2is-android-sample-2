package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

import org.oidc.agent.LoginService;
import org.oidc.agent.OAuth2TokenResponse;
import org.oidc.agent.TokenRequest;


public class UserInfoActivity extends AppCompatActivity {

    private LoginService mLoginService;
    private static final String LOG_TAG = "UserInfoActivity";

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
                readUserInfo(oAuth2TokenResponse);
            }
        });
    }

    private void readUserInfo(OAuth2TokenResponse response) {

        try {
            String idToken = response.idToken;
            String accessToken = response.accessToken;
            Log.i(LOG_TAG, String.format("Token Response [ Access Token: %s, ID Token: %s ]", response.accessToken, response.idToken));
            JSONParser parser = new JSONParser();
            String[] split = idToken.split("\\.");
            String decodeIDToken = new String(Base64.decode(split[1], Base64.URL_SAFE), "UTF-8");
            JSONObject json = (JSONObject) parser.parse(decodeIDToken);
            String userName = (String) json.get("sub");
            String email = (String) json.get("email");
            addUiElements(decodeIDToken, userName, email, accessToken);
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

    private void addUiElements(String decodedIDToken, String userName, String email, String accessToken) throws Exception {

        TextView username = findViewById(R.id.username);
        TextView emailId = findViewById(R.id.emailid);
        TextView accessTokenView = findViewById(R.id.accesstoken);
        TextView idtokenView = findViewById(R.id.idtoken);

        idtokenView.setText(decodedIDToken);
        username.setText(userName);
        emailId.setText(email);
        accessTokenView.setText(accessToken);
    }
}

