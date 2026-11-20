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

public class Question3Activity extends AppCompatActivity {
    private TextView tvSmsFeedback;
    private ProgressBar timerProgressBar;
    private String playerName;
    private String playerPhone;
    private int currentScore;
    private CountDownTimer countDownTimer;
    private boolean isAnswered = false;

    private static final String CORRECT_ANSWER = "B";
    private static final String FEEDBACK_CORRECT = "Correct! Open spaces away from buildings, power lines, and trees protect you from falling debris.";
    private static final String FEEDBACK_INCORRECT = "Incorrect! Correct answer was B) Open field away from buildings.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q3);

        Intent incomingIntent = getIntent();
        playerName = incomingIntent.getStringExtra("PLAYER_NAME");
        playerPhone = incomingIntent.getStringExtra("PLAYER_PHONE");
        currentScore = incomingIntent.getIntExtra("CURRENT_SCORE", 0);

        TextView tvPlayerInfo = findViewById(R.id.tvProgress);
        TextView tvScore = findViewById(R.id.tvScore);
        TextView tvQuestion = findViewById(R.id.tvQuestion);
        tvSmsFeedback = findViewById(R.id.tvSmsFeedback);
        timerProgressBar = findViewById(R.id.timerProgressBar);

        Button btnOptionA = findViewById(R.id.btnOptionA);
        Button btnOptionB = findViewById(R.id.btnOptionB);
        Button btnOptionC = findViewById(R.id.btnOptionC);
        Button btnOptionD = findViewById(R.id.btnOptionD);

        tvPlayerInfo.setText(getString(R.string.player_info_q3, playerName != null ? playerName : ""));
        tvScore.setText(getString(R.string.score_fmt, currentScore));
        tvQuestion.setText("Where is the safest outdoor meeting place for your family after an earthquake?");

        btnOptionA.setText("A) Under power lines");
        btnOptionB.setText("B) Open field away from buildings");
        btnOptionC.setText("C) Under a highway overpass");
        btnOptionD.setText("D) Next to tall glass windows");

        btnOptionA.setVisibility(View.VISIBLE);
        btnOptionB.setVisibility(View.VISIBLE);
        btnOptionC.setVisibility(View.VISIBLE);
        btnOptionD.setVisibility(View.VISIBLE);

        startQuestionTimer();

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
                    Toast.makeText(Question3Activity.this, R.string.time_expired_q3, Toast.LENGTH_SHORT).show();
                    navigateToNextQuestion();
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
            tvSmsFeedback.setText(FEEDBACK_CORRECT);
            Toast.makeText(this, R.string.correct_answer, Toast.LENGTH_SHORT).show();
        } else {
            tvSmsFeedback.setText(FEEDBACK_INCORRECT);
            Toast.makeText(this, R.string.incorrect_answer, Toast.LENGTH_SHORT).show();
        }
        tvSmsFeedback.postDelayed(this::navigateToNextQuestion, 5000);
    }

    private void navigateToNextQuestion() {
        Intent intent = new Intent(Question3Activity.this, Question4Activity.class);
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
