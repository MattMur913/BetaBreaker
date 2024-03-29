package com.example.betabreaker;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betabreaker.Classes.AdapterRoutes;
import com.example.betabreaker.Classes.ClsRoutes;
import com.example.betabreaker.Frags.FragSpecCentre;

import java.util.ArrayList;
import java.util.List;

public class ActDisplayRoutes extends AppCompatActivity {

    private List<ClsRoutes> routesList = new ArrayList<>(); // List to hold ClsCentre objects
    private RecyclerView recyclerView;
    private AdapterRoutes adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_routes);

        routesList = (List<ClsRoutes>) getIntent().getSerializableExtra("routes");
        String centreID = (String) getIntent().getSerializableExtra("centreID");

        recyclerView = findViewById(R.id.dsRRec);
        adapter = new AdapterRoutes(routesList,  centreID,this);

        // Set layout manager and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Get the currently displayed fragment
        Fragment fragment = fragmentManager.findFragmentById(R.id.dsRLayout);

        // Show the RecyclerView if it's hidden
        if (recyclerView.getVisibility() == View.GONE) {
            recyclerView.setVisibility(View.VISIBLE);
            if (fragment instanceof FragSpecCentre) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
            fragmentManager.popBackStack();
        } else if (fragmentManager.getBackStackEntryCount() > 0) {
            // Pop the top fragment from the back stack
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed(); // Proceed with default back button behavior
        }
    }
}