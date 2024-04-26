package com.example.betabreaker.Frags;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.betabreaker.Classes.AdapterComments;
import com.example.betabreaker.Classes.ClsComment;
import com.example.betabreaker.Classes.ClsRoutes;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentSpecRouteBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FragSpecRoute extends Fragment {

    private FragmentSpecRouteBinding binding;
    private List<ClsComment> commentList = new ArrayList<>();
    private AdapterComments adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSpecRouteBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        TextView tvArea;
        TextView tvColour;
        TextView tvDate;
        TextView tvGrade;
        TextView tvSetter;
        TextView tvVotes;
        ImageView ivRoute;
        if (bundle != null) {
            ClsRoutes routes = (ClsRoutes) bundle.getSerializable("viewRoute");

            // Initialize views
            tvArea = binding.spRArea;
            tvColour = binding.spRColour;
            tvDate = binding.spRDate;
            tvGrade = binding.spRGrade;
            tvSetter = binding.spRSetter;
            tvVotes = binding.spRVotes;
            ivRoute = binding.spRClimb;
            Button btnAddComment = binding.btnComment;
            EditText edComment = binding.addComment;

            // Display the data in the TextViews
            if (routes != null) {
                tvArea.setText(routes.getArea());
                tvColour.setText(routes.getColour());
                tvGrade.setText(routes.getGrades());
                tvSetter.setText(routes.getSetter());
                tvVotes.setText(routes.getUpvotes());
                tvDate.setText(routes.getSetDate());
                Glide.with(view.getContext()).load(GlobalUrl.imageUrl + routes.getImage()).apply(RequestOptions.placeholderOf(R.drawable.placeholder_image))
                        .into(ivRoute);
            }

            btnAddComment.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                    String username = preferences.getString("username","");
                    String comment = edComment.getText().toString();
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("comment", comment)
                            .addFormDataPart("username", username )
                            .addFormDataPart("routeID", routes.getID())
                            .build();
                    Request request = new Request.Builder()
                            .url(GlobalUrl.addComment)
                            .post(requestBody)
                            .build();
                    OkHttpClient client = new OkHttpClient();

                    // Send the request asynchronously
                    client.newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            // Handle the response here
                            if (response.isSuccessful()) {
                                String responseData = response.body().string();
                                Log.d("FragAddRoutes", "onResponse: " + responseData);
                            } else {
                                Log.d("FragAddRoutes", "onResponse: Failed");
                            }
                        }
                        @Override
                        public void onFailure(Call call, IOException e) {
                            // Request failed
                            // Log error or show appropriate message
                        }
                    });
                            }
            });

             recyclerView = view.findViewById(R.id.crvLayout);
            getComments(routes.getID());
        }
    }

    private void getComments(String routeID){
        OkHttpClient client = new OkHttpClient();
        commentList.clear();
        String getComments = GlobalUrl.getComments.replace("{rid}",routeID);
        Request request = new Request.Builder()
                .url(getComments)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("FragDisplayComments", "Failed to fetch routes: " + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONObject resultSets = jsonObject.getJSONObject("ResultSets");
                        JSONArray commentTable = resultSets.getJSONArray("Table1");
                        for (int i = 0; i < commentTable.length(); i++) {
                            JSONObject commentData = commentTable.getJSONObject(i);
                            String comment = commentData.getString("comment");
                            String Username = commentData.getString("Username");
                            ClsComment newComment = new ClsComment(comment, Username);
                            commentList.add(newComment);
                        }

                        requireActivity().runOnUiThread(() -> {
                            // Update RecyclerView and hide progress bar
                            adapter = new AdapterComments();
                            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                            recyclerView.setAdapter(adapter);

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