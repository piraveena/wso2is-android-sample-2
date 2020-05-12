package com.example.myapplication;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import net.openid.appauth.AuthorizationException;
//import net.openid.appauth.AuthorizationResponse;
//import net.openid.appauth.AuthorizationService;
//import net.openid.appauth.ClientAuthentication;
//import net.openid.appauth.ClientSecretBasic;
//import net.openid.appauth.TokenResponse;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

import org.oidc.sample.ConfigManager;
import org.oidc.sample.LoginRequest;
import org.oidc.sample.OAuth2TokenResponse;


public class UserInfoActivity extends AppCompatActivity {


    ConfigManager configManager;
    LoginRequest loginRequestObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

    }

    @Override
    protected void onStart() {

        super.onStart();
        configManager = Util.getConfigManager(this);
        loginRequestObj = Util.getLogin();
        handleAuthorizationResponse(getIntent());
    }

    @MainThread
    private void handleAuthorizationResponse(Intent intent) {

        loginRequestObj.handleAuthorization(intent);
        OAuth2TokenResponse tokenResponse2 = loginRequestObj.getTokenResponse();
        Log.i("Tokenqqqq", "here in the application:" + tokenResponse2.getIdToken());

        //readUserInfo(tokenResponse2.idToken, tokenResponse2.accessToken);


//        String clientSecret = configManager.getClientSecret();
//        AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
//        Map<String, String> additionalParameters = new HashMap<>();
//        additionalParameters.put("client_secret", clientSecret);
//        AuthorizationService service = new AuthorizationService(this);
//
//        ClientAuthentication clientAuthentication = new ClientSecretBasic(clientSecret);
//        service.performTokenRequest(response.createTokenExchangeRequest(additionalParameters),
//                clientAuthentication, this::handleCodeExchangeResponse);
    }


    @MainThread
    private void readUserInfo(String idToken, String accessToken){
        try {
            JSONParser parser = new JSONParser();
            String[] split = idToken.split("\\.");
            String decodeIDToken = new String(Base64.decode(split[1], Base64.URL_SAFE),"UTF-8");
            JSONObject json = (JSONObject) parser.parse(decodeIDToken);
            String userName = (String) json.get("sub");
            String email = (String) json.get("email");
            addUiElements(decodeIDToken, userName, email, accessToken);
            doLogout(idToken);

        } catch (Exception e) {
            //handle the exception.
        }
    }

    private void doLogout(String idToken){
        Button btnClick = findViewById(R.id.logout);
        btnClick.setOnClickListener(new UserInfoActivity.LogoutListener(idToken));
    }

    public class LogoutListener implements Button.OnClickListener {

        String idToken;
        LogoutListener(String idToken){
            this.idToken = idToken;
        }
        @Override
        public void onClick(View view) {
            singleLogout(view.getContext(),idToken );
        }
    }

    private void singleLogout(Context context, String idToken){
        StringBuffer url = new StringBuffer();
        url.append(configManager.getLogoutEndpointUri());
        url.append("?id_token_hint=");
        url.append(idToken);
        url.append("&post_logout_redirect_uri=");
        url.append(configManager.getRedirectUri());

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        customTabsIntent.launchUrl(context, Uri.parse(url.toString()));
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

