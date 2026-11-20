package com.example.javastartupcomponent;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Question5Activity extends AppCompatActivity {
    private TextView tvSmsFeedback;
    private ProgressBar timerProgressBar;
    private String playerName;
    private String playerPhone;
    private int currentScore;
    private CountDownTimer countDownTimer;
    private boolean isAnswered = false;

    // Question 5 Correct Answer: A
    private static final String CORRECT_ANSWER = "A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q5);

        // 1. Retrieve information passed from previous intent (Question4Activity)
        Intent incomingIntent = getIntent();
        playerName = incomingIntent.getStringExtra("PLAYER_NAME");
        playerPhone = incomingIntent.getStringExtra("PLAYER_PHONE");
        currentScore = incomingIntent.getIntExtra("CURRENT_SCORE", 0);

        // 2. Initialize Views
        TextView tvPlayerInfo = findViewById(R.id.tvProgress);
        TextView tvScore = findViewById(R.id.tvScore);
        tvSmsFeedback = findViewById(R.id.tvSmsFeedback);
        timerProgressBar = findViewById(R.id.timerProgressBar);

        Button btnOptionA = findViewById(R.id.btnOptionA);
        Button btnOptionB = findViewById(R.id.btnOptionB);
        Button btnOptionC = findViewById(R.id.btnOptionC);
        Button btnOptionD = findViewById(R.id.btnOptionD);

        // 3. Configure layout for Question 5
        btnOptionA.setText(R.string.option_a);
        btnOptionB.setText(R.string.option_b);
        btnOptionC.setText(R.string.option_c);
        btnOptionD.setText(R.string.option_d);

        btnOptionA.setVisibility(View.VISIBLE);
        btnOptionB.setVisibility(View.VISIBLE);
        btnOptionC.setVisibility(View.VISIBLE);
        btnOptionD.setVisibility(View.VISIBLE);

        // 4. Populate Question 5 Details
        tvPlayerInfo.setText("Q5 of 10 | Player: " + (playerName != null ? playerName : ""));
        tvScore.setText(getString(R.string.score_fmt, currentScore));

        // 5. Start 15-second Countdown Timer
        startQuestionTimer();

        // 6. Button Click Listeners
        btnOptionA.setOnClickListener(v -> processAnswer("A"));
        btnOptionB.setOnClickListener(v -> processAnswer("B"));
        btnOptionC.setOnClickListener(v -> processAnswer("C"));
        btnOptionD.setOnClickListener(v -> processAnswer("D"));
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
                    Toast.makeText(Question5Activity.this, "Time Expired for Q5!", Toast.LENGTH_SHORT).show();
                    navigateToSummary();
                }
            }
        }.start();
    }

    private void processAnswer(String selectedOption) {
        if (isAnswered) return;
        isAnswered = true;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (selectedOption.equalsIgnoreCase(CORRECT_ANSWER)) {
            currentScore += 10;
            tvSmsFeedback.setText(R.string.sms_correct);
            Toast.makeText(this, R.string.correct_answer, Toast.LENGTH_SHORT).show();
        } else {
            tvSmsFeedback.setText(getString(R.string.sms_incorrect, CORRECT_ANSWER));
            Toast.makeText(this, R.string.incorrect_answer, Toast.LENGTH_SHORT).show();
        }
        tvSmsFeedback.postDelayed(this::navigateToSummary, 1500);
    }

    private void navigateToSummary() {
        Intent intent = new Intent(Question5Activity.this, SummaryActivity.class);
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