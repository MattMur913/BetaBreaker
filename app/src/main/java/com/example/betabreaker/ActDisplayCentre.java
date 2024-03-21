package com.example.betabreaker;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betabreaker.Classes.ClsCentre;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.Classes.MyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActDisplayCentre extends AppCompatActivity {

    private List<ClsCentre> centreList = new ArrayList<>(); // List to hold ClsCentre objects
    private RecyclerView recyclerView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_centre);

        // Call method to fetch data from Logic App
        fetchDataFromLogicApp();

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.dsCRec);
        adapter = new MyAdapter(centreList, this);

        // Set layout manager and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Get the currently displayed fragment
        Fragment fragment = fragmentManager.findFragmentById(R.id.dsCLayout);

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





    private void fetchDataFromLogicApp() {
        // Logic App endpoint URL
        String logicAppUrl = GlobalUrl.getCentresUrl;

        // Create an instance of OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Create a request
        Request request = new Request.Builder()
                .url(logicAppUrl)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Parse the JSON response and update the RecyclerView
                    try {
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String name = jsonObject.getString("centreName");
                            String address = jsonObject.getString("description");
                            String description = jsonObject.getString("Address");
                            String logoid = jsonObject.getString("logoName");
                            // Parse other fields similarly

                            // Create a ClsCentre object and add it to the list
                            ClsCentre centre = new ClsCentre(id, name, address, description, "", "", "", logoid);
                            centreList.add(centre);
                        }

                        // Notify adapter of data changes on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void createInstances(){
        centreList.add( new ClsCentre("1", "Community Center", "123 Main St", "A hub for community activities", "info@example.com", "555-1234", "example.com", ""));
        centreList.add( new ClsCentre("2", "Library", "456 Elm St", "", "", "", "", ""));
        centreList.add(  new ClsCentre("3", "Park", "789 Oak St", "", "", "", "", ""));
        centreList.add(  new ClsCentre("4", "Fitness Center", "1010 Pine St", "State-of-the-art gym facilities", "fitness@example.com", "555-5678", "fitnesscenter.com", ""));
        centreList.add(new ClsCentre("5", "Museum", "1313 Maple St", "Preserving history and culture", "museum@example.com", "", "", ""));

    }
}
