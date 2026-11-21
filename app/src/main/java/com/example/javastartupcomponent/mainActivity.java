package com.example.javastartupcomponent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class mainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        EditText name = findViewById(R.id.playerName);
        EditText phone = findViewById(R.id.playerNumber);
        Button btn_next = findViewById(R.id.buttonStart);
        Button btnPastAttempts = findViewById(R.id.buttonPastAttempts);
        Button btnExit = findViewById(R.id.buttonExit);

        btn_next.setOnClickListener(v -> {
            String playerNameStr = name.getText().toString();
            String playerPhoneStr = phone.getText().toString();

            Intent i = new Intent(
                    mainActivity.this,
                    welcomeActivity.class
            );
            i.putExtra("PLAYER_NAME", playerNameStr);
            i.putExtra("PLAYER_PHONE", playerPhoneStr);
            i.putExtra("CURRENT_SCORE", 0);
            startActivity(i);
        });

        btnPastAttempts.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity.this, PastAttemptsActivity.class);
            startActivity(intent);
        });

        btnExit.setOnClickListener(v -> finish());
    }
}