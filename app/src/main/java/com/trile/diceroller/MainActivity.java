package com.trile.diceroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textRollResult;
    Spinner spinnerDie;
    int[] spinnerDieOptions;

    int minValue = 1, maxValue = 1;

    Button btnRollOnce;
    Button btnRollTwice;

    EditText inputNumOfSides;
    Button btnAddNewDie;

    TextView textHistory;
    List<Integer> historyRollResults = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textRollResult = findViewById(R.id.textRollResult);
        spinnerDie = findViewById(R.id.spinnerDie);

        btnRollOnce = findViewById(R.id.btnRollOnce);
        btnRollTwice = findViewById(R.id.btnRollTwice);

        inputNumOfSides = findViewById(R.id.inputNumOfSides);
        btnAddNewDie = findViewById(R.id.btnAddNewDie);

        textHistory = findViewById(R.id.textHistory);

        setupSpinner();
        setupButtonsRoll();
    }

    private void setupSpinner() {
        spinnerDieOptions = getResources().getIntArray(R.array.die_options);

        List<String> options = new ArrayList();
        for (int option : spinnerDieOptions) {
            options.add(String.valueOf(option));
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                options
        );
        spinnerDie.setAdapter(adapter);

        minValue = 1;
        maxValue = spinnerDieOptions[0];

        spinnerDie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minValue = 1;
                maxValue = spinnerDieOptions[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void setupButtonsRoll() {
        btnRollOnce.setOnClickListener(view -> {
            int randomNum = getRandomNumber(minValue, maxValue);
            historyRollResults.add(randomNum);

            String rollResult = getResources()
                    .getQuantityString(R.plurals.roll_results, 1, randomNum);
            textRollResult.setText(rollResult);

            String historyResults = buildHistoryRollResults(historyRollResults, getResources());
            textHistory.setText(historyResults);
        });

        btnRollTwice.setOnClickListener(view -> {
            int randomNum1 = getRandomNumber(minValue, maxValue);
            int randomNum2 = getRandomNumber(minValue, maxValue);
            historyRollResults.add(randomNum1);
            historyRollResults.add(randomNum2);

            String rollResult = getResources()
                    .getQuantityString(R.plurals.roll_results, 2, randomNum1, randomNum2);
            textRollResult.setText(rollResult);

            String historyResults = buildHistoryRollResults(historyRollResults, getResources());
            textHistory.setText(historyResults);
        });
    }

    private String buildHistoryRollResults(List<Integer> rollResults, Resources resources) {
        String listStr = rollResults.toString();
        return resources.getString(
                R.string.history,
                listStr.substring(1, listStr.length() - 1)  // Remove start & end square brackets []
        );
    }

    private int getRandomNumber(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}