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
import com.example.betabreaker.Frags.FragEditCentre;
import com.example.betabreaker.Frags.FragEditRoute;
import com.example.betabreaker.Frags.FragSpecCentre;
import com.example.betabreaker.databinding.ActivityAdminViewsBinding;

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

public class ActAdminViews extends AppCompatActivity {

    private List<ClsCentre> centreList = new ArrayList<>();
    private ActivityAdminViewsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_views);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String centreID = preferences.getString("adminOf", "");
        fetchSingleCentre(centreID);

        final Button btnEditCentre = binding.editCentre;
        final Button btnEditRoute = binding.editRoutes;

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
        btnEditRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragEditRoute fragment = new FragEditRoute();
                Bundle bundle = new Bundle();
                bundle.putSerializable("centre", centreList.get(0));
                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragContent, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });

    }

    private void fetchSingleCentre(String centreID){
        Log.d("SingleCentre", "Getting centre");
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
                        if(jsonResponse.has("RouteDetails")) {
                            JSONObject routeDetails = jsonResponse.getJSONObject("RouteDetails");
                            String area = routeDetails.optString("Area", "");
                            String colour = routeDetails.optString("Colour", "");
                            String grades = routeDetails.optString("Grades", "");
                            String setDate = routeDetails.optString("SetDate", "");
                            String setter = routeDetails.optString("Setter", "");
                            int upvotes = routeDetails.optInt("Upvotes", 0);
                            String imageUrl = routeDetails.optString("ImageUrl","");

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