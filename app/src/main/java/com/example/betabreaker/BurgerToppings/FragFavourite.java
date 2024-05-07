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
import com.example.betabreaker.databinding.FragmentFavouriteBinding;

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

public class FragFavourite extends Fragment   {
    private FragmentFavouriteBinding binding;
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

            binding = FragmentFavouriteBinding.inflate(inflater, container, false);
            return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        cdView = binding.cardViewLettace;
        recyclerView = binding.lettaceRecy;
        progressBar = binding.progressBar1;

        txtLabel = binding.textView2;
        imgLogo = binding.imageView2;
        txtLabel.setVisibility(View.INVISIBLE);
        imgLogo.setVisibility(View.INVISIBLE);
        cdView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        String favouriteCentre = preferences.getString("favCent", "");
        if (favouriteCentre.isEmpty()){
            favouriteCentre = preferences.getString("adminOf","");
            if(favouriteCentre.isEmpty()){
                //TODO: add a toast
            }else{

                fetchSingleCentre(favouriteCentre);
            }

        }else{ fetchSingleCentre(favouriteCentre);}
    }

    private void fetchSingleCentre(String centreID){
        String logicAppUrl = GlobalUrl.getSinCentreUrl.replace("{id}",centreID);
        routesList.clear();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(logicAppUrl)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
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


                        centreFav = new ClsCentre(id, name, address, description, email, contact, website, logoid, routesList);

                        requireActivity().runOnUiThread(() -> {
                            txtLabel.setText(centreFav.getCentreName());
                            Glide.with(imgLogo.getContext()).load(GlobalUrl.imageUrl + centreFav.getlogo()).apply(RequestOptions.placeholderOf(R.drawable.placeholder_image)).into(imgLogo);

                            adapter = new AdapterRoutes(routesList, centreFav.getIdCentre(), requireContext(),  FragFavourite.this);
                            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                            recyclerView.setAdapter(adapter);

                            txtLabel.setVisibility(View.VISIBLE);
                            imgLogo.setVisibility(View.VISIBLE);

                            recyclerView.setVisibility(View.VISIBLE);
                            cdView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);


                        });

                    } catch (JSONException e) {
                        Log.d("TestError", "JSONException: " + e.getMessage());
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
