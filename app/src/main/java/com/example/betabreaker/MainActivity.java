package com.example.betabreaker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.betabreaker.Frags.FragWelcomeScrn;
import com.example.betabreaker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = new Intent(this, ActDisplayApp.class);
        startActivity(intent);

        // Display FraWelcomeScrn fragment immediately
       // displayWelcomeScreen();
    }

    private void displayWelcomeScreen() {
        FragWelcomeScrn welcomeFragment = new FragWelcomeScrn();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, welcomeFragment);
        transaction.commit();
    }
}