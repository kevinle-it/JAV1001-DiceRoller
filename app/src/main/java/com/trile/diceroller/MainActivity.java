package com.trile.diceroller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView textRollResult;
    Spinner spinnerDie;
    List<Integer> spinnerDieOptions = new ArrayList();
    ArrayAdapter<CharSequence> spinnerDieAdapter;

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
        setupInputNumOfSides();
        setupButtonAddNewDie();
    }

    private void setupSpinner() {
        int[] dieOptions = getResources().getIntArray(R.array.die_options);

        for (int option : dieOptions) {
            spinnerDieOptions.add(option);
        }

        List<String> options = new ArrayList();
        for (int option : spinnerDieOptions) {
            options.add(String.valueOf(option));
        }

        spinnerDieAdapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                options
        );
        spinnerDie.setAdapter(spinnerDieAdapter);

        minValue = 1;
        maxValue = spinnerDieOptions.get(0);

        spinnerDie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minValue = 1;
                maxValue = spinnerDieOptions.get(i);
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

    private void setupInputNumOfSides() {
        inputNumOfSides.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("0")) {
                    // 0 sided die is not allowed
                    inputNumOfSides.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        inputNumOfSides.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                btnAddNewDie.callOnClick();
                return true;
            }
            return false;
        });
    }

    private void setupButtonAddNewDie() {
        btnAddNewDie.setOnClickListener(view -> {
            // Close keyboard
            hideSoftKeyboard((Activity) view.getContext(), true);

            String dieTypeText = inputNumOfSides.getText().toString();
            int dieTypeInt = parseInt(dieTypeText);

            if (!spinnerDieOptions.contains(dieTypeInt)) {
                // Add new item to spinner
                spinnerDieAdapter.insert(dieTypeText,spinnerDieAdapter.getCount());
                spinnerDieAdapter.notifyDataSetChanged();

                spinnerDie.setSelection(spinnerDieAdapter.getCount() - 1);

                // Add same new item to options list
                spinnerDieOptions.add(dieTypeInt);
            } else {
                int duplicatedPos = spinnerDieOptions.indexOf(dieTypeInt);
                spinnerDie.setSelection(duplicatedPos);
            }

            // Reset edit text
            inputNumOfSides.setText("");
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

    private void hideSoftKeyboard(Activity activity, boolean clearCurrentFocus) {
        Objects.requireNonNull(activity, "Cannot Hide Soft Keyboard with NULL Activity/Context!");
        // Check if no view has focused.
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            if (clearCurrentFocus) {
                view.clearFocus();
            }
        }
    }

    private int parseInt(String str) {
        try{
            return Integer.valueOf(str);
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
        return -1;
    }
}