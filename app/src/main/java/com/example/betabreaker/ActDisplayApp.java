package com.example.betabreaker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

        if (adminValue < 1) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                    .setOpenableLayout(drawer)
                    .build();
            Menu menu = navigationView.getMenu();
            MenuItem adminItem = menu.findItem(R.id.nav_admin);
            adminItem.setVisible(false);
        } else {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_admin) // Add nav_admin
                    .setOpenableLayout(drawer)
                    .build();

        }


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentName;
        int foundIt = 0;
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null) {
                fragmentName = fragment.getClass().getSimpleName();
                Log.d("CurrentFragment", "Fragment: " + fragmentName);
                if (fragmentName.equals("FragSpecCentre")) {
                    Log.d("TurtwigSolos", "onBackPressed: Pooped ");
                    // Now, make Fragment1 visible again
                    /*
                    FragDisplayCentres reDisplay = (FragDisplayCentres) fragmentManager.findFragmentByTag("hidden");
                    if (reDisplay != null) {
                        Log.d("TurtwigSolos", "onBackPressed: Noyt null");
                        // Fragment1 exists, make it visible
                        View fragment1View = reDisplay.getView();
                        if (fragment1View != null) {
                            Log.d("TurtwigSolos", "onBackPressed: Make visible");
                            RecyclerView recyclerView = fragment1View.findViewById(R.id.dsCRec);
                            recyclerView.setVisibility(View.VISIBLE);

                            // Show the EditText
                            EditText searchEditText = fragment1View.findViewById(R.id.searchEditText);
                            searchEditText.setVisibility(View.VISIBLE);
                            foundIt = 1;
                        }
                    }

                     */
                }
            }
        }
        if (foundIt == 0) {
            super.onBackPressed();
        }
    }
}

