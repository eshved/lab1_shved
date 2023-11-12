package com.example.lab1_shved;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_toast_game_over); // Встановлення макету для цієї активності

        int guessedWordCount = getIntent().getIntExtra("guessedWordCount", 0);
        TextView guessedWordsTextView = findViewById(R.id.guessedWordsTextView);
        guessedWordsTextView.setText("Кількість вгаданих слів: " + guessedWordCount);

        // Настройка кнопки "Грати знову"
        Button replayButton = findViewById(R.id.replayButton);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переключення на активність GameActivity для початку нової гри
                Intent gameIntent = new Intent(GameOverActivity.this, GameActivity.class);
                startActivity(gameIntent);
                finish();
            }
        });

        // Настройка кнопки "Вийти в головне меню"
        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переключення на головне меню або іншу активність, на ваш вибір
                Intent mainMenuIntent = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(mainMenuIntent);
                finish();
            }
        });
    }
}
