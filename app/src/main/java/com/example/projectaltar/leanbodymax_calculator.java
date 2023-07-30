package com.example.projectaltar;

import androidx.appcompat.app.AppCompatActivity;

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

import com.airbnb.lottie.LottieAnimationView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class leanbodymax_calculator extends AppCompatActivity {
    Dialog mDialog;

    private EditText weightEditText, heightEditText, nameEditText;
    private RadioGroup radioGroup;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;
    private Button computeButton;
    private TextView lbmResultTextView, lbmUpdate;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leanbodymax_calculator);

        mDialog = new Dialog(this);

        int delay = getIntent().getIntExtra("popup_delay", 0);
        if (delay > 0) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDialog.setContentView(R.layout.lbm_popup);
                    mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    mDialog.show();
                }
            }, delay);
        }

        weightEditText = findViewById(R.id.weightValue);
        heightEditText = findViewById(R.id.heightValue);
        nameEditText = findViewById(R.id.nameValue);
        radioGroup = findViewById(R.id.radioGroup);
        radioButtonMale = findViewById(R.id.radioMale);
        radioButtonFemale = findViewById(R.id.radioFemale);
        computeButton = findViewById(R.id.calculateButton);
        lbmResultTextView = findViewById(R.id.lbmResult);
        lbmUpdate = findViewById(R.id.lbmUpdate);

        computeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateLBM();
            }
        });

        LottieAnimationView lottieButton = findViewById(R.id.outputsHistory);
        lottieButton.setAnimation(R.raw.show);
        lottieButton.setRepeatCount(0);

        lottieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLbmHistoryActivity();
            }
        });
    }

    private void calculateLBM() {
        String weightString = weightEditText.getText().toString();
        String heightString = heightEditText.getText().toString();
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

        if (heightString.isEmpty()) {
            heightEditText.setError("Height is required.");
            isValid = false;
        } else {
            heightEditText.setError(null);
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

        double weight = Double.parseDouble(weightString);
        double height = Double.parseDouble(heightString);
        boolean isMale = radioButtonMale.isChecked();

        double lbm;

        if (isMale) {
            lbm = 0.407 * weight + 0.267 * height - 19.2;
        } else {
            lbm = 0.252 * weight + 0.473 * height - 48.3;
        }

        // Get the current time, day, and date
        Calendar calendar = Calendar.getInstance();
        String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        String currentTime = timeFormat.format(calendar.getTime());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        String formattedLbm = String.format(Locale.getDefault(), "%.1f", lbm);
        String lbmResultText = "<big><b>Your LBM is: " + formattedLbm + " kcal</b></big>";
        String lbmDateTimeText = "<small><small>Calculated on " + currentDate + " at " + currentTime + "</small></small>";

        lbmResultTextView.setText(Html.fromHtml(lbmResultText));
        lbmResultTextView.append("\n");
        lbmResultTextView.append(Html.fromHtml(lbmDateTimeText));
        lbmResultTextView.setTextSize(18); // Set the LBM result text size to 24sp

        // Save current LBM value with date, time, and day
        SharedPreferences sharedPrefs = getSharedPreferences("LBM_ENTRIES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        int entryCount = sharedPrefs.getInt("entryCount", 0) + 1;

        // Retrieve previous LBM value if available
        double previousLBM = 0.0;
        if (entryCount > 1) {
            String previousLBMString = sharedPrefs.getString("lbmValue" + (entryCount - 1), "");
            if (!previousLBMString.isEmpty()) {
                previousLBM = Double.parseDouble(previousLBMString);
            }
        }

        editor.putString("lbmValue" + entryCount, String.valueOf(lbm));
        editor.putString("lbmName" + entryCount, nameString);
        editor.putString("lbmDate" + entryCount, date);
        editor.putString("lbmTime" + entryCount, time);
        editor.putString("lbmWeight" + entryCount, weightString);

        isMale = radioButtonMale.isChecked();
        String gender = isMale ? "Male" : "Female";
        editor.putString("lbmGender" + entryCount, gender);

        editor.putInt("entryCount", entryCount);
        editor.apply();

        /*// Compare with previous LBM value
        if (previousLBM != 0.0) {
            previousLBM = Double.parseDouble(String.valueOf(previousLBM));
            double lbmDifference = lbm - previousLBM;
            String updateMessage;
            if (lbmDifference > 0) {
                updateMessage = "Your LBM has increased by " + String.format(Locale.getDefault(), "%.2f", Math.abs(lbmDifference)) + " kg. from " + String.format(Locale.getDefault(), "%.2f", Math.abs(previousLBM)) + " kg. to " + formattedLbm + " kg.";
            } else if (lbmDifference < 0) {
                updateMessage = "Your LBM has decreased by " + String.format(Locale.getDefault(), "%.2f", Math.abs(lbmDifference)) + " kg. from " + String.format(Locale.getDefault(), "%.2f", Math.abs(previousLBM)) + " kg. to " + formattedLbm + " kg.";
            } else {
                updateMessage = "Your LBM remains the same";
            }
            lbmUpdate.setText(updateMessage);
            lbmUpdate.setVisibility(View.VISIBLE);
        }*/
    }

    public void openLbmHistoryActivity() {
        Intent intent = new Intent(this, LbmHistoryActivity.class);
        startActivity(intent);
    }
}