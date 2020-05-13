package com.example.myapplication;

import android.content.Context;

import org.oidc.agent.ConfigManager;
import org.oidc.agent.LoginService;

public class Util {

    private static ConfigManager mConfigManager;
    private static LoginService mLogin;

    public static ConfigManager getConfigManager(Context context) {
        if(mConfigManager == null) {
            mConfigManager = ConfigManager.getInstance(context, R.raw.config);
        }
        return mConfigManager;

    }

    public static LoginService getmLogin(){
        if(mLogin == null) {
            mLogin = new LoginService();
        }
        return mLogin;
    }
}
