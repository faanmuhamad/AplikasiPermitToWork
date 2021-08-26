package com.example.workpermit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    private Session session;
    private LinearLayoutCompat home_btn_permit, home_btn_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home_btn_permit = findViewById(R.id.home_btn_permit);
        home_btn_history = findViewById(R.id.home_btn_history);

        home_btn_permit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToPermit();
            }
        });

        home_btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHistory();
            }
        });
    }

    private void GoToHistory() {
        Intent i = new Intent(HomeActivity.this, HistoryPermitActivity.class);
        startActivity(i);
    }

    private void GoToPermit() {
        Intent i = new Intent(HomeActivity.this, PermitActivity.class);
        startActivity(i);
    }
}