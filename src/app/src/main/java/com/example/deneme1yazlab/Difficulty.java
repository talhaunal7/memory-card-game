package com.example.deneme1yazlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Difficulty extends AppCompatActivity {

    private int playerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        //Button button_4x4=findViewById(R.id.diff_4x4);

        Intent i = getIntent();
        int choose = i.getIntExtra("choose", 3);
        playerCount = choose;
    }


    public void startFour(View v) {
        Intent i = new Intent(this, Game4x4Activity.class);
        i.putExtra("playerC", playerCount);
        i.putExtra("diff", 4);
        startActivity(i);
    }

    public void startTwo(View v) {
        Intent i = new Intent(this, Game4x4Activity.class);
        i.putExtra("playerC", playerCount);
        i.putExtra("diff", 2);
        startActivity(i);
    }

    public void startSix(View v) {
        Intent i = new Intent(this, Game4x4Activity.class);
        i.putExtra("playerC", playerCount);
        i.putExtra("diff",6);
        startActivity(i);
    }


}