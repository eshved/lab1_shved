package com.example.lab1_shved;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button startGameButton;
    private Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGameButton = findViewById(R.id.startGameButton);
        settingsButton = findViewById(R.id.settingsButton);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(settingsIntent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Обробка кліку на кнопці "Налаштування"
                Intent settingsIntent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(settingsIntent);
            }
        });
    }
}