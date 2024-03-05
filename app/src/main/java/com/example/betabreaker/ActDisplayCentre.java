package com.example.betabreaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.betabreaker.Classes.ClsCentre;
import com.example.betabreaker.Classes.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActDisplayCentre extends AppCompatActivity {

    private List<ClsCentre> centreList = new ArrayList<>(); // List to hold ClsCentre objects
    private RecyclerView recyclerView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_centre);

        // Load ClsCentre objects (Replace this with your actual data loading mechanism)
        createInstances();

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.dsCRec);
        adapter = new MyAdapter(centreList);

        // Set layout manager and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



    }

    public void createInstances(){
        centreList.add( new ClsCentre("1", "Community Center", "123 Main St", "A hub for community activities", "info@example.com", "555-1234", "example.com", 1));
       centreList.add( new ClsCentre("2", "Library", "456 Elm St", "", "", "", "", 2));
        centreList.add(  new ClsCentre("3", "Park", "789 Oak St", "", "", "", "", 3));
        centreList.add(  new ClsCentre("4", "Fitness Center", "1010 Pine St", "State-of-the-art gym facilities", "fitness@example.com", "555-5678", "fitnesscenter.com", 4));
        centreList.add(new ClsCentre("5", "Museum", "1313 Maple St", "Preserving history and culture", "museum@example.com", "", "", 5));

    }

}