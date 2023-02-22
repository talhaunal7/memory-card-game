package com.example.deneme1yazlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Player extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
    }

    public void singlePlay(View v){
        Intent i =new Intent(this,Difficulty.class);
        i.putExtra("choose",1);
        startActivity(i) ;
    }

    public void multiPlay(View v){
        Intent i =new Intent(this,Difficulty.class);
        i.putExtra("choose",2);
        startActivity(i) ;
    }
}