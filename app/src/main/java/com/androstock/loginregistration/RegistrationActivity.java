package com.androstock.loginregistration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.*;

import org.json.JSONObject;

import java.io.IOException;


public class RegistrationActivity extends AppCompatActivity {

    EditText username, email, password;
    Button signupBtn;
    TextView signIn_text;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        username = (EditText) findViewById(R.id.reg_username);
        email = (EditText) findViewById(R.id.reg_email);
        password = (EditText) findViewById(R.id.reg_password);
        signupBtn = (Button) findViewById(R.id.reg_btn);

        // get values
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!username.getText().toString().equals("") && !email.getText().toString().equals("") && !password.getText().toString().equals("")) {
                        Log.e("pass->>", password.getText().toString());

                        String postUrl= "http://uiulivebus.wasdpa-bd.org/server/apis/signup.php";
                        String postBody="{" +
                                "    \"username\": \""+ username.getText() +"\",\n" +
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
                        if(username.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Username can't be empty!", Toast.LENGTH_LONG).show();
                        } else if(email.getText().toString().equals("")) {
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






        signIn_text = findViewById(R.id.signIn_text);

        signIn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
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

            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();

            username.setText("");
            email.setText("");
            password.setText("");

            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onFailureResponse(JSONObject response) {
        Toast.makeText(getApplicationContext(), "Try again!", Toast.LENGTH_LONG).show();

        username.setText("");
        email.setText("");
        password.setText("");
    }



}
