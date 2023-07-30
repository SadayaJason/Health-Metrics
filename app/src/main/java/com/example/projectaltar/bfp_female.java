package com.example.projectaltar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class bfp_female extends AppCompatActivity {
    private EditText weightEditText, heightEditText, waistEditText, neckEditText, hipEditText, nameEditText;
    private Button computeButton;
    private TextView bfpResultTextView, bfpUpdate, bfpCategoryTextView, categoryUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfp_female);

        weightEditText = findViewById(R.id.weightValue);
        heightEditText = findViewById(R.id.heightValue);
        waistEditText = findViewById(R.id.waistValue);
        neckEditText = findViewById(R.id.neckValue);
        hipEditText = findViewById(R.id.hipValue);
        nameEditText = findViewById(R.id.nameValue);
        computeButton = findViewById(R.id.calculateButton);
        bfpResultTextView = findViewById(R.id.bfpResult);
        bfpUpdate = findViewById(R.id.bfpUpdate);
        bfpCategoryTextView = findViewById(R.id.bfpCategory);
        categoryUpdate = findViewById(R.id.categoryUpdate);

        computeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBFP();
            }
        });

        LottieAnimationView lottieButton = findViewById(R.id.outputsHistory);
        lottieButton.setAnimation(R.raw.show);
        lottieButton.setRepeatCount(0);

        lottieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBfpHistoryActivity_female();
            }
        });
    }

    private void calculateBFP() {
        String weightString = weightEditText.getText().toString();
        String heightString = heightEditText.getText().toString();
        String waistString = waistEditText.getText().toString();
        String neckString = neckEditText.getText().toString();
        String hipString = hipEditText.getText().toString();
        String bfpCategory;
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

        if (waistString.isEmpty()) {
            waistEditText.setError("Waist measurement is required.");
            isValid = false;
        } else {
            waistEditText.setError(null);
        }

        if (neckString.isEmpty()) {
            neckEditText.setError("Neck measurement is required.");
            isValid = false;
        } else {
            neckEditText.setError(null);
        }

        if (hipString.isEmpty()) {
            hipEditText.setError("Hip measurement is required.");
            isValid = false;
        } else {
            hipEditText.setError(null);
        }

        if (!isValid) {
            return;
        }

        double weight = Double.parseDouble(weightString);
        double height = Double.parseDouble(heightString);
        double waist = Double.parseDouble(waistString);
        double neck = Double.parseDouble(neckString);
        double hip = Double.parseDouble(hipString);

        // Calculate Body Fat Percentage (BFP) for women
        double abdomen = waist + hip - neck;
        double bfp = 495 / (1.29579 - 0.35004 * Math.log10(abdomen) + 0.22100 * Math.log10(height)) - 450;

        // Determine the category based on Body Fat Percentage (BFP) for women
        if (bfp < 10) {
            bfpCategory = "Your Body Fat Percentage aligns and categorized as Essential Fat";
        } else if (bfp >= 10 && bfp <= 13) {
            bfpCategory = "Your Body Fat Percentage aligns and categorized as Athletes";
        } else if (bfp >= 14 && bfp <= 20) {
            bfpCategory = "Your Body Fat Percentage aligns and categorized as Fitness";
        } else if (bfp >= 21 && bfp <= 24) {
            bfpCategory = "Your Body Fat Percentage aligns and categorized as Average";
        } else {
            bfpCategory = "Your Body Fat Percentage aligns and categorized as Obese";
        }

        Calendar calendar = Calendar.getInstance();
        String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        String currentTime = timeFormat.format(calendar.getTime());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        String formattedBfp = String.format(Locale.getDefault(), "%.1f", bfp);
        String bfpResultText = "<big><b>Your BFP is: " + formattedBfp + "%</b></big>";
        String bfpDateTimeText = "<small><small>Calculated on " + currentDate + " at " + currentTime + "</small></small>";

        bfpResultTextView.setText(Html.fromHtml(bfpResultText));
        bfpResultTextView.append("\n");
        bfpResultTextView.append(Html.fromHtml(bfpDateTimeText));
        bfpResultTextView.setTextSize(18); // Set the BFP result text size to 24sp
        bfpCategoryTextView.setText(bfpCategory);

        // Save current BFP value with date, time, and day
        SharedPreferences sharedPrefs = getSharedPreferences("BFP_ENTRIES_FEMALE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        int entryCount = sharedPrefs.getInt("entryCount", 0) + 1;

        // Retrieve previous BFP value if available
        double previousBFP = 0.0;
        if (entryCount > 1) {
            String previousBFPString = sharedPrefs.getString("bfpValue" + (entryCount - 1), "");
            if (!previousBFPString.isEmpty()) {
                previousBFP = Double.parseDouble(previousBFPString);
            }
        }

        editor.putString("bfpValue" + entryCount, String.valueOf(bfp));
        editor.putString("bfpName" + entryCount, nameString);
        editor.putString("bfpDate" + entryCount, date);
        editor.putString("bfpTime" + entryCount, time);
        editor.putString("bfpWeight" + entryCount, weightString);
        editor.putString("bfpHeight" + entryCount, heightString);
        editor.putString("bfpWaist" + entryCount, waistString);
        editor.putString("bfpNeck" + entryCount, neckString);
        editor.putString("bfpHip" + entryCount, hipString);
        editor.putString("bfpCategory" + entryCount, bfpCategory);

        editor.putInt("entryCount", entryCount);
        editor.apply();

        /*// Compare with previous BFP value
        if (previousBFP != 0.0) {
            previousBFP = Double.parseDouble(String.valueOf(previousBFP));
            double bfpDifference = bfp - previousBFP;
            String updateMessage;
            if (bfpDifference > 0) {
                updateMessage = "Your BFP has increased by " + String.format(Locale.getDefault(), "%.2f", Math.abs(bfpDifference)) + "% from " + String.format(Locale.getDefault(), "%.2f", Math.abs(previousBFP)) + "% to " + formattedBfp + "%.";
            } else if (bfpDifference < 0) {
                updateMessage = "Your BFP has decreased by " + String.format(Locale.getDefault(), "%.2f", Math.abs(bfpDifference)) + "% from " + String.format(Locale.getDefault(), "%.2f", Math.abs(previousBFP)) + "% to " + formattedBfp + "%.";
            } else {
                updateMessage = "Your BFP remains the same";
            }
            bfpUpdate.setText(updateMessage);
            bfpUpdate.setVisibility(View.VISIBLE);
        }*/
    }
    public void openBfpHistoryActivity_female() {
        Intent intent = new Intent(this, BfpHistoryActivity_female.class);
        startActivity(intent);
    }
}