package com.application.norgevz.myticketon;

import android.content.Context;
import android.os.AsyncTask;

import com.application.norgevz.myticketon.rest.BaseClient;

import com.google.gson.Gson;
import com.loopj.android.http.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by norgevz on 12/8/2016.
 */
import com.application.norgevz.myticketon.rest.*;

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

    public void Validate(Credentials credentials, Context context){

        AuthenticateParams[] params = new AuthenticateParams[1];
        params[0] = new AuthenticateParams(context, credentials);

        new Authenticate().execute(params);
    }

    OnLogin listener;

    public void setListener(OnLogin listener){
        this.listener = listener;
    }

    public interface OnLogin{

        void OnResult(boolean value);

        void failLogin(String text);

        void registerCredentials( Credentials credentials );
    }


    class Authenticate extends AsyncTask<AuthenticateParams, Void , Void> {

        public boolean result;

        @Override
        protected Void doInBackground(AuthenticateParams... params)
        {

            final Credentials credentials = params[0].credentials;
            Context context = params[0].context;

            Gson gson = new Gson();
            SyncHttpClient client = new SyncHttpClient();

            try {

                String json = gson.toJson(credentials, Credentials.class);
                client.addHeader("Authorization", "amx" + " " + myKey);
                StringEntity requestData = new StringEntity(json);
                requestData.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                client.post(context,
                        getEndpoint(), requestData, "application/json", new TextHttpResponseHandler(){
                            //TODO Check for other onFailures and why is taking so long
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                if(listener != null) {
                                    listener.failLogin("Error loading data.Please check connectivity and try again");
                                }
                                System.out.println("KERNEL PANIC");
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                                if(listener != null){
                                    listener.registerCredentials( credentials );
                                    listener.OnResult(Boolean.parseBoolean(responseString));
                                }
                            }
                        });

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();

                if(listener != null) {
                    listener.failLogin("Error loading data.Please check connectivity and try again");
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }
}
