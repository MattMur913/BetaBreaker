package com.example.betabreaker.ConvertedActs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betabreaker.Classes.AdapterCentres;
import com.example.betabreaker.Classes.ClsCentre;
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

public class FragDisplayCentres extends Fragment {

    private List<ClsCentre> centreList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterCentres adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_display_centres, container, false);

        // Initialize RecyclerView and adapter
        recyclerView = rootView.findViewById(R.id.dsCRec);
        adapter = new AdapterCentres(centreList);

        // Set layout manager and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Fetch data from Logic App
        fetchDataFromLogicApp();
        EditText searchEditText = rootView.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No implementation needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No implementation needed
            }
        });


        return rootView;
    }

    private void fetchDataFromLogicApp() {
        // Logic App endpoint URL
        centreList.clear();
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
                            String contact = jsonObject.getString("contactNumber");
                            String email = jsonObject.getString("email");
                            String address = jsonObject.getString("description");
                            String website = jsonObject.getString("website");
                            String description = jsonObject.getString("Address");
                            String logoid = jsonObject.getString("logoName");
                            JSONArray routeDetailsArray = jsonObject.getJSONArray("RouteDetails");
                            List<ClsRoutes> routes = new ArrayList<>();
                            for (int j = 0; j < routeDetailsArray.length(); j++) {
                                JSONObject routeObject = routeDetailsArray.getJSONObject(j);
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
                            ClsCentre centre = new ClsCentre(id, name, address, description, email, contact, website, logoid, routes);
                            centreList.add(centre);
                        }

                        // Notify adapter of data changes on the main thread
                        requireActivity().runOnUiThread(new Runnable() {
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


}
