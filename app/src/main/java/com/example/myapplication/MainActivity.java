package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {


    private static final String AUTHORIZATION_RESPONSE_INTENT = "com.example.myapplication.HANDLE_AUTHORIZATION_RESPONSE";
    private static final String USED_INTENT = "USED_INTENT";
    public static final String LOG_TAG = "AppAuthSample";
    private JSONObject configJson;
    private String configHash;
    private String configError;
    private String clientId;
    private String clientSecret;
    private String scope;
    private Uri redirectUri;
    private Uri authEndpointUri;
    private Uri tokenEndpointUri;
    private Uri userInfoEndpointUri;
    private Uri logoutEndpointUri;
    private Boolean httpsRequired;
    Resources resources;


    private final AtomicReference<CustomTabsIntent> customTabIntent = new AtomicReference<>();
    ConfigManager configManager;

    public void getConfigManager(Context context){
        configManager = ConfigManager.getInstance(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnClick = (Button) findViewById(R.id.button);
        getConfigManager(this);
        btnClick.setOnClickListener(new AuthorizeListener());
    }

    public class AuthorizeListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {

            AuthorizationServiceConfiguration serviceConfiguration = new AuthorizationServiceConfiguration(
                    configManager.getAuthEndpointUri(),  configManager.getTokenEndpointUri());
            AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                    serviceConfiguration,
                    configManager.getClientId(),
                    ResponseTypeValues.CODE,
                    configManager.getRedirectUri()
            );
            builder.setScopes(configManager.getScope());
            AuthorizationRequest request = builder.build();
            AuthorizationService authorizationService = new AuthorizationService(view.getContext());
            CustomTabsIntent.Builder intentBuilder = authorizationService.createCustomTabsIntentBuilder(request.toUri());

            customTabIntent.set(intentBuilder.build());

            Intent completionIntent = new Intent(view.getContext(), UserInfoActivity.class);
            Intent cancelIntent = new Intent(view.getContext(), MainActivity.class);
            cancelIntent.putExtra("failed", true);
            cancelIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            authorizationService.performAuthorizationRequest(request, PendingIntent.getActivity(view.getContext(), 0,
                    completionIntent, 0), PendingIntent.getActivity(view.getContext(), 0, cancelIntent, 0),
                    customTabIntent.get());

        }
    }
}
