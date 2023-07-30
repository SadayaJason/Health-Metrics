package com.example.projectaltar;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/*import com.example.projectaltar.databinding.ActivityMainBinding;*/
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityHome extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText name, age, sex;
    private Button btnsave;
    private RadioGroup radioGroup;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;

    boolean isCheckedDone = false;
    Dialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDialog = new Dialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        /*sex = findViewById(R.id.sex);*/
        btnsave = findViewById(R.id.btn_save);
        radioGroup = findViewById(R.id.radioGroup);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);



        /*Spinner spinner = findViewById(R.id.edit_gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);*/


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*String input1 = name.getText().toString().trim();
                String input2 = age.getText().toString().trim();
                String input3 = sex.getText().toString().trim();

                boolean isValid = true;

                if (input1.isEmpty()) {
                    name.setError("This field is required.");
                    isValid = false;
                }

                if (input2.isEmpty()) {
                    age.setError("This field is required.");
                    isValid = false;
                }

                if (input3.isEmpty()) {
                    sex.setError("This field is required.");
                    isValid = false;
                }

                if (isValid) {
                    // Proceed with the desired action or save the data
                    // ...
                }*/
            }
        });

        LottieAnimationView lottieButton = findViewById(R.id.lottieButton);
        lottieButton.setAnimation(R.raw.fingerprint);
        lottieButton.setRepeatCount(0);
        lottieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHome.this);
                builder.setTitle("Confirmation")
                        .setMessage("DATA PRIVACY NOTICE!!\n\nThe participants are assured that the personal data and other sensitive data entrusted to the Health Metric shall be used with due diligence and prudence, for the sole purpose of gathering registrations.\n\nAs such, upon accessing the registration form, you acknowledge and agree that the information may be used and disclosed by the Health Metric in accordance with any legal and regulatory standards and in compliance with the “Data Privacy Act of 2012”.")
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input1 = name.getText().toString().trim();
                                String input2 = age.getText().toString().trim();
                                /*String input3 = sex.getText().toString().trim();*/

                                boolean isValid = true;

                                if (input1.isEmpty()) {
                                    name.setError("This field is required.");
                                    isValid = false;
                                }

                                if (input2.isEmpty()) {
                                    age.setError("This field is required.");
                                    isValid = false;
                                }

                                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                                if (selectedRadioButtonId == -1) {
                                    // No radio button selected
                                    Toast.makeText(ActivityHome.this, "Please select an option", Toast.LENGTH_SHORT).show();
                                    isValid = false;
                                }

                                if (isValid) {
                                    // Save the selected option to the database
                                    String gender;
                                    if (selectedRadioButtonId == R.id.radioButtonMale) {
                                        gender = "Male";
                                    } else {
                                        gender = "Female";
                                    }
                                    /*mDatabase.child("Gender").setValue(selectedOption);*/

                                    // Proceed with the desired action or save the data
                                    // ...

                                    if (isCheckedDone) {
                                        lottieButton.setSpeed(-1);
                                        lottieButton.playAnimation();
                                        isCheckedDone = false;
                                    } else {
                                        lottieButton.setSpeed(1);
                                        lottieButton.playAnimation();
                                        isCheckedDone = true;
                                    }

                                    lottieButton.playAnimation();

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            opencalculator_categories();
                                            saveData();
                                        }
                                    }, 2000); // 2-second delay (2000 milliseconds)
                                }

                                /*opencalculator_categories();
                                saveData();*/
                            }
                        })
                        .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ActivityHome.this, "Declined", Toast.LENGTH_LONG).show();
                            }
                        })
                        .create()
                        .show();
            }
        });



        // Retrieve the saved state from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String currentPage = preferences.getString("currentPage", "");

        // Check if a valid saved state exists
        if (!currentPage.isEmpty()) {
            // Navigate to the appropriate screen based on the saved state
            switch (currentPage) {
                case "calculator_categories":
                    startActivity(new Intent(this, calculator_categories.class));
                    break;
                // I will add more cases for other screens if needed
            }

            // Finishing the current activity so I can prevent returning to it's previous activity when pressing the back button
            finish();
            return;
        }

        /*zxcz*/

    }



    private void saveData() {
        try {
            String gender = "";

            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            if (selectedRadioButtonId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                gender = selectedRadioButton.getText().toString();
            }

            User user = new User(name.getText().toString(),
                    age.getText().toString(),
                    gender);

            mDatabase.child("users").child(user.getName()).setValue(user);
            Toast.makeText(ActivityHome.this, "Successfully Added Data", Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }




    public void opencalculator_categories() {
        // Save the current page to SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("currentPage", "calculator_categories");
        editor.apply();

        // Start the calculator_categories activity
        Intent intent = new Intent(this, calculator_categories.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        // Leaving this method empty so I could prevent any action when the back button is clicked by the user.
    }
}