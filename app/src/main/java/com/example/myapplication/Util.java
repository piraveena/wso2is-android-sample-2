package com.example.myapplication;

import android.content.Context;

import org.oidc.agent.ConfigManager;
import org.oidc.agent.LoginService;

public class Util {

    static ConfigManager configManager;
    static LoginService login;

    public static ConfigManager getConfigManager(Context context) {
        if(configManager == null) {
            configManager = ConfigManager.getInstance(context, R.raw.config);
        }
        return configManager;

    }

    public static LoginService getLogin(){
        if(login == null) {
            login = new LoginService();
        }
        return login;
    }
}
