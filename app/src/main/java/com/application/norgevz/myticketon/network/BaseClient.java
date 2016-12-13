package com.application.norgevz.myticketon.network;


import com.application.norgevz.myticketon.settings.Settings;

/**
 * Created by norgevz on 12/8/2016.
 */

public class BaseClient {

    private String customEndpoint;

    private String key;

    public String getBaseEndpoint(){
        return Settings.getEndpoint();
    }

    public String getEndpoint() {
        return getBaseEndpoint() + customEndpoint;
    }


    public BaseClient(String customEndpoint, String key) {
        this.key = key;
        this.customEndpoint = customEndpoint;
    }
}
