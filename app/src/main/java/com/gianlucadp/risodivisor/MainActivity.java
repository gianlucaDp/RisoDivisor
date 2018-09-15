package com.gianlucadp.risodivisor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCE = "RisoDivisor";
    private static final String SP_CONTAINER_WEIGHT = "container_weight";
    private static final int SERVINGS = 2;

    private TextView mResultTextValue;
    private TextView mResultText;
    private EditText mContainerWeightText;
    private EditText mTotalWeightText;
    private FloatingActionButton mSaveButton;
    private FloatingActionButton mCalculateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linkUIElements();

        SharedPreferences settings = getSharedPreferences(SHARED_PREFERENCE, 0);
        int storedValue = settings.getInt(SP_CONTAINER_WEIGHT, 0);
        if (storedValue > 0)
            mContainerWeightText.setText(String.valueOf(storedValue));
            mTotalWeightText.requestFocus();

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeContainerWeight();
            }
        });

        mCalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String containerWeight = mContainerWeightText.getText().toString();
                String totalWeight = mTotalWeightText.getText().toString();

                if (!TextUtils.isEmpty(containerWeight) && !TextUtils.isEmpty(totalWeight) ) {
                    int containerValue = Integer.valueOf(containerWeight);
                    int totalValue = Integer.valueOf(totalWeight);
                    if (totalValue>containerValue) {
                        int result = (totalValue - containerValue) / SERVINGS + containerValue;
                        showResult(result);
                    }else{
                        Toast.makeText(MainActivity.this,R.string.weight_hint, Toast.LENGTH_LONG).show();
                        hideResult();
                    }
                }else{
                    Toast.makeText(MainActivity.this,R.string.fill_hint, Toast.LENGTH_LONG).show();
                    hideResult();
                }
            }
        });


    }

    private void hideResult() {
        mResultText.setVisibility(View.INVISIBLE);
        mResultTextValue.setVisibility(View.INVISIBLE);
    }

    private void showResult(int result) {
        mResultTextValue.setText(String.valueOf(result));
        mResultText.setVisibility(View.VISIBLE);
        mResultTextValue.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm!=null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            mContainerWeightText.clearFocus();
            mTotalWeightText.clearFocus();
        }

    }

    private void storeContainerWeight() {
        SharedPreferences settings = getSharedPreferences(SHARED_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        String containerWeight = mContainerWeightText.getText().toString();
        if (!TextUtils.isEmpty(containerWeight)) {
            int value = Integer.valueOf(containerWeight);
            editor.putInt(SP_CONTAINER_WEIGHT, value);
            editor.apply();
        }

    }

    private void linkUIElements() {
        mResultTextValue = findViewById(R.id.tv_text_value);
        mResultText = findViewById(R.id.tv_text_result);
        mContainerWeightText = findViewById(R.id.et_container);
        mTotalWeightText = findViewById(R.id.et_total);
        mSaveButton = findViewById(R.id.fab_save);
        mCalculateButton = findViewById(R.id.fab_calculate);
    }
}
