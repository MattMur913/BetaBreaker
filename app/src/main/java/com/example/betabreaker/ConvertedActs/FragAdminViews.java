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
import com.example.betabreaker.Frags.FragAddRoute;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentAdminViewsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
        hardCersion(centreID);
        final Button btnAddRoute = binding.addRoute;


        btnAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager(); // Use getChildFragmentManager() here
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
            ClsCentre centre= new ClsCentre(id, name, address, description, email, contact, website, logoid, routes);

            // Add the centre to the list
            centreList.add(centre);

            // Further processing...
        } catch (JSONException e) {
            Log.d("SingleCentre", "JSONException: " + e.getMessage());
        }
    }
}
