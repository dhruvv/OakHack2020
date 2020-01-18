package com.example.oakhack2020final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button emergencyViewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emergencyViewButton = findViewById(R.id.EmergencyViewButton);
        emergencyViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }

        });
    }
}
