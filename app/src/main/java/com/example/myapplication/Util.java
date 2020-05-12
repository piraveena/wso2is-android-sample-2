package com.example.myapplication;

import android.content.Context;

import org.oidc.sample.ConfigManager;
import org.oidc.sample.LoginRequest;

public class Util {

    static ConfigManager configManager;
    static LoginRequest login;

    public static ConfigManager getConfigManager(Context context) {
        if(configManager == null) {
            configManager = ConfigManager.getInstance(context, R.raw.config);
        }
        return configManager;

    }

    public static LoginRequest getLogin(){
        if(login == null) {
            login = new LoginRequest();
        }
        return login;
    }
}
