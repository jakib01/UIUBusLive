package com.androstock.loginregistration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BusActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button btnSave;
    Spinner spinnerRoutes, spinnerBuses, spinnerPUPoints, spinnerRMPoints;
    List<String> list_routes, list_buses, list_pup, list_rp;
    JSONArray routes_list, buses_list, pickUpPoints_list, reminderPoints_list;
    ArrayList<List<String>> routes, buses, pupoints, rpoints;

    String user_id = "", route_id = "", pickup_point_id = "", reminder_point_id = "", bus_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);

        btnSave = (Button) findViewById(R.id.move_to_map);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BusActivity.this,"You Selected : r: " + route_id + ", b: " + bus_id +", p: " + pickup_point_id + ", rp: " + reminder_point_id,Toast.LENGTH_LONG).show();
                registerData();
//                startActivity(new Intent(BusActivity.this, MapsActivity.class));
//                finish();
            }
        });


        spinnerRoutes = (Spinner) findViewById(R.id.spinnerRoutes) ;
        spinnerBuses = (Spinner) findViewById(R.id.spinnerBuses) ;
        spinnerPUPoints = (Spinner) findViewById(R.id.spinnerPickup) ;
        spinnerRMPoints = (Spinner) findViewById(R.id.spinnerReminder) ;
        routes = new ArrayList<>();

        Intent intent = getIntent();
        user_id = intent.getStringExtra("id");
        /// rest call
        try {
        String postUrl= "http://uiulivebus.wasdpa-bd.org/server/apis/get-routes.php";
        String postBody="{" +

                "    \"opt\": \"get-routes\"    " +
                "}";

        Log.e("str ---> ", postBody);
        Object resJson = new JSONObject();
        resJson = postJson(postUrl,postBody);
        Log.d("Res json: ", resJson.toString());
        JSONObject json = new JSONObject(resJson.toString());

        if(json.getInt("code") == 200) {
            routes_list = json.getJSONArray("data");
            Log.d("routes_list: ", routes_list.toString());
            list_routes = new ArrayList<String>();
            list_routes.add("--Select Route--");
            for(int i = 0; i < json.getJSONArray("data").length(); i++){
                JSONObject dataObj = new JSONObject(json.getJSONArray("data").get(i).toString());
                list_routes.add(dataObj.getString("name") + "_" + (i+1) + ": " + dataObj.getString("full_route"));
            }

//            routes.add(list_routes);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, list_routes);
            spinnerRoutes.setAdapter(adapter);
        } else {
            onFailureResponse(json);
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        spinnerRoutes.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                // TODO Auto-generated method stub
//                Toast.makeText(BusActivity.this,"You Selected : "+ i                        + list_routes.get(i)+" Level ",Toast.LENGTH_SHORT).show();
//                Log.d("", "You Selected : "                        + list_routes.get(i)+" Level ");
                try {
                    JSONObject dataObj = new JSONObject(routes_list.get(i-1).toString());

                    route_id = dataObj.get("id").toString();
                    Log.d("", "Your Selected Rout : " + route_id);
                    getBusList(route_id);
                    getPickUpPointList(route_id);
                    getReminderPointList(route_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });



    }

    public void getBusList(String route_id) {
        try {
            String postUrl= "http://uiulivebus.wasdpa-bd.org/server/apis/get-buses.php";
            String postBody="{" +

                    "    \"opt\": \"get-buses\",\n" +
                    "    \"route_id\": \" " + route_id + "\"     " +
                    "}";

            Log.e("str ---> ", postBody);
            Object resJson = new JSONObject();
            resJson = postJson(postUrl,postBody);
            Log.d("Res json: ", resJson.toString());
            JSONObject json = new JSONObject(resJson.toString());

            if(json.getInt("code") == 200) {
                buses_list = json.getJSONArray("data");
                Log.d("bus_list: ", buses_list.toString());
                list_buses = new ArrayList<String>();
                list_buses.add("--Select Bus--");
                for(int i = 0; i < json.getJSONArray("data").length(); i++){
                    JSONObject dataObj = new JSONObject(json.getJSONArray("data").get(i).toString());
                    list_buses.add(dataObj.getString("name"));
                }

//                buses.add(list_buses);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item, list_buses);
                spinnerBuses.setAdapter(adapter);


            } else {
                onFailureResponse(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        spinnerBuses.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                // TODO Auto-generated method stub
//                Toast.makeText(BusActivity.this,"You Selected : "+ i  + list_routes.get(i)+" Level ",Toast.LENGTH_SHORT).show();
//                Log.d("", "You Selected : "+ i  + list_buses.get(i)+" Level ");
                try {
                    JSONObject dataObj = new JSONObject(buses_list.get(i-1).toString());

//                    getPickUpPointList(dataObj.get("id").toString());
                    bus_id = dataObj.get("id").toString();
                    Log.d("", "Your Selected Bus : " + bus_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
    }


    public void getPickUpPointList(String route_id) {
        try {
            String postUrl= "http://uiulivebus.wasdpa-bd.org/server/apis/get-pickUpPoints.php";
            String postBody="{" +

                    "    \"opt\": \"get-pickuppoints\",\n" +
                    "    \"route_id\": \" " + route_id + "\"     " +
                    "}";

            Log.e("str ---> ", postBody);
            Object resJson = new JSONObject();
            resJson = postJson(postUrl,postBody);
            Log.d("Res json: ", resJson.toString());
            JSONObject json = new JSONObject(resJson.toString());

            if(json.getInt("code") == 200) {
                pickUpPoints_list = json.getJSONArray("data");
                Log.d("pup_list: ", pickUpPoints_list.get(0).toString());
                list_pup = new ArrayList<String>();
                list_pup.add("--Select Pick Up Point--");
                for(int i = 0; i < json.getJSONArray("data").length(); i++){
                    JSONObject dataObj = new JSONObject(json.getJSONArray("data").get(i).toString());
                    list_pup.add(dataObj.getString("name"));
                }

//                buses.add(list_buses);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item, list_pup);
                spinnerPUPoints.setAdapter(adapter);


            } else {
                onFailureResponse(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        spinnerPUPoints.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                // TODO Auto-generated method stub
//                Toast.makeText(BusActivity.this,"You Selected : "+ i  + list_routes.get(i)+" Level ",Toast.LENGTH_SHORT).show();
//                Log.d("", "You Selected : "+ i  + list_pup.get(i)+" Level ");
                try {
                    JSONObject dataObj = new JSONObject(pickUpPoints_list.get(i-1).toString());

//                    getPickUpPointList(dataObj.get("id").toString());
                    pickup_point_id = dataObj.get("id").toString();
                    Log.d("", "Your Selected PP : " + pickup_point_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
    }

    public void getReminderPointList(String route_id) {
        try {
            String postUrl= "http://uiulivebus.wasdpa-bd.org/server/apis/get-reminderpoints.php";
            String postBody="{" +

                    "    \"opt\": \"get-buses\",\n" +
                    "    \"route_id\": \" " + route_id + "\"     " +
                    "}";

            Log.e("str ---> ", postBody);
            Object resJson = new JSONObject();
            resJson = postJson(postUrl,postBody);
            Log.d("Res json: ", resJson.toString());
            JSONObject json = new JSONObject(resJson.toString());

            if(json.getInt("code") == 200) {
                reminderPoints_list = json.getJSONArray("data");
                Log.d("rmp_list: ", reminderPoints_list.toString());
                list_rp = new ArrayList<String>();
                list_rp.add("--Select Reminder Point--");
                for(int i = 0; i < json.getJSONArray("data").length(); i++){
                    JSONObject dataObj = new JSONObject(json.getJSONArray("data").get(i).toString());
                    list_rp.add(dataObj.getString("name"));

                }

//                buses.add(list_buses);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item, list_rp);
                spinnerRMPoints.setAdapter(adapter);


            } else {
                onFailureResponse(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        spinnerRMPoints.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                // TODO Auto-generated method stub
//                Toast.makeText(BusActivity.this,"You Selected : " + i + list_routes.get(i)+" Level ",Toast.LENGTH_SHORT).show();
//                Log.d("", "You Selected : " + list_rp.get(i)+" Level ");
                try {
                    JSONObject dataObj = new JSONObject(reminderPoints_list.get(i-1).toString());

//                    getPickUpPointList(dataObj.get("id").toString());
                    reminder_point_id = dataObj.get("id").toString();
                    Log.d("", "Your Selected RP : " + reminder_point_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
    }

    public Object postJson(String url, String jsonStr){

        final MediaType mediaType
                = MediaType.parse("application/json");

        OkHttpClient httpClient = new OkHttpClient();

        //post json using okhttp
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, jsonStr))
                .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("TAG:", "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            }

        } catch (IOException e) {
            Log.e("TAG", "error in getting response for json post request okhttp");
        }
        return null;
    }


    public void registerData() {
        try {
            String postUrl= "http://uiulivebus.wasdpa-bd.org/server/apis/insert-user-choice.php";
            String postBody="{" +

                    "    \"userId\": \""+ user_id +"\",\n" +
                    "    \"routeId\": \""+ route_id +"\",\n" +
                    "    \"pickUpPointId\": \""+ pickup_point_id +"\",\n" +
                    "    \"reminderPointId\": \""+ reminder_point_id +"\",\n" +
                    "    \"busId\": \"" + bus_id + "\"     " +
                    "}";

            Log.e("str ---> ", postBody);
            Object resJson = new JSONObject();
            resJson = postJson(postUrl,postBody);
            Log.d("Res json: ", resJson.toString());
            JSONObject json = new JSONObject(resJson.toString());

            if(json.getInt("code") == 200) {
                onSuccessResponse(json);
            } else {
                onFailureResponse(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onSuccessResponse(JSONObject response) {
        String msg = "";

        try {

            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();




//            startActivity(new Intent(LoginActivity.this, BusActivity.class));
//            finish();
//            Intent intent = new Intent(BusActivity.this, MapsActivity.class);
            startActivity(new Intent(BusActivity.this, MapsActivity.class));
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onFailureResponse(JSONObject response) {
        Toast.makeText(getApplicationContext(), "Try again!", Toast.LENGTH_LONG).show();

//
//        email.setText("");
//        password.setText("");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
