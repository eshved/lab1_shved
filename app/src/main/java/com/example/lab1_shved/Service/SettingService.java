package com.example.lab1_shved.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

public class SettingService extends Service {
    private final IBinder binder = new SettingsBinder();

    public class SettingsBinder extends Binder {
        public SettingService getService() {
            return SettingService.this;
        }
    }

    private RadioGroup minWordLengthRadioGroup;
    private RadioGroup maxWordLengthRadioGroup;
    private RadioGroup timeRadioGroup;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void saveSettings(String minWordLength, String maxWordLength, String selectedTime) {
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("min_length", minWordLength);
        editor.putString("max_length", maxWordLength);
        editor.putString("selected_time", selectedTime);

        // Проверка на пустую строку перед сохранением
        if (!selectedTime.isEmpty()) {
            editor.putInt("minWordLengthRadioGroup", minWordLengthRadioGroup.getCheckedRadioButtonId());
            editor.putInt("maxWordLengthRadioGroup", maxWordLengthRadioGroup.getCheckedRadioButtonId());
            editor.putInt("timeRadioGroup", timeRadioGroup.getCheckedRadioButtonId());
        }

        editor.apply();
    }

    public void loadSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);

        String minWordLength = sharedPreferences.getString("min_length", "4");
        String maxWordLength = sharedPreferences.getString("max_length", "7");
        String selectedTimeStr = sharedPreferences.getString("selected_time", "1");
        int selectedTime;
        if (selectedTimeStr.isEmpty()) {
            selectedTime = 1; // Значение по умолчанию, если строка пуста
        } else {
            selectedTime = Integer.parseInt(selectedTimeStr);
        }

        // Инициализация RadioGroup объектов
        minWordLengthRadioGroup = new RadioGroup(this);  // Замените 'this' на ваш контекст, если необходимо
        maxWordLengthRadioGroup = new RadioGroup(this);  // Замените 'this' на ваш контекст, если необходимо
        timeRadioGroup = new RadioGroup(this);           // Замените 'this' на ваш контекст, если необходимо

        setRadioGroupSelection(minWordLengthRadioGroup, minWordLength);
        setRadioGroupSelection(maxWordLengthRadioGroup, maxWordLength);
        setRadioGroupSelection(timeRadioGroup, String.valueOf(selectedTime));// Convert the integer to a string for the radio group

        // Set the value of time in the UI
        for (int i = 0; i < timeRadioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) timeRadioGroup.getChildAt(i);
            if (radioButton.getText().toString().contains(String.valueOf(selectedTime))) {
                timeRadioGroup.check(radioButton.getId());
            }
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