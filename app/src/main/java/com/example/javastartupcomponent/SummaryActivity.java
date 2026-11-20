package com.example.javastartupcomponent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SummaryActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 101;

    private String playerName;
    private String playerPhone;
    private int finalScore;

    private TextView tvSmsStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // 1. Retrieve data passed from final question
        Intent intent = getIntent();
        playerName = intent.getStringExtra("PLAYER_NAME");
        playerPhone = intent.getStringExtra("PLAYER_PHONE");
        finalScore = intent.getIntExtra("CURRENT_SCORE", 0);

        // 2. Initialize Views
        TextView tvPlayerNameSummary = findViewById(R.id.tvPlayerNameSummary);
        TextView tvFinalScore = findViewById(R.id.tvFinalScore);
        tvSmsStatus = findViewById(R.id.tvSmsStatus);

        Button btnSendSms = findViewById(R.id.btnSendSms);
        Button btnMainMenu = findViewById(R.id.btnMainMenu);

        // 3. Populate Summary Details
        tvPlayerNameSummary.setText("Player: " + (playerName != null ? playerName : "Anonymous"));
        tvFinalScore.setText("Final Score: " + finalScore + " Points");

        if (playerPhone != null && !playerPhone.trim().isEmpty()) {
            tvSmsStatus.setText("SMS Target: " + playerPhone);
        } else {
            tvSmsStatus.setText("No phone number provided.");
        }

        // 4. Click Listeners
        btnSendSms.setOnClickListener(v -> checkAndSendSms());

        btnMainMenu.setOnClickListener(v -> {
            Intent mainIntent = new Intent(SummaryActivity.this, mainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
            finish();
        });
    }

    private void checkAndSendSms() {
        if (playerPhone == null || playerPhone.trim().isEmpty()) {
            Toast.makeText(this, "Cannot send SMS: No phone number entered.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_CODE
            );
        } else {
            sendSms();
        }
    }

    private void sendSms() {
        try {
            SmsManager smsManager;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                smsManager = getSystemService(SmsManager.class);
            } else {
                smsManager = SmsManager.getDefault();
            }
            String message = "Earthquake Quiz Result for " + playerName + ": Final Score = " + finalScore + " Points.";
            smsManager.sendTextMessage(playerPhone, null, message, null, null);

            tvSmsStatus.setText("SMS Sent successfully to " + playerPhone);
            Toast.makeText(this, "SMS Sent to " + playerPhone + "!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            tvSmsStatus.setText("Failed to send SMS: " + e.getMessage());
            Toast.makeText(this, "SMS Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSms();
            } else {
                tvSmsStatus.setText("SMS permission denied by user.");
                Toast.makeText(this, "SMS Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}