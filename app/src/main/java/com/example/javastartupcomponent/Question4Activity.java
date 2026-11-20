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

public class Question4Activity extends AppCompatActivity {
    private TextView tvSmsFeedback;
    private ProgressBar timerProgressBar;
    private String playerName;
    private String playerPhone;
    private int currentScore;
    private CountDownTimer countDownTimer;
    private boolean isAnswered = false;

    private static final String CORRECT_ANSWER = "B";
    private static final String FEEDBACK_CORRECT = "Correct! Drop to your knees, Cover under a sturdy desk/table, and Hold On until shaking stops.";
    private static final String FEEDBACK_INCORRECT = "Incorrect! Correct answer was B) Drop, Cover, and Hold On.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q4);

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

        tvPlayerInfo.setText(getString(R.string.player_info_q4, playerName != null ? playerName : ""));
        tvScore.setText(getString(R.string.score_fmt, currentScore));
        tvQuestion.setText("What is the officially recommended immediate action when earthquake shaking begins indoors?");

        btnOptionA.setText("A) Run outside");
        btnOptionB.setText("B) Drop, Cover, and Hold On");
        btnOptionC.setText("C) Stand in doorway");
        btnOptionD.setText("D) Use elevator");

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
                    Toast.makeText(Question4Activity.this, R.string.time_expired_q4, Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(Question4Activity.this, Question5Activity.class);
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
