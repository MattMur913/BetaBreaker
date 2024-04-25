package com.example.betabreaker.Editables;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.betabreaker.Classes.ClsCentre;
import com.example.betabreaker.Classes.ClsRoutes;
import com.example.betabreaker.Classes.ConfirmationDialog;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.Frags.FragAddRoute;
import com.example.betabreaker.Frags.FragDisplayRoutes;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentEditCentreBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FragEditCentre extends Fragment {

    private FragmentEditCentreBinding binding;
    private Uri imageURI;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ClsCentre edtCentre;
    private List<ClsRoutes> routesList = new ArrayList<>();
    private ImageView imgLogo;
    private EditText txtName;
    private EditText txtEmail;
    private EditText txtAddress;
    private EditText txtContct;
    private EditText txtWebsite;
    private Button btnRoutes;
    private Button btnReset;
    private Button btnUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditCentreBinding.inflate(inflater, container, false);
        imgLogo = binding.spCLogoview;
        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnRoutes = binding.vewRoutes;
        txtName = binding.edName;
        txtAddress = binding.edAddress;
        txtEmail = binding.edEmail;
        txtContct = binding.edNumber;
        txtWebsite = binding.edWebsite;
        btnReset = binding.rotReset;
        btnUpdate = binding.rotUpdate;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String centreID = preferences.getString("adminOf", "");

        fetchSingleCentre(centreID);
        Button btnAddRoute = binding.addRoute;

        btnAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragAddRoute fragment = new FragAddRoute();
                Bundle bundle = new Bundle();
                bundle.putSerializable("centre", centreID);
                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragContent, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        btnRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCentre != null) {
                    Log.d("CHECKING EDITCENTRE", "onClick: CHECKER");

                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragDisplayRoutes fragment = new FragDisplayRoutes();
                    Bundle bundle = new Bundle();
                    List<ClsRoutes> routes = edtCentre.getRoutes();

                    fragmentTransaction.remove(FragEditCentre.this);
                    bundle.putSerializable("routes", (Serializable) routes);
                    bundle.putSerializable("centreID", edtCentre.getIdCentre());
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragContent, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    // Handle the case when edtCentre is null or not initialized yet
                    Log.e("FragEditCentre", "edtCentre is null or not initialized yet");
                }
            }
        });


        btnReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Glide.with(imgLogo.getContext()).load(GlobalUrl.imageUrl + edtCentre.getlogo()).apply(RequestOptions.placeholderOf(R.drawable.placeholder_image)).into(imgLogo);
                txtName.setText(edtCentre.getCentreName());
                txtAddress.setText(edtCentre.getAddress());
                txtContct.setText(edtCentre.getNumber());
                txtEmail.setText(edtCentre.getEmail());
                txtWebsite.setText(edtCentre.getWebsite());
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmationDialog.showConfirmationDialog(getContext(), "Are you sure you want update this route?", new ConfirmationDialog.ConfirmationListener() {
                    @Override
                    public void onConfirm() {
                        String updURL = GlobalUrl.updCentreUrl.replace("{id}", centreID);
                        // Get values from EditText fields
                        String Name = txtName.getText().toString();
                        String Address = txtAddress.getText().toString();
                        String Email = txtEmail.getText().toString();
                        String Contact = txtContct.getText().toString();
                        String Website = txtWebsite.getText().toString();

                        // Create a MultipartBody.Builder to construct the request body
                        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("centreName", Name)
                                .addFormDataPart("address", Address)
                                .addFormDataPart("email", Email)
                                .addFormDataPart("contactNumber", Contact)
                                .addFormDataPart("website", Website);

                        // Add image file if selected
                        if (imageURI != null) {
                            File logoPath = null;
                            try {
                                logoPath = createTemporaryFileFromUri(imageURI);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            requestBodyBuilder.addFormDataPart("file", "logo.jpg",
                                    RequestBody.create(MediaType.parse("image/*"), logoPath));
                            requestBodyBuilder.addFormDataPart("newImage","0");
                        }else{
                            requestBodyBuilder.addFormDataPart("newImage","1");
                        }

                        // Build the request body
                        RequestBody requestBody = requestBodyBuilder.build();

                        // Create a request
                        Request request = new Request.Builder()
                                .url(updURL)
                                .post(requestBody)
                                .build();

                        // Execute the request asynchronously
                        OkHttpClient client = new OkHttpClient();
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

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

    }


    private void fetchSingleCentre(String centreID){
        Log.d("SingleCentre", "Getting centre");
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
                        String contact = jsonResponse.getString("contactNumber");
                        String email = jsonResponse.getString("email");
                        String address = jsonResponse.getString("description");
                        String website = jsonResponse.getString("website");
                        String description = jsonResponse.getString("Address");
                        String logoid = jsonResponse.getString("logoName");

                        // Check if "RouteDetails" exists in the JSON
                        //List<ClsRoutes> routes = new ArrayList<>();
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
                        edtCentre = new ClsCentre(id, name, address, description, email, contact, website, logoid, routesList);
                        Log.d("SingleCentre2", String.valueOf(edtCentre.getRoutes()));

                        requireActivity().runOnUiThread(() -> {
                            // Update UI components with centreFav data
                            Log.d("Testing", GlobalUrl.imageUrl+edtCentre.getlogo());
                            Glide.with(imgLogo.getContext()).load(GlobalUrl.imageUrl + edtCentre.getlogo()).apply(RequestOptions.placeholderOf(R.drawable.placeholder_image)).into(imgLogo);
                            txtName.setText(edtCentre.getCentreName());
                            txtAddress.setText(edtCentre.getAddress());
                            txtContct.setText(edtCentre.getNumber());
                            txtEmail.setText(edtCentre.getEmail());
                            txtWebsite.setText(edtCentre.getWebsite());
                            btnRoutes.setVisibility(View.VISIBLE);
                            btnUpdate.setVisibility(View.VISIBLE);
                            btnReset.setVisibility(View.VISIBLE);


                        });

                    } catch (JSONException e) {
                        Log.d("SingleCentre", "JSONException: " + e.getMessage());
                    }
                }
            }
        });
    }
    private File createTemporaryFileFromUri(Uri uri) throws IOException {
        InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            // Handle error
            return null;
        }

        File tempFile = File.createTempFile("temp", null, getActivity().getCacheDir());
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[4 * 1024]; // Adjust buffer size as needed
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        return tempFile;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            // Display the selected image
            imageURI = imageUri;
            imgLogo.setImageURI(imageUri);
        }
    }
}