package com.example.betabreaker.ConvertedActs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
        Log.d("SingleCentre4", "onCreateView: 0 ");
        return inflater.inflate(R.layout.fragment_display_centres, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize RecyclerView and adapter
        Log.d("SingleCentre4", "onCreateView: 1 ");
        view.findViewById(R.id.dsCRec).setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.dsCRec);



        // Fetch data from Logic App
        fetchDataFromLogicApp();
        EditText searchEditText = view.findViewById(R.id.searchEditText);
        searchEditText.setVisibility(View.VISIBLE);
        Log.d("SingleCentre4", "onCreateView: 2 ");
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchEditText.setHint("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchEditText.setHint("");
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchEditText.setHint("");
            }
        });}

    private void fetchDataFromLogicApp() {
        // Logic App endpoint URL
        Log.d("SingleCentre4", "onCreateView: 3 ");
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
                            Log.d("SingleCentre4", "onCreateView: " +centre.getCentreName());
                            centreList.add(centre);

                            setAdapterValues();
                        }


                        requireActivity().runOnUiThread(() -> {

                                adapter = new AdapterCentres(centreList, requireContext(),FragDisplayCentres.this);

                                // Set layout manager and adapter
                                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                                recyclerView.setAdapter(adapter);

                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setAdapterValues(){

    }

}
