package com.example.javastartupcomponent;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PastAttemptsActivity extends AppCompatActivity {

    private ListView listViewAttempts;
    private TextView tvEmptyAttempts;
    private List<Person> attemptsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_attempts);

        listViewAttempts = findViewById(R.id.listViewAttempts);
        tvEmptyAttempts = findViewById(R.id.tvEmptyAttempts);
        Button btnClearAttempts = findViewById(R.id.btnClearAttempts);
        Button btnBackToMain = findViewById(R.id.btnBackToMain);

        loadAttemptsData();

        btnClearAttempts.setOnClickListener(v -> {
            FileStorage.clearAttempts(PastAttemptsActivity.this);
            Toast.makeText(PastAttemptsActivity.this, "Past attempts cleared", Toast.LENGTH_SHORT).show();
            loadAttemptsData();
        });

        btnBackToMain.setOnClickListener(v -> finish());
    }

    private void loadAttemptsData() {
        attemptsList = FileStorage.loadAttempts(this);
        if (attemptsList == null || attemptsList.isEmpty()) {
            listViewAttempts.setVisibility(View.GONE);
            tvEmptyAttempts.setVisibility(View.VISIBLE);
        } else {
            listViewAttempts.setVisibility(View.VISIBLE);
            tvEmptyAttempts.setVisibility(View.GONE);
            ArrayAdapter<Person> adapter = new ArrayAdapter<Person>(
                    this,
                    R.layout.item_past_attempt,
                    R.id.tvItemName,
                    attemptsList
            ) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    Person person = getItem(position);
                    TextView tvName = view.findViewById(R.id.tvItemName);
                    TextView tvPhone = view.findViewById(R.id.tvItemPhone);
                    TextView tvScore = view.findViewById(R.id.tvItemScore);
                    TextView tvDate = view.findViewById(R.id.tvItemDate);

                    if (person != null) {
                        tvName.setText("Player: " + person.getName());
                        tvPhone.setText("Phone: " + person.getPhone());
                        tvScore.setText("Score: " + person.getScore() + " pts");
                        tvDate.setText(person.getDate());
                    }
                    return view;
                }
            };
            listViewAttempts.setAdapter(adapter);
        }
    }
}
