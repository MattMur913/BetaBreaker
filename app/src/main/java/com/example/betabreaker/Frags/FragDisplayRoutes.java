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

import com.example.betabreaker.Classes.AdapterEditRoutes;
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

    private final List<ClsRoutes> routesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display_routes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.dsRRec);
        Bundle bundle = getArguments();
        progressBar = view.findViewById(R.id.progressBar);

        // Initially show the progress bar and hide the RecyclerView
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        if (bundle != null) {
            String centreID = (String) bundle.getSerializable("centreID");
            String fragger = (String) bundle.getSerializable("fragger");

            //gets the routes from the API
            getRoutesFromCentre(centreID,fragger);
        }

    }

    private void getRoutesFromCentre(String centreID, String fragger) {
        OkHttpClient client = new OkHttpClient();
        routesList.clear();
        String getRoutes = GlobalUrl.getRoutes.replace("{cID}", centreID);
        Request request = new Request.Builder()
                .url(getRoutes)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TestError", "Failed to fetch routes: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONArray routeDetailsArray = new JSONArray(responseData);
                        //cycles through the array
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

                            ClsRoutes route = new ClsRoutes(area, colour, grades, setDate, setter, upvotes, imageUrl,routeID);
                            routesList.add(route);
                        }

                        requireActivity().runOnUiThread(() -> {
                            //This will check if the user is admin or not
                            if(fragger.equals("Admin")){ AdapterEditRoutes adapter = new AdapterEditRoutes(routesList, centreID, FragDisplayRoutes.this);
                                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                                recyclerView.setAdapter(adapter);}
                            else{ AdapterRoutes adapter = new AdapterRoutes(routesList, centreID, requireContext(), FragDisplayRoutes.this);
                                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                                recyclerView.setAdapter(adapter);}

                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        });
                    } catch (JSONException e) {
                        Log.e("TestError", "Error parsing JSON: " + e.getMessage());
                    }
                } else {
                    Log.e("TestError", "Failed to fetch routes: " + response.code() + " " + response.message());
                }
            }
        });
    }
}
