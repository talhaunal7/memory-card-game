package com.example.deneme1yazlab;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Game4x4Activity extends AppCompatActivity implements View.OnClickListener {
    public final MediaType mediaType = MediaType.get("application/json");
    private int numberOfElements;
    private MemoryButton[] buttons;

    private int[] buttonPlaces;
    private MemoryButton selectedA;
    private MemoryButton selectedB;
    private int gameScore;
    private int gameScoreA;
    private int gameScoreB;
    private long gameTime;
    private boolean isBusy = false;
    private CountDownTimer timer;
    private int playerCount;
    private boolean turn = false;
    private int timeLimit;
    private MediaPlayer matchFound;
    private MediaPlayer timeOver;
    private MediaPlayer backgroundMusic;
    private MediaPlayer gameWon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game4x4);

        if (matchFound == null) {
            matchFound = MediaPlayer.create(this, R.raw.match_found_crop);
        }
        if (timeOver == null) {
            timeOver = MediaPlayer.create(this, R.raw.time_over);
        }
        if (backgroundMusic == null) {
            backgroundMusic = MediaPlayer.create(this, R.raw.game_music);
        }
        if (gameWon == null) {
            gameWon = MediaPlayer.create(this, R.raw.game_won_crop);
        }
        backgroundMusic.start();

        Intent a = getIntent();
        playerCount = a.getIntExtra("playerC", 3);
        int difficulty = a.getIntExtra("diff", 4);

        if (playerCount == 1) {
            timeLimit = 45000;
            TextView tx = findViewById(R.id.player_turn);
            String tr = "-";
            tx.setText(tr);
        } else {
            timeLimit = 60000;
            TextView tx = findViewById(R.id.player_turn);
            String tr = "SIRA : -A-";
            tx.setText(tr);
            TextView ty = findViewById(R.id.score_field);
            tr = "SKOR A: 0 - SKOR B: 0";
            ty.setText(tr);
        }

        GridLayout gridLayout = findViewById(R.id.grid_layout_4x4);
        gameScore = 0;
        gridLayout.setColumnCount(difficulty);
        gridLayout.setColumnCount(difficulty);

        numberOfElements = difficulty * difficulty;
        Future<String[]> future = getStringArray();

        String[] stringArr = new String[44];
        try {
            stringArr = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }



        buttons = new MemoryButton[numberOfElements];

        String[] buttonGraphicsBase64 = new String[numberOfElements / 2];
        int[] buttonGraphics = new int[numberOfElements / 2];

        Random rand = new Random();
        int[] numbers;
        switch (difficulty) {
            case 4:
                numbers = new int[8];
                break;
            case 6:
                numbers = new int[18];
                break;
            default:
                numbers = new int[2];
                break;
        }
        HashSet<Integer> chosenNumbers = new HashSet<>();
        if (difficulty == 4) {

            for (int i = 0; i < 2; i++) {
                int num = rand.nextInt(11);
                while (chosenNumbers.contains(num)) {
                    num = rand.nextInt(11);
                }
                numbers[i] = num;
                chosenNumbers.add(num);
            }
            for (int i = 2; i < 4; i++) {
                int num = rand.nextInt(11) + 11;
                while (chosenNumbers.contains(num)) {
                    num = rand.nextInt(11) + 11;
                }
                numbers[i] = num;
                chosenNumbers.add(num);
            }
            for (int i = 4; i < 6; i++) {
                int num = rand.nextInt(11) + 22;
                while (chosenNumbers.contains(num)) {
                    num = rand.nextInt(11) + 22;
                }
                numbers[i] = num;
                chosenNumbers.add(num);
            }
            for (int i = 6; i < 8; i++) {
                int num = rand.nextInt(11) + 33;
                while (chosenNumbers.contains(num)) {
                    num = rand.nextInt(11) + 33;
                }
                numbers[i] = num;
                chosenNumbers.add(num);
            }
        } else if (difficulty == 6) {
            for (int i = 0; i < 4; i++) {
                int num = rand.nextInt(11);
                while (chosenNumbers.contains(num)) {
                    num = rand.nextInt(11);
                }
                numbers[i] = num;
                chosenNumbers.add(num);
            }
            for (int i = 4; i < 8; i++) {
                int num = rand.nextInt(11) + 11;
                while (chosenNumbers.contains(num)) {
                    num = rand.nextInt(11) + 11;
                }
                numbers[i] = num;
                chosenNumbers.add(num);
            }
            for (int i = 8; i < 13; i++) {
                int num = rand.nextInt(11) + 22;
                while (chosenNumbers.contains(num)) {
                    num = rand.nextInt(11) + 22;
                }
                numbers[i] = num;
                chosenNumbers.add(num);
            }
            for (int i = 13; i < 18; i++) {
                int num = rand.nextInt(11) + 33;
                while (chosenNumbers.contains(num)) {
                    num = rand.nextInt(11) + 33;
                }
                numbers[i] = num;
                chosenNumbers.add(num);
            }
        } else {
            for (int i = 0; i < 2; i++) {
                int num = rand.nextInt(11);
                while (chosenNumbers.contains(num)) {
                    num = rand.nextInt(11);
                }
                numbers[i] = num;
                chosenNumbers.add(num);
            }
        }
        for (int i = 0; i < numberOfElements / 2; i++) {
            buttonGraphicsBase64[i] = stringArr[numbers[i]];
        }

        for (int i = 0; i < numberOfElements / 2; i++) {
            buttonGraphics[i] = i * 7;
        }

        buttonPlaces = new int[numberOfElements];
        shuffleButtonGraphics();

        for (int r = 0; r < difficulty; r++) {
            for (int c = 0; c < difficulty; c++) {

                MemoryButton tempButton = new MemoryButton(this, r, c, buttonGraphics[buttonPlaces[r * difficulty + c]], buttonGraphicsBase64[buttonPlaces[r * difficulty + c]], difficulty);
                tempButton.setId(View.generateViewId());// xml için id
                tempButton.setOnClickListener(this);
                buttons[r * difficulty + c] = tempButton;
                gridLayout.addView(tempButton);
            }
        }

        TextView timeText = (TextView) findViewById(R.id.timerText);
        timer = new CountDownTimer(timeLimit, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                gameTime = seconds;
                @SuppressLint("DefaultLocale") String time = String.format("%02d", seconds);
                timeText.setText(time);
            }

            @Override
            public void onFinish() {
                TextView winText = (TextView) findViewById(R.id.textView2);
                backgroundMusic.stop();
                timeOver.start();
                if (playerCount == 2) {
                    String s = (gameScoreA > gameScoreB) ? "SÜRE DOLDU KAZANAN A" : "SÜRE DOLDU KAZANAN B";
                    winText.setText(s);
                } else {
                    String s = "SÜRE DOLDU !!";
                    winText.setText(s);
                }


                for (int i = 0; i < numberOfElements; i++) {
                    buttons[i].flip();
                }

            }
        };
        timer.start();

    }


    public Future<String[]> getStringArray() {
        // create a CompletableFuture object that will hold the stringArray data
        CompletableFuture<String[]> future = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            future = new CompletableFuture<>();
        }

        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8800/basedata/getimages";


        try {
            Request request = new Request.Builder().url(url).get().build();

            CompletableFuture<String[]> finalFuture = future;
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("EEE", e + "");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String res = response.body().string();
                    res = res.replaceAll("[\\[\\]\"]", "");
                    String[] array = res.split(",");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        finalFuture.complete(array);
                    }
                }
            });

        } catch (Exception e) {
            Log.d("EEE", e + "");
        }
        return future;
    }


    protected void shuffleButtonGraphics() {
        Random rand = new Random();
        for (int i = 0; i < numberOfElements; i++) {
            buttonPlaces[i] = i % (numberOfElements / 2);
        }
        for (int i = 0; i < numberOfElements; i++) {
            int temp = buttonPlaces[i];
            int swapIndex = rand.nextInt(numberOfElements);
            buttonPlaces[i] = buttonPlaces[swapIndex];
            buttonPlaces[swapIndex] = temp;
        }
    }

    public void checkWin() {
        TextView t = (TextView) findViewById(R.id.textView2);
        int count = 0;
        for (int i = 0; i < numberOfElements; i++) {
            if (buttons[i].isMatched()) {
                count++;
            }
        }
        String winner = (gameScoreA > gameScoreB) ? "A" : "B";
        String s = (playerCount == 1) ? "KAZANDIN" : "KAZANAN " + winner;
        if (count == numberOfElements) {
            backgroundMusic.stop();
            gameWon.start();
            t.setText(s);
            timer.cancel();
        }
    }


    public void setScore(int hero, int house) {

        int houseCoefficient = (house == 0 || house == 1) ? 2 : 1;
        TextView tx = findViewById(R.id.score_field);
        if (playerCount == 1) {

            gameScore += (2L * hero * houseCoefficient) * (gameTime / 10.0);
            String scoreIndicator = "SKOR : " + gameScore; //+ " [Ev k.=" + houseCoefficient + " Kart=" + hero + " Zaman=" + gameTime + "]";
            tx.setText(scoreIndicator);
        } else {
            float calc = 2 * hero * houseCoefficient;
            if (!turn) {
                gameScoreA += 2 * hero * houseCoefficient;
            } else {
                gameScoreB += 2 * hero * houseCoefficient;
            }
            String scoreIndicator = "SKOR A: " + gameScoreA + " - SKOR B: " + gameScoreB;
            tx.setText(scoreIndicator);
        }
    }


    public void setScoreFalse(int hero1, int hero2, int house1, int house2) {
        int house1Coefficient = (house1 == 0 || house1 == 1) ? 2 : 1;
        int house2Coefficient = (house2 == 0 || house2 == 1) ? 2 : 1;
        TextView tx = findViewById(R.id.score_field);
        if (playerCount == 1) {
            if (house1 == house2) {
                gameScore -= ((hero1 + hero2) / house1Coefficient) * (gameTime / 10.0);
            } else {
                gameScore -= (((house1 + house2) / 2.0) * house1Coefficient * house2Coefficient) * (gameTime / 10.0);
            }
            String scoreIndicator = "SKOR : " + gameScore;//+ " Ev1=" + house1Coefficient + " Ev2=" + house2Coefficient + " K1=" + hero1 + " K2=" + hero2;
            tx.setText(scoreIndicator);
        } else {
            TextView ty = findViewById(R.id.player_turn);
            String turnString;
            double calc = (house1 == house2) ? ((double) (hero1 + hero2) / house1Coefficient) : (((house1 + house2) / 2.0) * house1Coefficient * house2Coefficient);
            if (!turn) {
                gameScoreA -= calc;
                turnString = "SIRA : B";
            } else {
                gameScoreB -= calc;
                turnString = "SIRA : A";
            }
            turn = !turn;
            String scoreIndicator = "SKOR A: " + gameScoreA + "  SKOR B: " + gameScoreB;
            ty.setText(turnString);
            tx.setText(scoreIndicator);


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundMusic.release();
        gameWon.release();
        timer.cancel();
    }

    public void pauseMusic(View v) {
        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        if (toggleButton.isChecked()) {
            backgroundMusic.pause();
        } else {
            backgroundMusic.start();
            ;
        }
    }

    @Override
    public void onClick(View view) {
        if (isBusy)
            return;

        MemoryButton button = (MemoryButton) view;
        if (button.isMatched)
            return;

        if (selectedA == null) {
            selectedA = button;
            selectedA.flip();
            return;
        }

        if (selectedA.getId() == button.getId()) {
            return;
        }
        int id = selectedA.getFrontDrawableId();

        if (selectedA.getFrontDrawableId() == button.getFrontDrawableId()) {
            matchFound.start();
            button.flip();
            button.setMatched(true);
            selectedA.setMatched(true);
            selectedA.setEnabled(false);
            button.setEnabled(false);
            selectedA = null;
            checkWin();
            setScore(button.score, button.house);
            return;

        } else {
            selectedB = button;
            selectedB.flip();
            isBusy = true;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setScoreFalse(selectedA.getScore(), selectedB.getScore(), selectedA.house, selectedB.house);
                    selectedA.flip();
                    selectedB.flip();
                    selectedB = null;
                    selectedA = null;
                    isBusy = false;
                }
            }, 1000);


        }


    }


}