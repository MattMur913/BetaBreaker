package com.example.betabreaker;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.betabreaker.Frags.FragWelcomeScrn;
import com.example.betabreaker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.betabreaker.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Display FraWelcomeScrn fragment immediately
        Log.d("SingleCentre7", "MainAct");
       displayWelcomeScreen();
    }

    private void displayWelcomeScreen() {
        FragWelcomeScrn welcomeFragment = new FragWelcomeScrn();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.welcome_act, welcomeFragment);
        transaction.commit();
    }
}