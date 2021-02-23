package com.kokonetworks.theapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private Field field;
    private TextView tvLevel;
    private TextView tvScore;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                setContentView(R.layout.activity_main);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                setContentView(R.layout.activity_main_landscape);
                break;
        }

        field = findViewById(R.id.field);
        tvLevel = findViewById(R.id.tvLevel);
        btnStart = findViewById(R.id.btnStart);
        tvScore = findViewById(R.id.tvScore);

        setEventListeners();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(!tvScore.getText().toString().isEmpty()){
           outState.putString("level", tvLevel.getText().toString());
           outState.putString("score", tvScore.getText().toString());
        } else {
            if (field.getScore() > -1){
                outState.putLong("startTime", field.getMole().getStartTimeForLevel());
                outState.putInt("actualGameLevel", field.getMole().getCurrentLevel());
                outState.putInt("activeCircle", field.getCurrentCircle());
                outState.putInt("currentScore", field.getScore());
            }
        }

    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState.containsKey("level")) {
            tvScore.setText(savedInstanceState.getString("score"));
            tvLevel.setText(savedInstanceState.getString("level"));
        } else {
            btnStart.setVisibility(View.GONE);
            field.continueGame(savedInstanceState.getLong("startTime")
                    , savedInstanceState.getInt("actualGameLevel")
                    , savedInstanceState.getInt("activeCircle")
                    , savedInstanceState.getInt("currentScore"));
        }
        savedInstanceState.clear();
    }

    void setEventListeners(){
        btnStart.setOnClickListener(view -> {
            btnStart.setVisibility(View.GONE);
            tvScore.setVisibility(View.GONE);
            tvScore.setText("");
            field.startGame();
        });

        field.setListener(listener);
    }

    private final Field.Listener listener = new Field.Listener() {

        @Override
        public void onGameEnded(int score) {
            btnStart.setVisibility(View.VISIBLE);
            tvScore.setVisibility(View.VISIBLE);
            tvScore.setText(String.format(getString(R.string.your_score), score));
        }

        @Override
        public void onLevelChange(int level) {
            tvLevel.setText(String.format(getString(R.string.level), level));
        }
    };
}