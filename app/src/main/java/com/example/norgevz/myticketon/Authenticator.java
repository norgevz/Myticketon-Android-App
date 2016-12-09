

package com.example.norgevz.myticketon;

import android.content.Context;
import android.os.AsyncTask;

import com.example.norgevz.myticketon.rest.BaseClient;

import com.google.gson.Gson;
import com.loopj.android.http.*;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by norgevz on 12/8/2016.
 */
import com.example.norgevz.myticketon.rest.*;

public class Authenticator extends BaseClient{

    public static String myKey;

    private static String authEnpoint;

    static {
        authEnpoint = "/nrest/Users/Authenticate";
        myKey = "MTO_eRTUJHsCuQSHR+L3GxqOJyDmQpCgps102ciuabc=";
    }

    public Authenticator(String key) {
        super(authEnpoint , key);
    }

    public boolean Validate(Credentials credentials, Context context){

        AuthenticateParams[] params = new AuthenticateParams[1];
        params[0] = new AuthenticateParams(context, credentials);

        new Authenticate().execute(params);
        return false;
    }


    class Authenticate extends AsyncTask<AuthenticateParams, Void , Void> {

        public boolean result;

        @Override
        protected Void doInBackground(AuthenticateParams... params)
        {

            Credentials credentials = params[0].credentials;
            Context context = params[0].context;

            Gson gson = new Gson();
            SyncHttpClient client = new SyncHttpClient();

            try {

                String json = gson.toJson(credentials, Credentials.class);
                client.addHeader("Authorization", "amx" + " " + myKey);
                StringEntity requestData= new StringEntity(json);
                requestData.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                client.post(context,
                        getEndpoint(), requestData, "application/json", new TextHttpResponseHandler(){

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                System.out.println("KERNEL PANIC");
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                result = Boolean.parseBoolean(responseString);
                            }
                        });

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }

    }

}
