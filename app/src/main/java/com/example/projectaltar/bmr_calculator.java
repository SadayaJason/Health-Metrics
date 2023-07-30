package com.example.projectaltar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class bmr_calculator extends AppCompatActivity {
    Dialog mDialog;

    private EditText weightEditText, ageEditText, nameEditText;
    private RadioGroup radioGroup;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;
    private Button computeButton;
    private TextView bmrResultTextView, bmrUpdate;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmr_calculator);

        mDialog = new Dialog(this);

        int delay = getIntent().getIntExtra("popup_delay", 0);
        if (delay > 0) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDialog.setContentView(R.layout.bmr_popup);
                    mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    mDialog.show();
                }
            }, delay);
        }

        weightEditText = findViewById(R.id.weightValue);
        ageEditText = findViewById(R.id.ageValue);
        nameEditText = findViewById(R.id.nameValue);
        radioGroup = findViewById(R.id.radioGroup);
        radioButtonMale = findViewById(R.id.radioMale);
        radioButtonFemale = findViewById(R.id.radioFemale);
        computeButton = findViewById(R.id.calculateButton);
        bmrResultTextView = findViewById(R.id.bmrResult);
        bmrUpdate = findViewById(R.id.bmrUpdate);

        computeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMR();
            }
        });

        LottieAnimationView lottieButton = findViewById(R.id.outputsHistory);
        lottieButton.setAnimation(R.raw.show);
        lottieButton.setRepeatCount(0);

        lottieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBmrHistoryActivity();
            }
        });
    }

    private void calculateBMR() {
        String weightString = weightEditText.getText().toString();
        String ageString = ageEditText.getText().toString();
        String nameString = nameEditText.getText().toString();

        boolean isValid = true;

        if (nameString.isEmpty()) {
            nameEditText.setError("Name is required.");
            isValid = false;
        } else {
            nameEditText.setError(null);
        }

        if (weightString.isEmpty()) {
            weightEditText.setError("Weight is required.");
            isValid = false;
        } else {
            weightEditText.setError(null);
        }

        if (ageString.isEmpty()) {
            ageEditText.setError("Age is required.");
            isValid = false;
        } else {
            ageEditText.setError(null);
        }

        if (!radioButtonMale.isChecked() && !radioButtonFemale.isChecked()) {
            radioButtonMale.setError("Please select a gender.");
            radioButtonFemale.setError("Please select a gender.");
            isValid = false;
        } else {
            radioButtonMale.setError(null);
            radioButtonFemale.setError(null);
        }

        if (!isValid) {
            return;
        }

        int weight = Integer.parseInt(weightString);
        int age = Integer.parseInt(ageString);
        boolean isMale = radioButtonMale.isChecked();


        double bmr;

        if (isMale) {
            if (age >= 0 && age <= 3) {
                bmr = 61.0 * weight - 33.7;
            } else if (age >= 3 && age <= 10) {
                bmr = 23.3 * weight + 514;
            } else if (age >= 10 && age <= 18) {
                bmr = 18.4 * weight + 581;
            } else if (age >= 18 && age <= 30) {
                bmr = 16.0 * weight + 545;
            } else if (age >= 30 && age <= 60) {
                bmr = 14.2 * weight + 593;
            } else {
                bmr = 13.5 * weight + 514;
            }
        } else {
            if (age >= 0 && age <= 3) {
                bmr = 59.7 * weight - 34.8;
            } else if (age >= 3 && age <= 10) {
                bmr = 20.3 * weight + 485;
            } else if (age >= 10 && age <= 18) {
                bmr = 12.2 * weight + 746;
            } else if (age >= 18 && age <= 30) {
                bmr = 14.7 * weight + 495;
            } else if (age >= 30 && age <= 60) {
                bmr = 8.7 * weight + 829;
            } else {
                bmr = 10.5 * weight + 596;
            }
        }

        // Get the current time, day, and date
        Calendar calendar = Calendar.getInstance();
        String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        String currentTime = timeFormat.format(calendar.getTime());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        String formattedBmr = String.format(Locale.getDefault(), "%.1f", bmr);
        String bmrResultText = "<big><b>Your BMR is: " + formattedBmr + " kcal</b></big>";
        String bmrDateTimeText = "<small><small>Calculated on " + currentDate + " at " + currentTime + "</small></small>";

        bmrResultTextView.setText(Html.fromHtml(bmrResultText));
        bmrResultTextView.append("\n");
        bmrResultTextView.append(Html.fromHtml(bmrDateTimeText));
        bmrResultTextView.setTextSize(18); // Set the BMR result text size to 24sp

        // Save current BMR value with date, time, and day
        SharedPreferences sharedPrefs = getSharedPreferences("BMR_ENTRIES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        int entryCount = sharedPrefs.getInt("entryCount", 0) + 1;

        // Retrieve previous BMR value if available
        double previousBMR = 0.0;
        if (entryCount > 1) {
            String previousBMRString = sharedPrefs.getString("bmrValue" + (entryCount - 1), "");
            if (!previousBMRString.isEmpty()) {
                previousBMR = Double.parseDouble(previousBMRString);
            }
        }

        editor.putString("bmrValue" + entryCount, String.valueOf(bmr));
        editor.putString("bmrName" + entryCount, nameString);
        editor.putString("bmrDate" + entryCount, date);
        editor.putString("bmrTime" + entryCount, time);
        editor.putString("bmrWeight" + entryCount, weightString);
        editor.putString("bmrAge" + entryCount, ageString);

        isMale = radioButtonMale.isChecked();
        String gender = isMale ? "Male" : "Female";
        editor.putString("bmrGender" + entryCount, gender);

        editor.putInt("entryCount", entryCount);
        editor.apply();

        /*// Compare with previous BMR value
        if (previousBMR != 0.0) {
            previousBMR = Double.parseDouble(String.valueOf(previousBMR));
            double bmrDifference = bmr - previousBMR;
            String updateMessage;
            if (bmrDifference > 0) {
                updateMessage = "Your BMR has increased by " + String.format(Locale.getDefault(), "%.2f", Math.abs(bmrDifference)) + " kcal. from " + previousBMR + " kcal. to " + formattedBmr + " kcal.";
            } else if (bmrDifference < 0) {
                updateMessage = "Your BMR has decreased by " + String.format(Locale.getDefault(), "%.2f", Math.abs(bmrDifference)) + " kcal. from " + previousBMR + " kcal. to " + formattedBmr + " kcal.";
            } else {
                updateMessage = "Your BMR remains the same";
            }
            bmrUpdate.setText(updateMessage);
            bmrUpdate.setVisibility(View.VISIBLE);
        }*/
    }

    public void openBmrHistoryActivity() {
        Intent intent = new Intent(this, BmrHistoryActivity.class);
        startActivity(intent);
    }
}
