package com.example.betabreaker.Frags;

import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.betabreaker.Classes.ClsRoutes;
import com.example.betabreaker.Classes.ConfirmationDialog;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentEditSpecRouteBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FragEditSpecRoute extends Fragment {

    private FragmentEditSpecRouteBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditSpecRouteBinding.inflate(inflater, container, false);
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
        Button btnDelete = binding.rotDelete;
        Button btnUpdate= binding.rotUpdate;
        if (bundle != null) {
            ClsRoutes routes = (ClsRoutes) bundle.getSerializable("viewRoute");

            // Initialize views
            tvArea = binding.edArea;
            tvColour = binding.edColour;
            tvDate = binding.edDate;
            tvGrade = binding.edGrade;
            tvSetter = binding.edSetter;
            ivRoute = binding.spRClimb;

            // Display the data in the TextViews
            if (routes != null) {
                tvArea.setText(routes.getArea());
                tvColour.setText(routes.getColour());
                tvGrade.setText(routes.getGrades());
                tvSetter.setText(routes.getSetter());
                tvDate.setText(routes.getSetDate());
                Glide.with(view.getContext()).load(GlobalUrl.imageUrl + routes.getImage()).apply(RequestOptions.placeholderOf(R.drawable.placeholder_image))
                        .into(ivRoute);
            }
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmationDialog.showConfirmationDialog(getContext(), "Are you sure you want update this route?", new ConfirmationDialog.ConfirmationListener() {
                    @Override
                    public void onConfirm() {
                        ClsRoutes routes = (ClsRoutes) bundle.getSerializable("viewRoute");
                        String centreID = (String) bundle.getSerializable("centreID");

                        String updURL = GlobalUrl.updRoutesUrl.replace("{cID}", centreID);
                        updURL = updURL.replace("{rID}",routes.getImage() );
                        // Get values from EditText fields
                        String area = binding.edArea.getText().toString();
                        String colour = binding.edColour.getText().toString();
                        String grade = binding.edGrade.getText().toString();
                        String date = binding.edDate.getText().toString();
                        String setter = binding.edSetter.getText().toString();

                        // Create a JSON object to hold the values
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("Area", area);
                            jsonObject.put("Colour", colour);
                            jsonObject.put("Grades", grade);
                            jsonObject.put("SetDate", date);
                            jsonObject.put("Setter", setter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Convert JSON object to string
                        String routeData = jsonObject.toString();
                        OkHttpClient client = new OkHttpClient();
                        RequestBody body = RequestBody.create(MediaType.parse("application/json"), routeData);


                        // Create a request
                        Request request = new Request.Builder()
                                .url(updURL)
                                .put(body)
                                .build();

                        // Execute the request asynchronously
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("SingleCentre", "Not updated");

                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.d("SingleCentre", "Updated");
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                fragmentManager.popBackStack();


                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        // Handle cancellation action
                    }
                });

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                ConfirmationDialog.showConfirmationDialog(getContext(), "Are you sure you want delete this route?", new ConfirmationDialog.ConfirmationListener() {
                    @Override
                    public void onConfirm() {
                        ClsRoutes routes = (ClsRoutes) bundle.getSerializable("viewRoute");
                        String centreID = (String) bundle.getSerializable("centreID");

                        String deleteUrl = GlobalUrl.delRouteUrl.replace("{cID}", centreID);
                        deleteUrl = deleteUrl.replace("{rID}",routes.getImage() );
                        OkHttpClient client = new OkHttpClient();

                        // Create a request
                        Request request = new Request.Builder()
                                .url(deleteUrl)
                                .delete()
                                .build();

                        // Execute the request asynchronously
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("SingleCentre", "Not Deleted");
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                fragmentManager.popBackStack();
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.d("SingleCentre", "Deleted");
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                fragmentManager.popBackStack();

                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        // Handle cancellation action
                    }
                });

            }
         });
    }
}