package com.example.betabreaker.Editables;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.betabreaker.Classes.ClsCentre;
import com.example.betabreaker.Classes.ClsRoutes;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.Frags.FragDisplayRoutes;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentEditCentreBinding;

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


public class FragEditCentre extends Fragment {

    private FragmentEditCentreBinding binding;

    private ClsCentre edtCentre;

    private List<ClsRoutes> routesList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditCentreBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        final Button btnRoutes = binding.vewRoutes;
        TextView textViewCentreId = binding.spCName;
        TextView textViewCentreName = binding.spCAddress;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String centreID = preferences.getString("centreID", "");

        fetchSingleCentre(centreID);

        if (bundle != null) {
            ClsCentre centre = (ClsCentre) bundle.getSerializable("centre");
            if (centre != null) {
                textViewCentreId.setText(centre.getIdCentre());
                textViewCentreName.setText(centre.getCentreName());
            }

            btnRoutes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragDisplayRoutes fragment = new FragDisplayRoutes();
                    Bundle bundle = new Bundle();
                    if (centre != null) {
                        List<ClsRoutes> routes = centre.getRoutes();

                        bundle.putSerializable("routes", (Serializable) routes);
                        bundle.putSerializable("centreID", centre.getIdCentre());
                        fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragContent, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            });
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
                        edtCentre = new ClsCentre(id, name, address, description, "", "", "", logoid, routes);
                        Log.d("SingleCentre2", String.valueOf(edtCentre.getCentreName()));

                        requireActivity().runOnUiThread(() -> {
                            ImageView spCLogoview = binding.spCLogoview;
                            // Update UI components with centreFav data
                            Log.d("Testing", GlobalUrl.imageUrl+edtCentre.getlogo());
                            Glide.with(spCLogoview.getContext()).load(GlobalUrl.imageUrl + edtCentre.getlogo()).apply(RequestOptions.placeholderOf(R.drawable.placeholder_image)).into(spCLogoview);


                        });

                    } catch (JSONException e) {
                        Log.d("SingleCentre", "JSONException: " + e.getMessage());
                    }
                }
            }
        });
    }
}