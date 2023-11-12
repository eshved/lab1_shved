package com.example.lab1_shved;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    private RadioGroup minWordLengthRadioGroup;
    private RadioGroup maxWordLengthRadioGroup;
    private RadioGroup timeRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        minWordLengthRadioGroup = findViewById(R.id.minWordLengthRadioGroup);
        maxWordLengthRadioGroup = findViewById(R.id.maxWordLengthRadioGroup);
        timeRadioGroup = findViewById(R.id.timeRadioGroup);

        loadSettings(); // Загрузить настройки під час запуску актівіті

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedMinLength = getSelectedMinLength();
                String selectedMaxLength = getSelectedMaxLength();
                String selectedTime = getSelectedTime();

                saveSettings(selectedMinLength, selectedMaxLength, selectedTime); // Сохранить настройки

                Toast.makeText(SettingActivity.this, "Налаштування збережені", Toast.LENGTH_SHORT).show();

                Intent gameIntent = new Intent(SettingActivity.this, GameActivity.class);
                gameIntent.putExtra("min_length", selectedMinLength);
                gameIntent.putExtra("max_length", selectedMaxLength);
                gameIntent.putExtra("selected_time", selectedTime);
                startActivity(gameIntent);
            }
        });
    }

    private String getSelectedMinLength() {
        int selectedId = minWordLengthRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);

        if (selectedRadioButton != null) {
            return selectedRadioButton.getText().toString();
        } else {
            return "";
        }
    }

    private String getSelectedMaxLength() {
        int selectedId = maxWordLengthRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);

        if (selectedRadioButton != null) {
            return selectedRadioButton.getText().toString();
        } else {
            return "";
        }
    }

    private String getSelectedTime() {
        int selectedId = timeRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);

        if (selectedRadioButton != null) {
            String selectedText = selectedRadioButton.getText().toString();
            // отримуйте лише числове значення, видаляючи текстову позначку
            String numericValue = selectedText.replaceAll("[^0-9]+", "");
            return numericValue;
        } else {
            return "";
        }
    }

    private void loadSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);

        String minWordLength = sharedPreferences.getString("min_length", "4");
        String maxWordLength = sharedPreferences.getString("max_length", "7");
        String selectedTimeStr = sharedPreferences.getString("selected_time", "1");
        int selectedTime = Integer.parseInt(selectedTimeStr);

        setRadioGroupSelection(minWordLengthRadioGroup, minWordLength);
        setRadioGroupSelection(maxWordLengthRadioGroup, maxWordLength);
        setRadioGroupSelection(timeRadioGroup, String.valueOf(selectedTime)); // Convert the integer to a string for the radio group

        // Set the value of time in the UI
        for (int i = 0; i < timeRadioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) timeRadioGroup.getChildAt(i);
            if (radioButton.getText().toString().contains(String.valueOf(selectedTime))) {
                timeRadioGroup.check(radioButton.getId());
            }
        }
    }

    private void saveSettings(String minWordLength, String maxWordLength, String selectedTime) {
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("min_length", minWordLength);
        editor.putString("max_length", maxWordLength);
        editor.putString("selected_time", selectedTime);
        editor.apply();
        Log.d("MyApp", "Settings saved: min_length = " + minWordLength + ", max_length = " + maxWordLength + ", selected_time = " + selectedTime);
        //в консоль виписуються значення
    }

    private void setRadioGroupSelection(RadioGroup radioGroup, String value) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (radioButton.getText().toString().equals(value)) {
                radioGroup.check(radioButton.getId());
                break;
            }
        }
    }
}