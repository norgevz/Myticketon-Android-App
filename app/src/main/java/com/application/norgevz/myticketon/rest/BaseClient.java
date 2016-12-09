package com.application.norgevz.myticketon.rest;


import com.application.norgevz.myticketon.settings.Settings;
import com.loopj.android.http.*;

/**
 * Created by norgevz on 12/8/2016.
 */

public class BaseClient {

    private String customEndpoint;

    private String key;

    private static AsyncHttpClient client = new AsyncHttpClient();


//    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        client.get(getAbsoluteUrl(url), params, responseHandler);
//    }
//
//    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        client.post(getAbsoluteUrl(url), params, responseHandler);
//    }


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
