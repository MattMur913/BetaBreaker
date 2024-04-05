package com.example.betabreaker.ConvertedActs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.betabreaker.Classes.ClsCentre;
import com.example.betabreaker.Classes.ClsRoutes;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.Frags.FragAddRoute;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentAdminViewsBinding;

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

public class FragAdminViews extends Fragment {

    private List<ClsCentre> centreList = new ArrayList<>();
    private FragmentAdminViewsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminViewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String centreID = preferences.getString("adminOf", "");
        //fetchSingleCentre(centreID);
        final Button btnAddRoute = binding.addRoute;


        btnAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager(); // Use getChildFragmentManager() here
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragAddRoute fragment = new FragAddRoute();
                Bundle bundle = new Bundle();
                bundle.putSerializable("centre", centreID);
                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragContent, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void fetchSingleCentre(String centreID) {
        Log.d("SingleCentre", "Getting centre");
        String logicAppUrl = GlobalUrl.getSinCentreUrl.replace("{id}", centreID);
        Log.d("SingleCentre2", logicAppUrl);

        // Hardcoded JSON response for testing
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
                        // Parse the JSON response
                        String responseData = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseData);
                        String id = jsonResponse.getString("id");
                        String name = jsonResponse.getString("centreName");
                        String contact = jsonResponse.getString("contactNumber");
                        String email = jsonResponse.getString("email");
                        String address = jsonResponse.getString("description");
                        String website = jsonResponse.getString("website");
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
                        ClsCentre centre = new ClsCentre(id, name, address, description, email, contact, website, logoid, routes);

                        // Add the centre to the list
                        centreList.add(centre);


                        // Further processing...
                    } catch (JSONException e) {
                        Log.d("SingleCentre", "JSONException: " + e.getMessage());
                    }
                }
            }
        });
    }
}
