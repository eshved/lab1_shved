package com.example.lab1_shved;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GameActivity extends AppCompatActivity {

    private TextView gameTimer;
    private TextView guessedWords;
    private TextView scrambledWord;
    private GridLayout letterGrid;
    private CountDownTimer timer;
    private int timeRemaining;
    private int guessedWordCount;
    private String currentWord;
    private List<String> wordList;
    private Map<Button, Integer> usedLetters = new HashMap<>();
    private StringBuilder currentText = new StringBuilder();
    private List<String> wordListCopy;
    private Button pauseButton;

    private int minWordLength;
    private int maxWordLength;
    private int selectedTimeInMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        gameTimer = findViewById(R.id.gameTimer);
        guessedWords = findViewById(R.id.guessedWords);
        scrambledWord = findViewById(R.id.scrambledWord);
        letterGrid = findViewById(R.id.letterGrid);

        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переключення на головне меню або іншу активність, на ваш вибір
                // Тут ви можете вказати, куди користувач буде перенаправлений після завершення гри.
                // приклад: переключення на головне меню (MainMenuActivity)
                Intent mainMenuIntent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(mainMenuIntent);
                finish(); // закрити поточну активність
            }
        });

        Button backspaceButton = findViewById(R.id.backspaceButton);
        backspaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBackspaceClick();
            }
        });

        pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePauseButtonClick();
            }
        });

        // Отримайте передані значення з Intent
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);

        minWordLength = Integer.parseInt(sharedPreferences.getString("min_length", "4"));
        maxWordLength = Integer.parseInt(sharedPreferences.getString("max_length", "7"));
        selectedTimeInMinutes = Integer.parseInt(sharedPreferences.getString("selected_time", "1"));

        // ініціалізація гри з готовими значеннями
        initializeGame();
    }

    private void initializeGame() {
        timeRemaining = selectedTimeInMinutes * 60;
        guessedWordCount = 0;

        // створення списку слів для гри
        List<String> allWords = Arrays.asList(
                "радіо", "музика", "графіка", "грати", "екран",
                "слово", "android", "java",
                "програма", "клавіатура", "мобільний", "планшет",
                "комп'ютер", "відео", "картинка",
                "іграшка", "процесор", "екран", "телефон"
        );

        // Створення нового списку для введених слів
        List<String> selectedWords = new ArrayList<>();

        // обирає слова відповідно до заданих значень
        for (String word : allWords) {
            int wordLength = word.length();
            if (wordLength >= minWordLength && wordLength <= maxWordLength) {
                selectedWords.add(word);
            }
        }

        // перемішування слів
        Collections.shuffle(selectedWords);

        // Перевірте, щоб у списку було не менше 5 слів
        // Виберіть слова із заданого діапазону довжин
        for (String word : allWords) {
            int wordLength = word.length();
            if (wordLength >= minWordLength && wordLength <= maxWordLength) {
                selectedWords.add(word);
            }
        }

        // якщо у списку менше ніж 5 слів додаються слова з allWords
        if (selectedWords.size() < 5) {
            selectedWords.addAll(allWords.subList(0, 5 - selectedWords.size()));
        }

        Collections.shuffle(selectedWords);

        // налаштування гри з заданими словами
        wordList = selectedWords;

        // ініціалізація wordListCopy і перемішування
        wordListCopy = new ArrayList<>(wordList);
        Collections.shuffle(wordListCopy);

        // Оновлюємо UI з урахуванням вибраного часу
        updateUI();

        startTimer();
    }

    private void startTimer() {
        timer = new CountDownTimer(timeRemaining * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeRemaining = (int) millisUntilFinished / 1000;
                updateUI();
            }

            public void onFinish() {
                timeRemaining = 0;
                updateUI();
            }
        }.start();
    }

    private boolean isGameEnded = false;
    private void updateUI() {
        if (isGameEnded) {
            return; // якщо гра завершилась, UI не оновлюється
        }

        gameTimer.setText("Час: " + timeRemaining + " сек");
        guessedWords.setText("Кількість вгаданих слів: " + guessedWordCount);

        if (timeRemaining > 0) {
            if (currentWord == null) {
                currentWord = getRandomWord(minWordLength, maxWordLength);
                shuffleLetters(currentWord);
            }
        } else if (guessedWordCount < 5 || guessedWordCount == 0) { // додано перевірку на guessedWordCount
            isGameEnded = true; // Встановлюємо прапор, що гра завершилась
            // Intent для переходу на GameOverActivity
            Intent gameOverIntent = new Intent(this, GameOverActivity.class);
            gameOverIntent.putExtra("guessedWordCount", guessedWordCount);
            startActivity(gameOverIntent);
            finish();
        } else if (guessedWordCount == 5) {
            isGameEnded = true;
            // Intent для переходу на WinGameActivity
            Intent winIntent = new Intent(this, WinGameActivity.class);
            startActivity(winIntent);
            finish();
        }
    }

    private Set<String> usedWords = new HashSet<>();
    private String getRandomWord(int minLength, int maxLength) {
        if (wordListCopy.isEmpty() || usedWords.size() >= wordListCopy.size()) {
            // Якщо список порожній або всі слова вже використані, оновіть wordListCopy та перетягніть його
            wordListCopy = new ArrayList<>(wordList);
            Collections.shuffle(wordListCopy);
            usedWords.clear();
        }

        String randomWord;
        do {
            // Виберіть випадкове слово з wordListCopy
            int randomIndex = new Random().nextInt(wordListCopy.size());
            randomWord = wordListCopy.get(randomIndex);
        } while (usedWords.contains(randomWord));

        // Позначте вибране слово як використане
        usedWords.add(randomWord);

        return randomWord;
    }


    private void shuffleLetters(String word) {
        char[] letters = word.toCharArray();
        List<Character> letterList = new ArrayList<>(letters.length);
        for (char letter : letters) {
            letterList.add(letter);
        }
        Collections.shuffle(letterList);

        letterGrid.removeAllViews();

        for (int i = 0; i < letterList.size(); i++) {
            Button letterButton = (Button) getLayoutInflater().inflate(R.layout.letter_button, null);
            letterButton.setText(letterList.get(i).toString());
            letterButton.setBackgroundResource(R.drawable.letter_frame);

            if (usedLetters.containsValue(i)) {
                letterButton.setTag("used");
            } else {
                letterButton.setTag("available");
            }

            letterGrid.addView(letterButton);

            final int finalI = i;
            letterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleLetterClick(letterButton, letterList.get(finalI));
                }
            });

            letterButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    handleBackspaceClick();
                    return true;
                }
            });
        }
    }

    private List<Integer> usedLetterIndices = new ArrayList<>();

    private void handleLetterClick(Button letterButton, char letter) {
        if (currentWord != null) {
            if ("available".equals(letterButton.getTag())) {
                currentText.append(letter);
                usedLetterIndices.add(letterGrid.indexOfChild(letterButton));
                scrambledWord.setText(currentText.toString());
                letterButton.setVisibility(View.INVISIBLE);
                letterButton.setTag("used");
            }
            checkWordMatch();
        }
    }

    private void handleBackspaceClick() {
        if (currentWord != null && currentText.length() > 0 && usedLetterIndices.size() > 0) {
            int lastIndex = usedLetterIndices.get(usedLetterIndices.size() - 1);
            char lastLetter = currentText.charAt(currentText.length() - 1);

            Button letterButton = (Button) letterGrid.getChildAt(lastIndex);
            letterButton.setVisibility(View.VISIBLE);
            letterButton.setTag("available");

            currentText.deleteCharAt(currentText.length() - 1);
            scrambledWord.setText(currentText.toString());
            usedLetterIndices.remove(usedLetterIndices.size() - 1);
            checkWordMatch();
        }
    }

    private void checkWordMatch() {
        if (currentWord != null && currentText.toString().equals(currentWord)) {
            guessedWordCount++;
            currentWord = null;
            scrambledWord.setText("");
            currentText.setLength(0);
            usedLetterIndices.clear();
            updateUI();

            if (!wordListCopy.isEmpty()) {
                wordListCopy.remove(0);
            }

            if (wordListCopy.isEmpty()) {
                wordListCopy = new ArrayList<>(wordList);
                Collections.shuffle(wordListCopy);
            }

            if (guessedWordCount == 5) {
                Intent winIntent = new Intent(GameActivity.this, WinGameActivity.class);
                startActivity(winIntent);
                finish();
            }

            showCustomToast();
        }
    }

    private void endGame() {
        timer.cancel();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (guessedWordCount == 5) {
                    Intent winIntent = new Intent(GameActivity.this, WinGameActivity.class);
                    startActivity(winIntent);
                    finish();
                } else {
                    Intent gameOverIntent = new Intent(GameActivity.this, GameOverActivity.class);
                    startActivity(gameOverIntent);
                    finish();
                }
            }
        }, 2000);
    }

    private void showCustomToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));

        Toast customToast = new Toast(getApplicationContext());
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.setView(layout);

        customToast.show();
    }
    private boolean isTimerPaused = false;
    private void handlePauseButtonClick() {
        if (timer != null) {
            if (isTimerPaused) {
                // Якщо таймер припинено, відновіть його
                startTimer();
                isTimerPaused = false;
                pauseButton.setText("Пауза");
            } else {
                // Якщо таймер працює, призупиніть його
                timer.cancel();
                isTimerPaused = true;
                pauseButton.setText("Продовжити");
            }
        }
    }
}