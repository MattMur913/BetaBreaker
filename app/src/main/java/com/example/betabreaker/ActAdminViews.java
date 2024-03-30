package com.example.betabreaker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.betabreaker.Classes.ClsCentre;
import com.example.betabreaker.Classes.ClsRoutes;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.Frags.FragAddRoute;
import com.example.betabreaker.Frags.FragDisplayRoutes;
import com.example.betabreaker.Frags.FragEditCentre;
import com.example.betabreaker.Frags.FragSpecCentre;
import com.example.betabreaker.databinding.ActivityAdminViewsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActAdminViews extends AppCompatActivity {

    private List<ClsCentre> centreList = new ArrayList<>();
    private ActivityAdminViewsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminViewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String centreID = preferences.getString("adminOf", "");
        //fetchSingleCentre(centreID);
        hardCersion(centreID);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragSpecCentre fragment = new FragSpecCentre();
        Bundle bundle = new Bundle();
        bundle.putSerializable("centre", centreList.get(0));
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragContent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        final Button btnEditCentre = binding.editCentre;
        final Button btnEditRoute = binding.editRoutes;
        final Button btnAddRoute = binding.addRoute;

        btnEditCentre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragEditCentre fragment = new FragEditCentre();
                Bundle bundle = new Bundle();
                bundle.putSerializable("centre", centreList.get(0));
                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragContent, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });

        btnAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragAddRoute fragment = new FragAddRoute();
                Bundle bundle = new Bundle();
                bundle.putSerializable("centre", centreList.get(0));
                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragContent, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        btnEditRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragDisplayRoutes fragment = new FragDisplayRoutes();
                Bundle bundle = new Bundle();
                ClsCentre centre = centreList.get(0);
                List<ClsRoutes> routes = centre.getRoutes();
                bundle.putSerializable("routes", (Serializable) routes);
                bundle.putSerializable("centreID",centre.getIdCentre());
                Log.d("SingleCentre5", String.valueOf(centre.getIdCentre()));
                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragContent, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });

    }
    private void hardCersion(String centreID) {
        Log.d("SingleCentre", "Getting centre");
        // Hardcoded centre ID
        centreID = "your_hardcoded_centre_id";
        // LogicAppUrl is not used in this case as we're hardcoding the data
        // String logicAppUrl = GlobalUrl.getSinCentreUrl.replace("{id}",centreID);
        // Log.d("SingleCentre2", logicAppUrl);

        // Hardcoded JSON response for testing
        String hardcodedResponse = "{\"id\":\"JTJmcHJvamVjdC1pbWFnZXMlMmY2Mzg0NjYzNjMzNzcwNzA1MDg=\",\"centreName\":\"Your Centre Name\",\"description\":\"Centre Description\",\"Address\":\"Centre Address\",\"logoName\":\"638472401472166118\",\"RouteDetails\":[{\"Area\":\"Area 1\",\"Colour\":\"Red\",\"Grades\":\"Grade 1\",\"SetDate\":\"2024-03-30\",\"Setter\":\"Setter 1\",\"Upvotes\":\"5\",\"imageUrl\":\"638472401472166118\"},{\"Area\":\"Area 2\",\"Colour\":\"Blue\",\"Grades\":\"Grade 2\",\"SetDate\":\"2024-03-31\",\"Setter\":\"Setter 2\",\"Upvotes\":\"8\",\"imageUrl\":\"638472401472166118\"}]}";

        try {
            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(hardcodedResponse);
            String id = jsonResponse.getString("id");
            String name = jsonResponse.getString("centreName");
            String address = jsonResponse.getString("description");
            String description = jsonResponse.getString("Address");
            String logoid = jsonResponse.getString("logoName");

            // Parse route details
            List<ClsRoutes> routes = new ArrayList<>();
            JSONArray routeDetailsArray = jsonResponse.getJSONArray("RouteDetails");
            for (int i = 0; i < routeDetailsArray.length(); i++) {
                JSONObject routeObject = routeDetailsArray.getJSONObject(i);
                String area = routeObject.optString("Area", "");
                String colour = routeObject.optString("Colour", "");
                String grades = routeObject.optString("Grades", "");
                String setDate = routeObject.optString("SetDate", "");
                String setter = routeObject.optString("Setter", "");
                String upvotes = routeObject.optString("Upvotes", "");
                String imageUrl = routeObject.optString("imageUrl", "");

                // Create a ClsRoutes object and add it to the list
                ClsRoutes route = new ClsRoutes(area, colour, grades, setDate, setter, upvotes, imageUrl);
                routes.add(route);
            }

            // Create a ClsCentre object
            ClsCentre centre = new ClsCentre(id, name, address, description, "", "", "", logoid, routes);

            // Add the centre to the list
            centreList.add(centre);

            // Further processing...
        } catch (JSONException e) {
            Log.d("SingleCentre", "JSONException: " + e.getMessage());
        }
    }


    private void fetchSingleCentre(String centreID){
        Log.d("SingleCentre", "Getting centre");
        centreID = "JTJmcHJvamVjdC1pbWFnZXMlMmY2Mzg0NjYzNjMzNzcwNzA1MDg=";
        String logicAppUrl = GlobalUrl.getSinCentreUrl.replace("{id}",centreID);
        Log.d("SingleCentre2", logicAppUrl);

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
                        JSONObject jsonResponse = new JSONObject(responseData);
                        String id = jsonResponse.getString("id");
                        String name = jsonResponse.getString("centreName");
                        String address = jsonResponse.getString("description");
                        String description = jsonResponse.getString("Address");
                        String logoid = jsonResponse.getString("logoName");

                        // Check if "RouteDetails" exists in the JSON
                        List<ClsRoutes> routes = new ArrayList<>();
                        JSONArray routeDetailsArray = jsonResponse.getJSONArray("RouteDetails");
                        for (int i = 0; i < routeDetailsArray.length(); i++) {
                            JSONObject routeObject = routeDetailsArray.getJSONObject(i);
                            String area = routeObject.optString("Area", "");
                            String colour = routeObject.optString("Colour", "");
                            String grades = routeObject.optString("Grades", "");
                            String setDate = routeObject.optString("SetDate", "");
                            String setter = routeObject.optString("Setter", "");
                            String upvotes = routeObject.optString("Upvotes", "");
                            String imageUrl = routeObject.optString("imageUrl", "");

                            // Create a ClsRoutes object and add it to the list
                            ClsRoutes route = new ClsRoutes(area, colour, grades, setDate, setter, upvotes, imageUrl);
                            routes.add(route);
                        }

                        // Create a ClsCentre object and add it to the list
                        ClsCentre centre = new ClsCentre(id, name, address, description, "", "", "", logoid, routes);
                        Log.d("SingleCentre2", String.valueOf(centre.getCentreName()));
                        centreList.add(centre);


                        //This for loop does not display the centres recieved from fetchSingleCentre
                        for(int i=0;i< centreList.size();i++){
                            Log.d("SingleCentre1", String.valueOf(centreList.get(i).getIdCentre()));
                        }
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragSpecCentre fragment = new FragSpecCentre();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("centre", centreList.get(0));
                        fragment.setArguments(bundle);

                        fragmentTransaction.replace(R.id.fragContent, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    } catch (JSONException e) {
                        Log.d("SingleCentre", "JSONException: " + e.getMessage());
                    }
                }
            }

        });
    }


}