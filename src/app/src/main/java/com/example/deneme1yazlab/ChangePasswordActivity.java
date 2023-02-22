package com.example.deneme1yazlab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    public static final MediaType mediaType = MediaType.get("application/json");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }


    public void setStatus(int code) {
        TextView status = ((TextView) findViewById(R.id.change_password_status));
        if (code == 200) {
            status.setText("password has changed !!");
        } else if (code == 404) {
            status.setText("user not found");
        } else if (code == 401) {
            status.setText("incorrect current password");
        } else {
            status.setText("hata ?");
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    public void changePasswordSubmit(View v) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8800/basedata/change-password";

        try {
            JSONObject json = new JSONObject();
            String name = ((TextView) findViewById(R.id.change_password_username)).getText().toString();
            String currentP = ((TextView) findViewById(R.id.change_password_current)).getText().toString();
            String newP = ((TextView) findViewById(R.id.change_password_new)).getText().toString();

            RequestBody body = RequestBody.create("{\"username\":\"" + name + "\", \"currentPassword\":\"" + currentP + "\", \"newPassword\":\"" + newP + "\"}", mediaType);
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
}