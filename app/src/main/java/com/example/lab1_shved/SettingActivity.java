package com.example.lab1_shved;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab1_shved.Service.SettingService;


public class SettingActivity extends AppCompatActivity {
    private boolean isServiceBound = false;
    public SettingService settingService;
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
        // Загрузить настройки при запуске активити

        Intent serviceIntent = new Intent(this, SettingService.class);
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedMinLength = getSelectedMinLength();
                String selectedMaxLength = getSelectedMaxLength();
                String selectedTime = getSelectedTime();

                settingService.saveSettings(selectedMinLength, selectedMaxLength, selectedTime); // Сохранить настройки

                Toast.makeText(SettingActivity.this, "Налаштування збережені", Toast.LENGTH_SHORT).show();

                Intent gameIntent = new Intent(SettingActivity.this, GameActivity.class);
                gameIntent.putExtra("min_length", selectedMinLength);
                gameIntent.putExtra("max_length", selectedMaxLength);
                gameIntent.putExtra("selected_time", selectedTime);
                startActivity(gameIntent);
            }
        });
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SettingService.SettingsBinder binder = (SettingService.SettingsBinder) iBinder;
            settingService = binder.getService();
            isServiceBound = true;
            settingService.loadSettings();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceBound = false;
        }
    };

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
            // Извлеките только числовое значение, удалив текстовую метку
            String numericValue = selectedText.replaceAll("[^0-9]+", "");
            return numericValue;
        } else {
            return "";
        }
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