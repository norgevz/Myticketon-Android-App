package com.example.norgevz.myticketon.rest;


import com.loopj.android.http.*;

/**
 * Created by norgevz on 12/8/2016.
 */

public class BaseClient {

    private static String baseEndpoint = "http://10.6.120.12:7000";

    private String customEndpoint;

    private String key;

    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return baseEndpoint + relativeUrl;
    }


    public String getEndpoint() {
        return baseEndpoint + customEndpoint;
    }

    public void setCustomEndpoint(String customEndpoint) {
        this.customEndpoint = customEndpoint;
    }

    public String getKey() {
        return key;
    }

    public BaseClient(String customEndpoint, String key) {
        this.key = key;
        this.customEndpoint = customEndpoint;
    }
}
