package com.application.norgevz.myticketon;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.application.norgevz.myticketon.network.BaseClient;

import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by norgevz on 12/8/2016.
 */
import com.application.norgevz.myticketon.network.*;

public class Authenticator extends BaseClient{

    public static String myKey;

    private static String authEnpoint;

    static {
        authEnpoint = "/nrest/Users/Authenticate";
        myKey = "MTO_eRTUJHsCuQSHR+L3GxqOJyDmQpCgps102ciuabc=";
    }

    public Authenticator(String key) {
        super(authEnpoint);
    }

    public void Validate(Credentials credentials){

        Credentials[] params = new Credentials[1];
        params[0] = credentials;

        new Authenticate().execute(params);
    }

    OnLogin listener;

    public void setListener(OnLogin listener){
        this.listener = listener;
    }

    public interface OnLogin{

        void OnResult(boolean value, Credentials credentials);

        void failLogin(String text);
    }


    class Authenticate extends AsyncTask<Credentials, Void , Void> {

        @Override
        protected Void doInBackground(Credentials... params)
        {

            final Credentials credentials = params[0];
            Gson gson = new Gson();

            RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
            final String json = gson.toJson(credentials, Credentials.class);
            String URL = getEndpoint();

            StringRequest request = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            listener.OnResult(Boolean.valueOf(response), credentials);
                            System.out.println(response);
                        }
                    }
                    ,new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.failLogin("Error loading data. Please check connectivity and try again");
                    VolleyLog.e("Error: ", error.getMessage());
                }
            })
            {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return json == null ? null : json.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json, "utf-8");
                        return null;
                    }
                }
//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    String responseString = "";
//                    if (response != null) {
//                        responseString = String.valueOf(response.statusCode);
//                        // can get more details such as response.headers
//                    }
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "amx" + " " + myKey);
                    return headers;
                }
            };

            request.setShouldCache(false);
            queue.add(request);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }
}
