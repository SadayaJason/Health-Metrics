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

public class LbmHistoryActivity extends AppCompatActivity {

    private TextView historyTextView;
    private LottieAnimationView outputsHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lbm_history);

        historyTextView = findViewById(R.id.historyTextView);
        outputsHistory = findViewById(R.id.outputsHistory);

        outputsHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });

        // Retrieve LBM calculation history from shared preferences
        SharedPreferences sharedPrefs = getSharedPreferences("LBM_ENTRIES", MODE_PRIVATE);
        List<String> historyList = getLbmHistory(sharedPrefs);

        // Set the history text to the TextView
        setHistoryText(historyList);
    }

    private List<String> getLbmHistory(SharedPreferences sharedPrefs) {
        List<String> historyList = new ArrayList<>();
        int entryCount = sharedPrefs.getInt("entryCount", 0);

        for (int i = 1; i <= entryCount; i++) {
            String lbmName = sharedPrefs.getString("lbmName" + i, "");
            String lbmValue = sharedPrefs.getString("lbmValue" + i, "");
            String lbmDate = sharedPrefs.getString("lbmDate" + i, "");
            String lbmTime = sharedPrefs.getString("lbmTime" + i, "");
            String lbmWeight = sharedPrefs.getString("lbmWeight" + i, "");
            String lbmHeight = sharedPrefs.getString("lbmHeight" + i, "");
            String lbmGender = sharedPrefs.getString("lbmGender" + i, "");

            String entry = "_______________________\nLBM Value: " + lbmValue + " kcal\n" +
                    "Name: " + lbmName + "\n" +
                    "Weight: " + lbmWeight + "\n" +
                    "Height: " + lbmHeight + "\n" +
                    "Gender: " + lbmGender + "\n" +
                    "Date: " + lbmDate + "\n" +
                    "Time: " + lbmTime;

            historyList.add(entry);
        }

        return historyList;
    }

    private void setHistoryText(List<String> historyList) {
        StringBuilder historyTextBuilder = new StringBuilder();

        if (!historyList.isEmpty()) {
            historyTextBuilder.append("LBM Calculation History:\n\n");

            for (int i = historyList.size() - 1; i >= 0; i--) {
                String entry = historyList.get(i);
                historyTextBuilder.append(entry);
                historyTextBuilder.append("\n\n");
            }
        } else {
            historyTextBuilder.append("LBM History is empty");
        }

        historyTextView.setText(historyTextBuilder.toString());
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LbmHistoryActivity.this);
        builder.setTitle("Search LBM History");

        LayoutInflater inflater = LayoutInflater.from(LbmHistoryActivity.this);
        View dialogView = inflater.inflate(R.layout.dialog_search, null);
        builder.setView(dialogView);

        EditText searchEditText = dialogView.findViewById(R.id.searchEditText);
        Button searchButton = dialogView.findViewById(R.id.searchButton);

        AlertDialog searchDialog = builder.create();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personName = searchEditText.getText().toString().trim();
                List<String> filteredHistory = getLbmHistoryForPerson(personName);
                setHistoryText(filteredHistory);
                searchDialog.dismiss();
            }
        });

        searchDialog.show();
    }

    private List<String> getLbmHistoryForPerson(String personName) {
        SharedPreferences sharedPrefs = getSharedPreferences("LBM_ENTRIES", MODE_PRIVATE);
        List<String> historyList = new ArrayList<>();
        int entryCount = sharedPrefs.getInt("entryCount", 0);

        for (int i = 1; i <= entryCount; i++) {
            String lbmName = sharedPrefs.getString("lbmName" + i, "");

            if (personName.equalsIgnoreCase(lbmName)) {
                String lbmValue = sharedPrefs.getString("lbmValue" + i, "");
                String lbmDate = sharedPrefs.getString("lbmDate" + i, "");
                String lbmTime = sharedPrefs.getString("lbmTime" + i, "");
                String lbmWeight = sharedPrefs.getString("lbmWeight" + i, "");
                String lbmGender = sharedPrefs.getString("lbmGender" + i, "");

                String entry = "_______________________\nLBM Value: " + lbmValue + " kcal\n" +
                        "Name: " + lbmName + "\n" +
                        "Weight: " + lbmWeight + "\n" +
                        "Gender: " + lbmGender + "\n" +
                        "Date: " + lbmDate + "\n" +
                        "Time: " + lbmTime;

                historyList.add(entry);
            }
        }

        return historyList;
    }
}