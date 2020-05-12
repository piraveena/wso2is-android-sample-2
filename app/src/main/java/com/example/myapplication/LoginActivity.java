package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.oidc.sample.ConfigManager;
import org.oidc.sample.LoginRequest;

import java.util.concurrent.atomic.AtomicReference;

public class LoginActivity extends AppCompatActivity {


    private static final String AUTHORIZATION_RESPONSE_INTENT = "com.example.myapplication.HANDLE_AUTHORIZATION_RESPONSE";
    private static final String USED_INTENT = "USED_INTENT";
    public static final String LOG_TAG = "AppAuthSample";
    private static Context mContext;
    LoginRequest login;
    ConfigManager configManager;


    private final AtomicReference<CustomTabsIntent> customTabIntent = new AtomicReference<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        login = Util.getLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configManager = Util.getConfigManager(this);
        findViewById(R.id.login).setOnClickListener(v ->
                doAuthorization(this)
        );

    }

    /**
     * Handles the authorization code flow. Build the authorization request with the given
     * parameters and sent it to the IDP. If the authorization request is successful,
     * UserInfoActivity will handle it.
     *
     * @param context Context.
     */
    private void doAuthorization(Context context) {

        Intent completionIntent = new Intent(context, UserInfoActivity.class);
        Intent cancelIntent = new Intent(context, LoginActivity.class);
        cancelIntent.putExtra("failed", true);
        cancelIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        login.doAuthorization(context, configManager, PendingIntent.getActivity(context, 0,
                completionIntent, 0),  PendingIntent.getActivity(context, 0, cancelIntent, 0));
    }
}
