package com.example.projectaltar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class calculator_categories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_categories);

        LottieAnimationView lottieButton_bmi = findViewById(R.id.bmiCalculator);
        lottieButton_bmi.setAnimation(R.raw.bmi_calculator);
        lottieButton_bmi.setRepeatCount(LottieDrawable.INFINITE);

        lottieButton_bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openbmi_calculator();
            }
        });

        LottieAnimationView lottieButton_bmr = findViewById(R.id.bmrCalculator);
        lottieButton_bmr.setAnimation(R.raw.bmr_calculator);
        lottieButton_bmr.setRepeatCount(LottieDrawable.INFINITE);

        lottieButton_bmr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openbmr_calculator();
            }
        });

        LottieAnimationView lottieButton_bfp = findViewById(R.id.bfpCalculator);
        lottieButton_bfp.setAnimation(R.raw.shieldhealth);
        lottieButton_bfp.setRepeatCount(LottieDrawable.INFINITE);

        lottieButton_bfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openbfp_calculator();
            }
        });

        LottieAnimationView lottieButton_lbm = findViewById(R.id.lbmCalculator);
        lottieButton_lbm.setAnimation(R.raw.lbm_calculator);
        lottieButton_lbm.setRepeatCount(LottieDrawable.INFINITE);

        lottieButton_lbm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openleanbodymax_calculator();
            }
        });

    }

    /*<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="BMR Calculator"
    android:gravity="center"
    android:textSize="28sp"
    android:textColor="@color/black"
    android:layout_marginTop="20dp"
    android:fontFamily="serif"/>*/


    @Override
    public void onBackPressed() {
        // Leaving this method empty so I could prevent any action when the back button is clicked by the user.
    }

    public void openbmi_calculator() {
        Intent intent = new Intent(this, bmi_calculator.class);
        intent.putExtra("popup_delay", 200); // .2 seconds delay before showing the privacy_popup dialog
        startActivity(intent);
    }

    public void openbmr_calculator() {
        Intent intent = new Intent(this, bmr_calculator.class);
        intent.putExtra("popup_delay", 200);
        startActivity(intent);
    }

    public void openbfp_calculator() {
        Intent intent = new Intent(this, bfp_calculator.class);
        startActivity(intent);
    }

    public void openleanbodymax_calculator() {
        Intent intent = new Intent(this, leanbodymax_calculator.class);
        intent.putExtra("popup_delay", 200);
        startActivity(intent);
    }
}