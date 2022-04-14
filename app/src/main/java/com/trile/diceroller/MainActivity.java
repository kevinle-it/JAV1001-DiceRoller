package com.trile.diceroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textRollResult;
    Spinner spinnerDie;

    Button btnRollOnce;
    Button btnRollTwice;

    EditText inputNumOfSides;
    Button btnAddNewDie;

    TextView textHistory;

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
    }
}