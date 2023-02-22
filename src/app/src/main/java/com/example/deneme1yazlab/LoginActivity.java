package com.example.deneme1yazlab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    public static final MediaType mediaType = MediaType.get("application/json");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void setStatus(int code) {
        TextView status = ((TextView) findViewById(R.id.login_register_status));
        Intent diff = new Intent(this, Player.class);
        if (code == 200) {
            status.setText("giris basarili");
            startActivity(diff);
        } else {
            status.setText("hatali giris");
        }
    }

    public void setStatusRegister(int code) {
        TextView status = ((TextView) findViewById(R.id.login_register_status));
        if (code == 200) {
            status.setText("kayit basarili");
        } else {
            status.setText("kayit basarisiz");
        }
    }

    public void changePassword(View v){
        Intent ch=new Intent(this,ChangePasswordActivity.class);
        startActivity(ch);
    }


    public void loginButton(View v) {

        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8800/auth/login";

        try {
            JSONObject json = new JSONObject();
            String name = ((TextView) findViewById(R.id.username_field)).getText().toString();
            String password = ((TextView) findViewById(R.id.password_field)).getText().toString();

            RequestBody body = RequestBody.create("{\"username\":\"" + name + "\", \"password\":\"" + password + "\"}", mediaType);
            Request request = new Request.Builder().url(url).post(body).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("EEE", e + "");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    setStatus(response.code());
                }
            });

        } catch (Exception e) {
            Log.d("EEE", e + "");
        }
    }

    public void registerButton(View v) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8800/auth/register";

        try {
            JSONObject json = new JSONObject();
            String name = ((TextView) findViewById(R.id.username_register_field)).getText().toString();
            String password = ((TextView) findViewById(R.id.password_register_field)).getText().toString();
            String email = ((TextView) findViewById(R.id.email_register_field)).getText().toString();
            RequestBody body = RequestBody.create("{\"username\":\"" + name + "\", \"password\":\"" + password + "\", \"email\":\"" + email + "\"}", mediaType);

            Request request = new Request.Builder().url(url).post(body).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    Log.d("EEE", e + "");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    setStatusRegister(response.code());
                }
            });

        } catch (Exception e) {
            Log.d("EEE", e + "");
        }
    }
}