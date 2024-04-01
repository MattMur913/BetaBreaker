package com.example.betabreaker.BurgerToppings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betabreaker.Classes.AdapterRoutes;
import com.example.betabreaker.Classes.ClsCentre;
import com.example.betabreaker.Classes.ClsRoutes;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentLettaceBinding;

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

public class Lettace extends Fragment {
    private FragmentLettaceBinding binding;
    private ClsCentre centreFav;
    private List<ClsRoutes> routesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterRoutes adapter;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLettaceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String favouriteCentre = preferences.getString("adminOf","");

        if(favouriteCentre.isEmpty()){
            view = LayoutInflater.from(requireContext()).inflate(R.layout.no_favourite, null);
            ViewGroup parent = (ViewGroup) binding.getRoot().getParent();
            int index = parent.indexOfChild(binding.getRoot());
            parent.removeView(binding.getRoot());
            parent.addView(view, index);
        }else{
            fetchSingleCentre(favouriteCentre);

            TextView txtLabel = binding.textView2;
            ImageView imgLogo = binding.imageView2;
            recyclerView = view.findViewById(R.id.lettaceRecy);
            progressBar = view.findViewById(R.id.progressBar1);

            // Initially show the progress bar and hide the RecyclerView
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

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
                            ClsRoutes route = new ClsRoutes(area, colour, grades, setDate, setter, upvotes, imageUrl);
                            routesList.add(route);
                        }

                        // Create a ClsCentre object and add it to the list
                        centreFav = new ClsCentre(id, name, address, description, "", "", "", logoid, routes);
                        Log.d("SingleCentre2", String.valueOf(centreFav.getCentreName()));


                        requireActivity().runOnUiThread(() -> {
                            // Update RecyclerView and hide progress bar
                            adapter = new AdapterRoutes(routesList, centreFav.getIdCentre(), requireActivity());
                            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                            recyclerView.setAdapter(adapter);

                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        });

                    } catch (JSONException e) {
                        Log.d("SingleCentre", "JSONException: " + e.getMessage());
                    }
                }
            }

        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}