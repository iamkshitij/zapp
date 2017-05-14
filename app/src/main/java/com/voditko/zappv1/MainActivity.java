package com.voditko.zappv1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements Keys {
    EditText queryTxt, responseTxt;
    Button sendRequest;
    String responseQuery;
    RequestQueue requestQueue;
    String url;
    String url2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queryTxt = (EditText) findViewById(R.id.queryTxt);
        responseTxt = (EditText) findViewById(R.id.responseTxt);
        sendRequest = (Button) findViewById(R.id.sendRequest);

        sendRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                responseQuery = queryTxt.getText().toString();

                StringTokenizer stringTokenizer = new StringTokenizer(responseQuery);

                int count = stringTokenizer.countTokens();
                if (count == 1) {
                    Toast.makeText(MainActivity.this, "Latitude + longitude", Toast.LENGTH_SHORT).show();
                    Log.d("COUNT 1", "near me");
                    sendRequestSingle(responseQuery);
                } else {
                    responseQuery = responseQuery.replace(" ", "+");
                    sendRequestToServer(responseQuery);
                }

                responseTxt.setText(" ");
            }
        });

        requestQueue = Volley.newRequestQueue(this);

    }

    private void sendRequestSingle(String responseQuery) {
        url2 = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+responseQuery+"&location=28.6153147,77.0737148&radius=500&key=" + KEYS;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //responseTxt.setText(response);
                //Toast.makeText(MainActivity.this, " "+responseQuery, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray = jsonObj.getJSONArray("results");
                    List<String> name = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject result = jsonArray.getJSONObject(i);

                        name.add(result.getString("name"));


                    }

                    for (String names : name
                            ) {
                        responseTxt.append("\n" + names);
                        // System.out.println(names);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseTxt.setText("Error in fetching data " + error.getMessage());


            }
        });
        requestQueue.add(stringRequest);

    }

    private void sendRequestToServer(final String responseQuery) {
        url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + responseQuery + "&key=" + KEYS;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //responseTxt.setText(response);
                //Toast.makeText(MainActivity.this, " "+responseQuery, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray = jsonObj.getJSONArray("results");
                    List<String> name = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject result = jsonArray.getJSONObject(i);

                        name.add(result.getString("name"));


                    }

                    for (String names : name
                            ) {
                        responseTxt.append("\n" + names);
                        // System.out.println(names);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseTxt.setText("Error in fetching data " + error.getMessage());
                String responseQueryT;
                responseQueryT = responseQuery.replace("+", " ");
                responseTxt.setText(" invalid query -> " + responseQueryT);

            }
        });
        requestQueue.add(stringRequest);
    }
}
