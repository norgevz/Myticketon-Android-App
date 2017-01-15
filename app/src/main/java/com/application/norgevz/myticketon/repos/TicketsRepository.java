package com.application.norgevz.myticketon.repos;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.application.norgevz.myticketon.models.Order;
import com.application.norgevz.myticketon.models.OrderHeader;
import com.application.norgevz.myticketon.models.ShowTimeInstance;
import com.application.norgevz.myticketon.models.Ticket;
import com.application.norgevz.myticketon.network.Credentials;
import com.application.norgevz.myticketon.network.VolleySingleton;
import com.application.norgevz.myticketon.settings.Settings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by norgevz on 12/13/2016.
 */

public class TicketsRepository {


    public interface onTicketsRequest{

        void onResponse(ArrayList<Ticket> tickets);

        void onFailResponse(String message);

        void OnUpdatedOrder(int position);
    }


    public void setListener(onTicketsRequest listener) {
        this._listener = listener;
    }

    private onTicketsRequest _listener;

    public void getTickets(ArrayList<String> ordersHeadersId) throws JSONException {
        getOrderHeaders(ordersHeadersId);
    }

    public void getOrderHeaders(ArrayList<String> ordersHeadersId) throws JSONException {

        String URL = Settings.getEndpoint() + "/nrest/Orders/GetOrderHeaders";
        final Gson gson = new Gson();
        String jsonString = gson.toJson(ordersHeadersId);

        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
        final String myKey = Settings.getKey();

        JSONArray jsonArray = new JSONArray(jsonString);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, URL, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response.toString());
                List<OrderHeader> headerList = gson.fromJson(response.toString(), new TypeToken<List<OrderHeader>>(){}.getType());
                ArrayList<String> showTimeInstanceIds = new ArrayList<>();

                for( OrderHeader header : headerList ){
                    for(Order order : header.Orders){
                        showTimeInstanceIds.add(order.ShowTimeInstanceId);
                    }
                }

                try {
                    getShowTimeInstanceIds(headerList, showTimeInstanceIds);
                } catch (JSONException e) {
                    _listener.onFailResponse("Error loading data. Please check connectivity and try again");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
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

    public void getShowTimeInstanceIds(final List<OrderHeader> headerList,  ArrayList<String> showTimeInstanceIds) throws JSONException {
        String URL = Settings.getEndpoint() + "/nrest/ShowTimeInstances/GetByIds";


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String jsonString = gson.toJson(showTimeInstanceIds);
        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
        final String myKey = Settings.getKey();

        JSONArray jsonArray = new JSONArray(jsonString);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, URL, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Ticket> tickets = new ArrayList<>();
                System.out.println(response.toString());
                List<ShowTimeInstance> showTimeInstances = gson.fromJson(response.toString(), new TypeToken<List<ShowTimeInstance>>(){}.getType());

                for( OrderHeader header : headerList ){
                    for(Order order : header.Orders) {

                        order.OrderHeader = header;

                        ShowTimeInstance showTimeInstance = null;
                        for(ShowTimeInstance show : showTimeInstances){
                            if(order.ShowTimeInstanceId.compareTo(show.Id) == 0){
                                showTimeInstance = show;
                            }
                        }

                        Ticket ticket = new Ticket(
                                showTimeInstance.Auditorium.Theater.Name
                                ,showTimeInstance.Show.Name
                                ,showTimeInstance.StartTime
                                ,order
                        );

                        tickets.add(ticket);
                    }
                    header.Orders.clear();
                }
                _listener.onResponse(tickets);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
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

    public void updateOrder(final Order order, final int position) {
        order.Printed = !order.Printed;
        String URL = Settings.getEndpoint() + "/api/Orders";
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        final String json = gson.toJson(order, Order.class);
        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
        final String myKey = Settings.getKey();

        StringRequest request = new StringRequest(Request.Method.PUT, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        _listener.OnUpdatedOrder(position);
                        System.out.println("response " + response);
                    }
                }
                ,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                order.Printed = !order.Printed;
                _listener.onFailResponse("Error loading data. Please check connectivity and try again");
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
