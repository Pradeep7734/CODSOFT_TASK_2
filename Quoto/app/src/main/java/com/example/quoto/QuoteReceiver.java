package com.example.quoto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class QuoteReceiver extends BroadcastReceiver {

//    private final String URL = "https://api.quotable.io/quotes?limit=1";
    private final String URL = "https://api.quotable.io/random";
    public static final String ACTION_QUOTE_RECEIVED = "com.example.quoto.QUOTE_RECEIVED";
    public static final String QUOTE = "quote";
    public static final String AUTHOR = "author";
    @Override
    public void onReceive(Context context, Intent intent) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            String quote = jsonObject.getString("content");
                            String author = jsonObject.getString("author");

                            Intent sendQuoteData = new Intent(ACTION_QUOTE_RECEIVED);
                            sendQuoteData.putExtra(QUOTE, quote);
                            sendQuoteData.putExtra(AUTHOR, author);

                            LocalBroadcastManager.getInstance(context).sendBroadcast(sendQuoteData);
                        } catch (JSONException e) {
                            Log.e("QuoteReceiver", "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                volleyError -> Log.i("QuoteReceiver", "Error :" + volleyError.toString())
        );

        requestQueue.add(jsonObjectRequest);
    }

}
