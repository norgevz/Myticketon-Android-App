package com.application.norgevz.myticketon.repos;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.application.norgevz.myticketon.models.SalesByAuditorium;
import com.application.norgevz.myticketon.models.ShowTimeInstance;
import com.application.norgevz.myticketon.network.VolleySingleton;
import com.application.norgevz.myticketon.settings.Settings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by norgevz on 12/16/2016.
 */

public class SalesRepository {

    public interface onSalesRequest{

        void onResponse(List<SalesByAuditorium> tickets);

        void onFailResponse(String message);
    }

    public void setListener(SalesRepository.onSalesRequest listener) {
        this._listener = listener;
    }

    private SalesRepository.onSalesRequest _listener;

    public void getSales(String providerId, Date date){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String day = dateFormat.format(date);
        String customEndPoint =
                String.format("/nrest/Statistics/GetSalesByAuditorium?providerId=%s&day=%s  ", providerId, day);

        String URL = Settings.getEndpoint() + customEndPoint;
        final String myKey = Settings.getKey();
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response.toString());
                List<SalesByAuditorium> showTimeInstances =
                        gson.fromJson(response.toString(), new TypeToken<List<SalesByAuditorium>>(){}.getType());
                _listener.onResponse(showTimeInstances);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _listener.onFailResponse("Error loading data. Please check connectivity and try again");
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "amx" + " " + myKey);
                return headers;
            }
        };
        queue.add(request);
    }

}
