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

public class BmrHistoryActivity extends AppCompatActivity {

    private TextView historyTextView;
    private LottieAnimationView outputsHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmr_history);

        historyTextView = findViewById(R.id.historyTextView);
        outputsHistory = findViewById(R.id.outputsHistory);

        outputsHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });

        // Retrieve BMR calculation history from shared preferences
        SharedPreferences sharedPrefs = getSharedPreferences("BMR_ENTRIES", MODE_PRIVATE);
        List<String> historyList = getBmrHistory(sharedPrefs);

        // Set the history text to the TextView
        setHistoryText(historyList);
    }

    private List<String> getBmrHistory(SharedPreferences sharedPrefs) {
        List<String> historyList = new ArrayList<>();
        int entryCount = sharedPrefs.getInt("entryCount", 0);

        for (int i = 1; i <= entryCount; i++) {
            String bmrName = sharedPrefs.getString("bmrName" + i, "");
            String bmrValue = sharedPrefs.getString("bmrValue" + i, "");
            String bmrDate = sharedPrefs.getString("bmrDate" + i, "");
            String bmrTime = sharedPrefs.getString("bmrTime" + i, "");
            String bmrWeight = sharedPrefs.getString("bmrWeight" + i, "");
            String bmrAge = sharedPrefs.getString("bmrAge" + i, "");
            String bmrGender = sharedPrefs.getString("bmrGender" + i, "");

            String entry = "_______________________\nBMR Value: " + bmrValue + " kcal\n" +
                    "Name: " + bmrName + "\n" +
                    "Weight: " + bmrWeight + "\n" +
                    "Age: " + bmrAge + "\n" +
                    "Gender: " + bmrGender + "\n" +
                    "Date: " + bmrDate + "\n" +
                    "Time: " + bmrTime;

            historyList.add(entry);
        }

        return historyList;
    }

    private void setHistoryText(List<String> historyList) {
        StringBuilder historyTextBuilder = new StringBuilder();

        if (!historyList.isEmpty()) {
            historyTextBuilder.append("BMR Calculation History:\n\n");

            for (int i = historyList.size() - 1; i >= 0; i--) {
                String entry = historyList.get(i);
                historyTextBuilder.append(entry);
                historyTextBuilder.append("\n\n");
            }
        } else {
            historyTextBuilder.append("BMR History is empty");
        }

        historyTextView.setText(historyTextBuilder.toString());
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BmrHistoryActivity.this);
        builder.setTitle("Search BMR History");

        LayoutInflater inflater = LayoutInflater.from(BmrHistoryActivity.this);
        View dialogView = inflater.inflate(R.layout.dialog_search, null);
        builder.setView(dialogView);

        EditText searchEditText = dialogView.findViewById(R.id.searchEditText);
        Button searchButton = dialogView.findViewById(R.id.searchButton);

        AlertDialog searchDialog = builder.create();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personName = searchEditText.getText().toString().trim();
                List<String> filteredHistory = getBmrHistoryForPerson(personName);
                setHistoryText(filteredHistory);
                searchDialog.dismiss();
            }
        });

        searchDialog.show();
    }

    private List<String> getBmrHistoryForPerson(String personName) {
        SharedPreferences sharedPrefs = getSharedPreferences("BMR_ENTRIES", MODE_PRIVATE);
        List<String> historyList = new ArrayList<>();
        int entryCount = sharedPrefs.getInt("entryCount", 0);

        for (int i = 1; i <= entryCount; i++) {
            String bmrName = sharedPrefs.getString("bmrName" + i, "");

            if (personName.equalsIgnoreCase(bmrName)) {
                String bmrValue = sharedPrefs.getString("bmrValue" + i, "");
                String bmrDate = sharedPrefs.getString("bmrDate" + i, "");
                String bmrTime = sharedPrefs.getString("bmrTime" + i, "");
                String bmrWeight = sharedPrefs.getString("bmrWeight" + i, "");
                String bmrAge = sharedPrefs.getString("bmrAge" + i, "");
                String bmrGender = sharedPrefs.getString("bmrGender" + i, "");

                String entry = "_______________________\nBMR Value: " + bmrValue + " kcal\n" +
                        "Name: " + bmrName + "\n" +
                        "Weight: " + bmrWeight + "\n" +
                        "Age: " + bmrAge + "\n" +
                        "Gender: " + bmrGender + "\n" +
                        "Date: " + bmrDate + "\n" +
                        "Time: " + bmrTime;

                historyList.add(entry);
            }
        }

        return historyList;
    }
}
