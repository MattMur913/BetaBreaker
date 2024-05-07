package com.example.betabreaker.Editables;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

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
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FragEditSpecRoute extends Fragment {

    private FragmentEditSpecRouteBinding binding;
    private ClsRoutes routes;

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
        ImageView ivRoute;
        Button btnDelete = binding.rotDelete;
        Button btnUpdate= binding.rotUpdate;
        if (bundle != null) {
            routes = (ClsRoutes) bundle.getSerializable("viewRoute");

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

                        String updURL = GlobalUrl.updRoutesUrl.replace("{cid}", centreID);
                        updURL = updURL.replace("{rid}",routes.getID() );
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
                            jsonObject.put("imageUrl", routes.getImage());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Convert JSON object to string
                        OkHttpClient client = new OkHttpClient();
                        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                .addFormDataPart("area", area)
                                .addFormDataPart("colour", colour)
                                .addFormDataPart("date", date)
                                .addFormDataPart("grade", grade)
                                .addFormDataPart("setter", setter)
                                .addFormDataPart("imageUrl", routes.getImage())
                                .build();


                        // Create a request
                        Request request = new Request.Builder()
                                .url(updURL)
                                .post(body)
                                .build();

                        // Execute the request asynchronously
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
                                        Bundle bundle = new Bundle();
                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                                        String centreID = preferences.getString("adminOf", "");
                                        bundle.putSerializable("centreID",centreID);
                                        bundle.putSerializable("fragger", "Admin");
                                        NavController navController = navHostFragment.getNavController();
                                        navController.popBackStack();
                                        navController.navigate(R.id.go_all_routes, bundle);
                                        Toast.makeText(getContext(), "An error occurred please try again later", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
                                        Bundle bundle = new Bundle();
                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                                        String centreID = preferences.getString("adminOf", "");
                                        bundle.putSerializable("centreID",centreID);
                                        bundle.putSerializable("fragger", "Admin");
                                        NavController navController = navHostFragment.getNavController();
                                        navController.popBackStack();
                                        navController.navigate(R.id.go_all_routes, bundle);
                                        Toast.makeText(getContext(), "Route details updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
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
                        deleteUrl = deleteUrl.replace("{rID}",routes.getID() );
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
                                Toast.makeText(getContext(), "AN ERROR HAS OCCURRED PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
                                        NavController navController = navHostFragment.getNavController();
                                        Bundle bundle = new Bundle();
                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                                        String centreID = preferences.getString("adminOf", "");
                                        bundle.putSerializable("centreID",centreID);
                                        bundle.putSerializable("fragger", "Admin");
                                        navController.popBackStack();
                                        navController.navigate(R.id.go_all_routes,bundle);
                                        Toast.makeText(getContext(), "Route has been deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });

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