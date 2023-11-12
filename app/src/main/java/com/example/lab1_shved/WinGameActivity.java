package com.example.lab1_shved;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WinGameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_game); // Установка макета

        // Настройка кнопки "Грати знову"
        Button replayButton = findViewById(R.id.replayButton);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переключение на активність GameActivity для початку нової гри
                Intent gameIntent = new Intent(WinGameActivity.this, GameActivity.class);
                startActivity(gameIntent);
                finish(); // закрити поточну активність
            }
        });

        // Настройка кнопки "Вийти в головне меню"
        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переключення на головне меню або іншу активність, на ваш вибір
                Intent mainMenuIntent = new Intent(WinGameActivity.this, MainActivity.class);
                startActivity(mainMenuIntent);
                finish();
            }
        });
    }
}
