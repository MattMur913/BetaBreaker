package com.example.betabreaker.Frags;

import android.app.Activity;
import android.content.Context;
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
    private final List<ClsComment> commentList = new ArrayList<>();
    private AdapterComments adapter;
    private RecyclerView recyclerView;
    private Activity mActivity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSpecRouteBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Creates each editable view
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

            // Display the data in the Views
            if (routes != null) {
                tvArea.setText(routes.getArea());
                tvColour.setText(routes.getColour());
                tvGrade.setText(routes.getGrades());
                tvSetter.setText(routes.getSetter());
                tvVotes.setText(routes.getUpvotes());
                tvDate.setText(routes.getSetDate());
                Glide.with(view.getContext()).load(GlobalUrl.imageUrl + routes.getImage()).apply(RequestOptions.placeholderOf(R.drawable.placeholder_image))
                        .into(ivRoute);

                //Gets the comments
                recyclerView = view.findViewById(R.id.crvLayout);
                getComments(routes.getID());
            }

            //Add comment functional
            btnAddComment.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //gets user data and all information to be passed along
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                    String username = preferences.getString("username","");
                    String comment = edComment.getText().toString();

                    //creates request body
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("comment", comment)
                            .addFormDataPart("username", username )
                            .addFormDataPart("routeID", routes.getID())
                            .build();
                    //creates the request
                    Request request = new Request.Builder()
                            .url(GlobalUrl.addComment)
                            .post(requestBody)
                            .build();
                    OkHttpClient client = new OkHttpClient();

                    // Send the request
                    client.newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            if (response.isSuccessful()) {
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //calls the display comments function again
                                        getComments(routes.getID());

                                    }
                                });
                            } else {
                                Log.d("TestError", "onResponse: Failed");
                            }
                        }
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }
                    });
                            }
            });


        }
    }

    //Get comments function
    private void getComments(String routeID){
        //ensures the comments list is empty
        commentList.clear();

        //creates the request
        OkHttpClient client = new OkHttpClient();
        String getComments = GlobalUrl.getComments.replace("{rid}",routeID);
        Request request = new Request.Builder()
                .url(getComments)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TestError", "Failed to fetch routes: " + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        JSONArray commentTable = jsonObject.getJSONArray("Table1");
                        for (int i = 0; i < commentTable.length(); i++) {
                            JSONObject commentData = commentTable.getJSONObject(i);
                            String comment = commentData.getString("comment");
                            String username = commentData.getString("Username");
                            ClsComment newComment = new ClsComment(username, comment);
                            commentList.add(newComment);
                        }
                        //This ensures the comments function is listed to the activity, there can be issues otherwise
                        if (mActivity != null) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter = new AdapterComments(commentList);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                                    recyclerView.setAdapter(adapter);
                                }
                            });
                        }
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