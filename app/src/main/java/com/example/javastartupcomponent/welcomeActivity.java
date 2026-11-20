package com.example.javastartupcomponent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class welcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_welcome);

        Button btn_cont = findViewById(R.id.buttonStart3);
        TextView text = findViewById(R.id.textView3);

        String name = getIntent().getStringExtra("PLAYER_NAME");
        String contact = getIntent().getStringExtra("PLAYER_PHONE");

        text.setText("Welcome " + "\n"+name + "!");
        btn_cont.setOnClickListener(v -> {
            Intent i = new Intent(
                    welcomeActivity.this,
                    Question1Activity.class
            );

            i.putExtra("PLAYER_NAME", name);
            i.putExtra("PLAYER_PHONE", contact);
            i.putExtra("CURRENT_SCORE", 0);

            startActivity(i);
        });
    }
}
