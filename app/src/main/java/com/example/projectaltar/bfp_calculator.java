package com.example.projectaltar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class bfp_calculator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfp_calculator);

        LottieAnimationView lottieButton_bfp_male = findViewById(R.id.maleCalculation);
        lottieButton_bfp_male.setAnimation(R.raw.menwalking);
        lottieButton_bfp_male.setRepeatCount(LottieDrawable.INFINITE);

        lottieButton_bfp_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openbfp_male();
            }
        });

        LottieAnimationView lottieButton_bfp_female = findViewById(R.id.femaleCalculation);
        lottieButton_bfp_female.setAnimation(R.raw.womenwalking);
        lottieButton_bfp_female.setRepeatCount(LottieDrawable.INFINITE);

        lottieButton_bfp_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openbfp_female();
            }
        });
    }

    public void openbfp_male() {
        Intent intent = new Intent(this, bfp_male.class);
        startActivity(intent);
    }

    public void openbfp_female() {
        Intent intent = new Intent(this, bfp_female.class);
        startActivity(intent);
    }
}