package com.androstock.loginregistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText username, email, password;
    Button signupBtn;
    private ProgressBar spinner;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        TextView signUp_text = findViewById(R.id.signUp_text);
        signUp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
            }
        });
        email = (EditText) findViewById(R.id.log_email);
        password = (EditText) findViewById(R.id.log_password);
        login = findViewById(R.id.btnLogin);
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OpenBusActivity();
//            }
//        });

        // get values

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                try {
                    if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
                        Log.e("pass->>", password.getText().toString());

                        String postUrl= "http://uiulivebus.wasdpa-bd.org/server/apis/login.php";
                        String postBody="{" +

                                "    \"email\": \""+ email.getText() +"\",\n" +
                                "    \"password\": \""+ password.getText() +"\"\n" +
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

                    } else {
                        if(email.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Email can't be empty!", Toast.LENGTH_LONG).show();
                        } else if(password.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Password can't be empty!", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public void onSuccessResponse(JSONObject response) {
        String msg = "";

        try {
            JSONArray data = response.getJSONArray("data");
//                    Log.d("---->>> ", data.getString(0));
            JSONObject dataObj = new JSONObject(data.get(0).toString());
            Log.d("bus ID: ", dataObj.getString("bus_id"));
            if(dataObj.getString("bus_id").equals("null")) {
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();


//            startActivity(new Intent(LoginActivity.this, BusActivity.class));
//            finish();

                Intent intent = new Intent(LoginActivity.this, BusActivity.class);
                intent.putExtra("id", dataObj.getString("id"));
                startActivityForResult(intent, 200);
                email.setText("");
                password.setText("");
                progress.dismiss();
            } else {
                Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                intent.putExtra("id", dataObj.getString("id"));
                startActivityForResult(intent, 200);
                email.setText("");
                password.setText("");
                progress.dismiss();
            }
//

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onFailureResponse(JSONObject response) {
        Toast.makeText(getApplicationContext(), "Try again!", Toast.LENGTH_LONG).show();


        email.setText("");
        password.setText("");
        progress.dismiss();
    }
    public void OpenBusActivity(){
        Intent intent = new Intent(this,BusActivity.class);
        startActivity(intent);
    }
}
