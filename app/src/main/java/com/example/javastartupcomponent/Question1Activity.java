package com.example.javastartupcomponent;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Question1Activity extends AppCompatActivity {
    private TextView tvSmsFeedback;
    private ProgressBar timerProgressBar;
    private String playerName;
    private String playerPhone;
    private int currentScore;
    private CountDownTimer countDownTimer;
    private boolean isAnswered = false;

    private Button btnOptionA, btnOptionB, btnOptionC, btnOptionD;

    private static final String CORRECT_ANSWER = "B";
    private static final String FEEDBACK_CORRECT = "Correct! Clean water (1 gal/person/day) and a working flashlight are essential emergency kit items.";
    private static final String FEEDBACK_INCORRECT = "Incorrect! Correct answer was B) Water & Flashlight.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q1);

        Intent incomingIntent = getIntent();
        playerName = incomingIntent.getStringExtra("PLAYER_NAME");
        playerPhone = incomingIntent.getStringExtra("PLAYER_PHONE");
        currentScore = incomingIntent.getIntExtra("CURRENT_SCORE", 0);

        TextView tvPlayerInfo = findViewById(R.id.tvProgress);
        TextView tvScore = findViewById(R.id.tvScore);
        TextView tvQuestion = findViewById(R.id.tvQuestion);
        tvSmsFeedback = findViewById(R.id.tvSmsFeedback);
        timerProgressBar = findViewById(R.id.timerProgressBar);

        btnOptionA = findViewById(R.id.btnOptionA);
        btnOptionB = findViewById(R.id.btnOptionB);
        btnOptionC = findViewById(R.id.btnOptionC);
        btnOptionD = findViewById(R.id.btnOptionD);

        tvPlayerInfo.setText(getString(R.string.player_info_q1, playerName != null ? playerName : ""));
        tvScore.setText(getString(R.string.score_fmt, currentScore));
        tvQuestion.setText("What is the most critical item to include in your emergency supply kit?");

        btnOptionA.setText("A) TV");
        btnOptionB.setText("B) Water & Flashlight");
        btnOptionC.setText("C) Books");
        btnOptionD.setText("D) Glassware");

        startQuestionTimer();

        btnOptionA.setOnClickListener(v -> processAnswer("A", btnOptionA));
        btnOptionB.setOnClickListener(v -> processAnswer("B", btnOptionB));
        btnOptionC.setOnClickListener(v -> processAnswer("C", btnOptionC));
        btnOptionD.setOnClickListener(v -> processAnswer("D", btnOptionD));
    }

    private void startQuestionTimer() {
        timerProgressBar.setMax(15);
        countDownTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                timerProgressBar.setProgress(secondsRemaining);
            }

            @Override
            public void onFinish() {
                if (!isAnswered) {
                    isAnswered = true;
                    highlightCorrectOnly();
                    tvSmsFeedback.setText("Time Expired! " + FEEDBACK_INCORRECT);
                    Toast.makeText(Question1Activity.this, R.string.time_expired, Toast.LENGTH_SHORT).show();
                    tvSmsFeedback.postDelayed(Question1Activity.this::navigateToNextQuestion, 5000);
                }
            }
        }.start();
    }

    private void processAnswer(String selectedOption, Button clickedButton) {
        if (isAnswered) return;
        isAnswered = true;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        disableAllButtons();
        if (selectedOption.equalsIgnoreCase(CORRECT_ANSWER)) {
            currentScore += 10;
            clickedButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            tvSmsFeedback.setText(FEEDBACK_CORRECT);
            Toast.makeText(this, R.string.correct_answer, Toast.LENGTH_SHORT).show();
        } else {
            clickedButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
            Button correctBtn = getCorrectButton();
            if (correctBtn != null) {
                correctBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            }
            tvSmsFeedback.setText(FEEDBACK_INCORRECT);
            Toast.makeText(this, R.string.incorrect_answer, Toast.LENGTH_SHORT).show();
        }
        tvSmsFeedback.postDelayed(this::navigateToNextQuestion, 5000);
    }

    private void highlightCorrectOnly() {
        disableAllButtons();
        Button correctBtn = getCorrectButton();
        if (correctBtn != null) {
            correctBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }
    }

    private void disableAllButtons() {
        if (btnOptionA != null) btnOptionA.setEnabled(false);
        if (btnOptionB != null) btnOptionB.setEnabled(false);
        if (btnOptionC != null) btnOptionC.setEnabled(false);
        if (btnOptionD != null) btnOptionD.setEnabled(false);
    }

    private Button getCorrectButton() {
        switch (CORRECT_ANSWER.toUpperCase()) {
            case "A":
            case "TRUE":
                return btnOptionA;
            case "B":
            case "FALSE":
                return btnOptionB;
            case "C":
                return btnOptionC;
            case "D":
                return btnOptionD;
            default:
                return null;
        }
    }

    private void navigateToNextQuestion() {
        Intent intent = new Intent(Question1Activity.this, Question2Activity.class);
        intent.putExtra("PLAYER_NAME", playerName);
        intent.putExtra("PLAYER_PHONE", playerPhone);
        intent.putExtra("CURRENT_SCORE", currentScore);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
