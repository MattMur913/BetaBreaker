package com.example.betabreaker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.betabreaker.databinding.ActivityDisplayAppBinding;
import com.google.android.material.navigation.NavigationView;

public class ActDisplayApp extends AppCompatActivity {

    private ActivityDisplayAppBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDisplayAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int adminValue = sharedPreferences.getInt("admin", 0); // Default value 0
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.userName);
        TextView userEmailTextView = headerView.findViewById(R.id.userEmail);
        if (adminValue < 1) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.FragDisplayCentres, R.id.FragFavourite, R.id.FragUserView)
                    .setOpenableLayout(drawer)
                    .build();
            Menu menu = navigationView.getMenu();
            MenuItem adminItem = menu.findItem(R.id.FragEditCentre);
            adminItem.setVisible(false);
        } else {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.FragDisplayCentres, R.id.FragFavourite, R.id.FragUserView, R.id.FragEditCentre) // Add nav_admin
                    .setOpenableLayout(drawer)
                    .build();

        }
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        String userName = sharedPreferences.getString("username", "");
        String userEmail = sharedPreferences.getString("email", "");

        userNameTextView.setText(userName);
        userEmailTextView.setText(userEmail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}

