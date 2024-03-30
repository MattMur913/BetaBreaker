package com.example.betabreaker.Frags;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betabreaker.Classes.AdapterRoutes;
import com.example.betabreaker.Classes.ClsRoutes;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.R;

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

public class FragDisplayRoutes extends Fragment {

    private List<ClsRoutes> routesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterRoutes adapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("SingleCentre4", "onCreateView: ");
        return inflater.inflate(R.layout.fragment_display_routes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("SingleCentre4", "onCreateView: 1 ");
        recyclerView = view.findViewById(R.id.dsRRec);
        progressBar = view.findViewById(R.id.progressBar);

        // Initially show the progress bar and hide the RecyclerView
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Log.d("SingleCentre4", "onCreateView: 2");
        Bundle bundle = getArguments();
        if (bundle != null) {
            String centreID = (String) bundle.getSerializable("centreID");
            getRoutesFromCentre(centreID);
        } else {
            Log.e("FragDisplayRoutes", "Bundle is null");
        }
    }

    private void getRoutesFromCentre(String centreID) {
        Log.d("SingleCentre4", "onCreateView: 3");
        OkHttpClient client = new OkHttpClient();
        String getRoutes = GlobalUrl.getRoutes.replace("{cID}", centreID);
        Request request = new Request.Builder()
                .url(getRoutes)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("FragDisplayRoutes", "Failed to fetch routes: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONArray routeDetailsArray = new JSONArray(responseData);
                        Log.d("SingleCentre4", "onCreateView:4 ");

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

                        requireActivity().runOnUiThread(() -> {
                            // Update RecyclerView and hide progress bar
                            adapter = new AdapterRoutes(routesList, centreID, requireActivity());
                            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                            recyclerView.setAdapter(adapter);

                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        });
                        Log.d("SingleCentre4", "onCreateView: 5");
                    } catch (JSONException e) {
                        Log.e("FragDisplayRoutes", "Error parsing JSON: " + e.getMessage());
                    }
                } else {
                    Log.e("FragDisplayRoutes", "Failed to fetch routes: " + response.code() + " " + response.message());
                }
            }
        });
    }
}
