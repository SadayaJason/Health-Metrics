package com.example.projectaltar;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class bmi_calculator extends AppCompatActivity {

    Dialog mDialog;

    private EditText weightEditText, heightEditText, nameEditText;
    private Button computeButton;
    private TextView bmiResultTextView, bmiCategoryTextView, bmiUpdate, categoryUpdate;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);

        mDialog = new Dialog(this);

        int delay = getIntent().getIntExtra("popup_delay", 0);
        if (delay > 0) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDialog.setContentView(R.layout.privacy_popup);
                    mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    mDialog.show();
                }
            }, delay);
        }

        weightEditText = findViewById(R.id.weightValue);
        heightEditText = findViewById(R.id.heightValue);
        nameEditText = findViewById(R.id.nameValue);
        computeButton = findViewById(R.id.calculateButton);
        bmiResultTextView = findViewById(R.id.bmiResult);
        bmiCategoryTextView = findViewById(R.id.bmiCategory);
        bmiUpdate = findViewById(R.id.bmiUpdate);
        categoryUpdate = findViewById(R.id.categoryUpdate);

        computeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBmi();
            }
        });

        LottieAnimationView lottieButton = findViewById(R.id.outputsHistory);
        lottieButton.setAnimation(R.raw.show);
        lottieButton.setRepeatCount(0);

        lottieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBmiHistoryActivity();
            }
        });
    }

    private void calculateBmi() {
        String weightString = weightEditText.getText().toString();
        String heightString = heightEditText.getText().toString();
        String bmiCategory;
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

        if (!isValid) {
            return;
        }

        float weight = Float.parseFloat(weightString);
        float height = Float.parseFloat(heightString);

        double bmi = weight / Math.pow(height, 2);//Computation

        //COndition(s) for the BMI Category.
        if (bmi <= 18.4) {
            bmiCategory = "Your Body Mass index aligns and categorized as Underweight";
        } else if (bmi <= 25.0) {
            bmiCategory = "Your Body Mass index aligns and categorized as Normal";
        } else if (bmi <= 39.9) {
            bmiCategory = "Your Body Mass index aligns and categorized as Overweight";
        }else {
            bmiCategory = "Your Body Mass index aligns and categorized as Obese";
        }

        Calendar calendar = Calendar.getInstance();
        String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        String currentTime = timeFormat.format(calendar.getTime());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        String formattedBmi = String.format(Locale.getDefault(), "%.1f", bmi);
        String bmiResultText = "<big><b>Your BMI is: " + formattedBmi + "%</b></big>";
        String bmiDateTimeText = "<small><small>Calculated on " + currentDate + " at " + currentTime + "</small></small>";

        bmiResultTextView.setText(Html.fromHtml(bmiResultText));
        bmiResultTextView.append("\n");
        bmiResultTextView.append(Html.fromHtml(bmiDateTimeText));
        bmiResultTextView.setTextSize(18); // Set the BFP result text size to 24sp
        bmiCategoryTextView.setText(bmiCategory);

        // Save current BFP value with date, time, and day
        SharedPreferences sharedPrefs = getSharedPreferences("BMI_ENTRIES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        int entryCount = sharedPrefs.getInt("entryCount", 0) + 1;

        // Retrieve previous BFP value if available
        double previousBMI = 0.0;
        if (entryCount > 1) {
            String previousBMIString = sharedPrefs.getString("bmiValue" + (entryCount - 1), "");
            if (!previousBMIString.isEmpty()) {
                previousBMI = Double.parseDouble(previousBMIString);
            }
        }

        editor.putString("bmiValue" + entryCount, String.valueOf(bmi));
        editor.putString("bmiName" + entryCount, nameString);
        editor.putString("bmiDate" + entryCount, date);
        editor.putString("bmiTime" + entryCount, time);
        editor.putString("bmiWeight" + entryCount, weightString);
        editor.putString("bmiHeight" + entryCount, heightString);
        editor.putString("bmiCategory" + entryCount, bmiCategory);

        editor.putInt("entryCount", entryCount);
        editor.apply();


        /*// Compare the previous BMI with the latest BMI
        if (previousBmi > 0) {
            String formattedPreviousBmi = String.format(Locale.getDefault(), "%.1f", previousBmi);
            if (bmi <= previousBmi) {
                bmiUpdate.setText("Your BMI has improved! From " + formattedPreviousBmi + " to " + formattedBmi);
            } else if (bmi > previousBmi) {
                bmiUpdate.setText("Your BMI has increased! From " + formattedPreviousBmi + " to " + formattedBmi);
            } else {
                bmiUpdate.setText("Your BMI remains the same.");
            }
        }
        // Compare the previous BMI category with the latest BMI category
        // Compare the previous BMI category with the latest BMI category
        if (!previousBmiCategory.isEmpty()) {
            if (!bmiCategory.equals(previousBmiCategory)) {
                categoryUpdate.setText("While your category has changed\nfrom " + previousBmiCategory + " to " + bmiCategory);
            } else {
                categoryUpdate.setText("Your BMI category remains the same.");
            }
        }*/

    }

    public void openBmiHistoryActivity() {
        Intent intent = new Intent(this, BmiHistoryActivity.class);
        startActivity(intent);
    }
}
