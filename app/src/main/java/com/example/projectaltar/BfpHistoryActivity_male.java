package com.example.projectaltar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
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

public class BfpHistoryActivity_male extends AppCompatActivity {

    private TextView historyTextView;
    private LottieAnimationView outputsHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfp_history_male);

        historyTextView = findViewById(R.id.historyTextView);
        outputsHistory = findViewById(R.id.outputsHistory);

        outputsHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });

        // Retrieve BMR calculation history from shared preferences
        SharedPreferences sharedPrefs = getSharedPreferences("BFP_ENTRIES_MALE", MODE_PRIVATE);
        List<String> historyList = getBfpHistory(sharedPrefs);

        // Set the history text to the TextView
        setHistoryText(historyList);
    }

    private List<String> getBfpHistory(SharedPreferences sharedPrefs) {
        List<String> historyList = new ArrayList<>();
        int entryCount = sharedPrefs.getInt("entryCount", 0);

        for (int i = 1; i <= entryCount; i++) {
            String bfpName = sharedPrefs.getString("bfpName" + i, "");
            String bfpValue = sharedPrefs.getString("bfpValue" + i, "");
            double bfpValueDouble = Double.parseDouble(bfpValue);
            String formattedBfpValue = String.format(Locale.getDefault(), "%.2f", bfpValueDouble);
            String bfpDate = sharedPrefs.getString("bfpDate" + i, "");
            String bfpTime = sharedPrefs.getString("bfpTime" + i, "");
            String bfpWeight = sharedPrefs.getString("bfpWeight" + i, "");
            String bfpHeight = sharedPrefs.getString("bfpHeight" + i, "");
            String bfpWaist = sharedPrefs.getString("bfpWaist" + i, "");
            String bfpNeck = sharedPrefs.getString("bfpNeck" + i, "");
            String bfpCategory = sharedPrefs.getString("bfpCategory" + i, "");

            String entry = "_______________________\nBFP Value: " + formattedBfpValue + "%\n" +
                    "Name: " + bfpName + "\n" +
                    "Weight: " + bfpWeight + "kg\n" +
                    "Height: " + bfpHeight + "cm\n" +
                    "Waist: " + bfpWaist + "cm\n" +
                    "Neck: " + bfpNeck + "cm\n" +
                    "Category: " + bfpCategory + "\n" +
                    "Date: " + bfpDate + "\n" +
                    "Time: " + bfpTime;

            historyList.add(entry);
        }

        return historyList;
    }

    private void setHistoryText(List<String> historyList) {
        StringBuilder historyTextBuilder = new StringBuilder();

        if (!historyList.isEmpty()) {
            historyTextBuilder.append("BFP Calculation History:\n\n");

            for (int i = historyList.size() - 1; i >= 0; i--) {
                String entry = historyList.get(i);
                historyTextBuilder.append(entry);
                historyTextBuilder.append("\n\n");
            }
        } else {
            historyTextBuilder.append("BFP History is empty");
        }

        historyTextView.setText(historyTextBuilder.toString());
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BfpHistoryActivity_male.this);
        builder.setTitle("Search BFP History");

        LayoutInflater inflater = LayoutInflater.from(BfpHistoryActivity_male.this);
        View dialogView = inflater.inflate(R.layout.dialog_search, null);
        builder.setView(dialogView);

        EditText searchEditText = dialogView.findViewById(R.id.searchEditText);
        Button searchButton = dialogView.findViewById(R.id.searchButton);

        AlertDialog searchDialog = builder.create();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personName = searchEditText.getText().toString().trim();
                List<String> filteredHistory = getBfpHistoryForPerson(personName);
                setHistoryText(filteredHistory);
                searchDialog.dismiss();
            }
        });

        searchDialog.show();
    }

    private List<String> getBfpHistoryForPerson(String personName) {
        SharedPreferences sharedPrefs = getSharedPreferences("BFP_ENTRIES_MALE", MODE_PRIVATE);
        List<String> historyList = new ArrayList<>();
        int entryCount = sharedPrefs.getInt("entryCount", 0);

        for (int i = 1; i <= entryCount; i++) {
            String bfpName = sharedPrefs.getString("bfpName" + i, "");

            if (personName.equalsIgnoreCase(bfpName)) {
            String bfpValue = sharedPrefs.getString("bfpValue" + i, "");
            double bfpValueDouble = Double.parseDouble(bfpValue);
            String formattedBfpValue = String.format(Locale.getDefault(), "%.2f", bfpValueDouble);
            String bfpDate = sharedPrefs.getString("bfpDate" + i, "");
            String bfpTime = sharedPrefs.getString("bfpTime" + i, "");
            String bfpWeight = sharedPrefs.getString("bfpWeight" + i, "");
            String bfpHeight = sharedPrefs.getString("bfpHeight" + i, "");
            String bfpWaist = sharedPrefs.getString("bfpWaist" + i, "");
            String bfpNeck = sharedPrefs.getString("bfpNeck" + i, "");
            String bfpCategory = sharedPrefs.getString("bfpCategory" + i, "");

            String entry = "_______________________\nBFP Value: " + formattedBfpValue + "%\n" +
                    "Name: " + bfpName + "\n" +
                    "Weight: " + bfpWeight + "kg\n" +
                    "Height: " + bfpHeight + "cm\n" +
                    "Waist: " + bfpWaist + "cm\n" +
                    "Neck: " + bfpNeck + "cm\n" +
                    "Category: " + bfpCategory + "\n" +
                    "Date: " + bfpDate + "\n" +
                    "Time: " + bfpTime;

                historyList.add(entry);
            }
        }

        return historyList;
    }
}