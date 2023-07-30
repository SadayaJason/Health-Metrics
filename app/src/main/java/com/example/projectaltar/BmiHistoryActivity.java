package com.example.projectaltar;

// BmiHistoryActivity.java

// Importing ng mga necessary classes
import android.app.AlertDialog;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BmiHistoryActivity extends AppCompatActivity {

    private TextView historyTextView;
    private LottieAnimationView outputsHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyTextView = findViewById(R.id.historyTextView);
        outputsHistory = findViewById(R.id.outputsHistory);

        outputsHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });

        // Retrieve BMI calculation history from shared preferences
        SharedPreferences sharedPrefs = getSharedPreferences("BMI_ENTRIES", MODE_PRIVATE);
        List<String> historyList = getBmiHistory(sharedPrefs);

        // Set the history text to the TextView
        setHistoryText(historyList);
    }

    private List<String> getBmiHistory(SharedPreferences sharedPrefs) {
        List<String> historyList = new ArrayList<>();
        int entryCount = sharedPrefs.getInt("entryCount", 0);

        for (int i = 1; i <= entryCount; i++) {
            String bmiName = sharedPrefs.getString("bmiName" + i, "");
            String bmiValue = sharedPrefs.getString("bmiValue" + i, "");
            double bmiValueDouble = Double.parseDouble(bmiValue);
            String formattedBmiValue = String.format(Locale.getDefault(), "%.2f", bmiValueDouble);
            String bmiDate = sharedPrefs.getString("bmiDate" + i, "");
            String bmiTime = sharedPrefs.getString("bmiTime" + i, "");
            String bmiWeight = sharedPrefs.getString("bmiWeight" + i, "");
            String bmiHeight = sharedPrefs.getString("bmiHeight" + i, "");
            String bmiCategory = sharedPrefs.getString("bmiCategory" + i, "");

            String entry = "_______________________\nBFP Value: " + formattedBmiValue + "%\n" +
                    "Name: " + bmiName + "\n" +
                    "Weight: " + bmiWeight + "kg\n" +
                    "Height: " + bmiHeight + "cm\n" +
                    "Category: " + bmiCategory + "\n" +
                    "Date: " + bmiDate + "\n" +
                    "Time: " + bmiTime;

            historyList.add(entry);
        }

        return historyList;
    }

    private void setHistoryText(List<String> historyList) {
        StringBuilder historyTextBuilder = new StringBuilder();

        if (!historyList.isEmpty()) {
            historyTextBuilder.append("BMI Calculation History:\n\n");

            for (int i = historyList.size() - 1; i >= 0; i--) {
                String entry = historyList.get(i);
                historyTextBuilder.append(entry);
                historyTextBuilder.append("\n\n");
            }
        } else {
            historyTextBuilder.append("BMI History is empty");
        }

        historyTextView.setText(historyTextBuilder.toString());
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BmiHistoryActivity.this);
        builder.setTitle("Search BMI History");

        LayoutInflater inflater = LayoutInflater.from(BmiHistoryActivity.this);
        View dialogView = inflater.inflate(R.layout.dialog_search, null);
        builder.setView(dialogView);

        EditText searchEditText = dialogView.findViewById(R.id.searchEditText);
        Button searchButton = dialogView.findViewById(R.id.searchButton);

        AlertDialog searchDialog = builder.create();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personName = searchEditText.getText().toString().trim();
                List<String> filteredHistory = getBmiHistoryForPerson(personName);
                setHistoryText(filteredHistory);
                searchDialog.dismiss();
            }
        });

        searchDialog.show();
    }

    private List<String> getBmiHistoryForPerson(String personName) {
        SharedPreferences sharedPrefs = getSharedPreferences("BMI_ENTRIES", MODE_PRIVATE);
        List<String> historyList = new ArrayList<>();
        int entryCount = sharedPrefs.getInt("entryCount", 0);

        // Trim the input person name to remove leading and trailing spaces
        personName = personName.trim();

        for (int i = 1; i <= entryCount; i++) {
            String bmiName = sharedPrefs.getString("bmiName" + i, "");

            // Trim the stored name to remove leading and trailing spaces
            bmiName = bmiName.trim();

            if (personName.equalsIgnoreCase(bmiName)) {
                String bmiValue = sharedPrefs.getString("bmiValue" + i, "");
                double bmiValueDouble = Double.parseDouble(bmiValue);
                String formattedBmiValue = String.format(Locale.getDefault(), "%.2f", bmiValueDouble);
                String bmiDate = sharedPrefs.getString("bmiDate" + i, "");
                String bmiTime = sharedPrefs.getString("bmiTime" + i, "");
                String bmiWeight = sharedPrefs.getString("bmiWeight" + i, "");
                String bmiHeight = sharedPrefs.getString("bmiHeight" + i, "");
                String bmiCategory = sharedPrefs.getString("bmiCategory" + i, "");

                String entry = "_______________________\nBFP Value: " + formattedBmiValue + "%\n" +
                        "Name: " + bmiName + "\n" +
                        "Weight: " + bmiWeight + "kg\n" +
                        "Height: " + bmiHeight + "cm\n" +
                        "Category: " + bmiCategory + "\n" +
                        "Date: " + bmiDate + "\n" +
                        "Time: " + bmiTime;

                historyList.add(entry);
            }
        }

        return historyList;
    }
}
