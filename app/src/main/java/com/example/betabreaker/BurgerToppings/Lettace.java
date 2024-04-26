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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

public class Lettace extends Fragment   {
    private FragmentLettaceBinding binding;
    private ClsCentre centreFav;
    private List<ClsRoutes> routesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterRoutes adapter;
    private FragmentContainerView msFav;
    private ProgressBar progressBar;
    private CardView cdView;
    private TextView txtLabel;
    private ImageView imgLogo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

            binding = FragmentLettaceBinding.inflate(inflater, container, false);
            return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String favouriteCentre = preferences.getString("favCent", "");
        if (favouriteCentre.isEmpty()){favouriteCentre = preferences.getString("adminOf","");}
        txtLabel = binding.textView2;
        imgLogo = binding.imageView2;
        cdView = binding.cardView;

        recyclerView = binding.lettaceRecy;
        progressBar = binding.progressBar1;

        txtLabel.setVisibility(View.INVISIBLE); // Hide textView2
        imgLogo.setVisibility(View.INVISIBLE); // Hide imageView2

        recyclerView.setVisibility(View.INVISIBLE); // Hide lettaceRecy RecyclerView
        cdView.setVisibility(View.INVISIBLE); // Hide cardView
        progressBar.setVisibility(View.VISIBLE); // Show progressBar1

        fetchSingleCentre(favouriteCentre);
    }

    private void fetchSingleCentre(String centreID){
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
                        String contact = jsonResponse.getString("contactNumber");
                        String email = jsonResponse.getString("email");
                        String address = jsonResponse.getString("Address");
                        String website = jsonResponse.getString("website");
                        String description = jsonResponse.getString("description");
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
                            String routeID = routeObject.optString("routeID", "");
                            ClsRoutes route = new ClsRoutes(area, colour, grades, setDate, setter, upvotes, imageUrl, routeID);
                            routesList.add(route);
                        }

                        // Create a ClsCentre object and add it to the list
                        centreFav = new ClsCentre(id, name, address, description, email, contact, website, logoid, routesList);
                        Log.d("SingleCentre2", String.valueOf(centreFav.getCentreName()));

                        requireActivity().runOnUiThread(() -> {
                            // Update UI components with centreFav data
                            txtLabel.setText(centreFav.getCentreName());
                            Log.d("Testing", GlobalUrl.imageUrl+centreFav.getlogo());
                            Glide.with(imgLogo.getContext()).load(GlobalUrl.imageUrl + centreFav.getlogo()).apply(RequestOptions.placeholderOf(R.drawable.placeholder_image)).into(imgLogo);

                            // Update RecyclerView and hide progress bar
                            adapter = new AdapterRoutes(routesList, centreFav.getIdCentre(), requireContext(),  Lettace.this);
                            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                            recyclerView.setAdapter(adapter);

                            txtLabel.setVisibility(View.VISIBLE); // Show textView2
                            imgLogo.setVisibility(View.VISIBLE); // Show imageView2

                            recyclerView.setVisibility(View.VISIBLE); // Show lettaceRecy RecyclerView
                            cdView.setVisibility(View.VISIBLE); // Show cardView
                            progressBar.setVisibility(View.GONE); // Hide progressBar1


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
