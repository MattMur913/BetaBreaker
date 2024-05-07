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
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.betabreaker.Classes.ClsCentre;
import com.example.betabreaker.Classes.ClsRoutes;
import com.example.betabreaker.Classes.ConfirmationDialog;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentEditCentreBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private final List<ClsRoutes> routesList = new ArrayList<>();
    private ImageView imgLogo;
    private EditText txtName;
    private EditText txtEmail;
    private EditText txtAddress;
    private EditText txtContact;
    private EditText txtWebsite;
    private EditText txtDesc;
    private Button btnRoutes;
    private Button btnReset;
    private Button btnUpdate;
    private Group grpDisplay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditCentreBinding.inflate(inflater, container, false);
        imgLogo = binding.spCLogoview;
        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //creates each interaactable
        btnRoutes = binding.vewRoutes;
        txtName = binding.edName;
        txtAddress = binding.edAddress;
        txtEmail = binding.edEmail;
        txtDesc = binding.edDesc;
        txtContact = binding.edNumber;
        txtWebsite = binding.edWebsite;
        btnReset = binding.rotReset;
        btnUpdate = binding.rotUpdate;

        grpDisplay = binding.editCentreGroup;
        grpDisplay.setVisibility(View.INVISIBLE);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String centreID = preferences.getString("adminOf", "");

        //gets centre data
        fetchSingleCentre(centreID);
        Button btnAddRoute = binding.addRoute;

        //adds an on click listener
        btnAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("centre", centreID);

                NavHostFragment.findNavController(FragEditCentre.this)
                        .navigate(R.id.go_add_route,bundle);
            }
        });
        btnRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCentre != null) {
                    //creates bundle and then sends it
                    Bundle bundle = new Bundle();
                    //List<ClsRoutes> routes = edtCentre.getRoutes();
                    //bundle.putSerializable("routes", (Serializable) routes);
                    bundle.putSerializable("centreID", edtCentre.getIdCentre());
                    bundle.putSerializable("fragger", "Admin");
                    NavHostFragment.findNavController(FragEditCentre.this)
                            .navigate(R.id.go_all_routes,bundle);

                }
            }
        });

        //reset on click listener
        btnReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //resets the current screen
                Glide.with(imgLogo.getContext()).load(GlobalUrl.imageUrl + edtCentre.getlogo()).apply(RequestOptions.placeholderOf(R.drawable.placeholder_image)).into(imgLogo);
                txtName.setText(edtCentre.getCentreName());
                txtAddress.setText(edtCentre.getAddress());
                txtContact.setText(edtCentre.getNumber());
                txtEmail.setText(edtCentre.getEmail());
                txtDesc.setText(edtCentre.getDescription());
                txtWebsite.setText(edtCentre.getWebsite());
            }
        });

        //Update on click listener
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Confirmation box first
                ConfirmationDialog.showConfirmationDialog(getContext(), "Are you sure you want update the centre details?", new ConfirmationDialog.ConfirmationListener() {
                    @Override
                    public void onConfirm() {
                        //gets data to send
                        String updURL = GlobalUrl.updCentreUrl.replace("{id}", centreID);
                        // Get values from EditText fields
                        String Name = txtName.getText().toString();
                        String Address = txtAddress.getText().toString();
                        String Email = txtEmail.getText().toString();
                        String Description = txtDesc.getText().toString();
                        String Contact = txtContact.getText().toString();
                        String Website = txtWebsite.getText().toString();

                        // Create a body of data to send
                        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("centreName", Name)
                                .addFormDataPart("address", Address)
                                .addFormDataPart("email", Email)
                                .addFormDataPart("description", Description)
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
                            //Indetinfier for the api
                            requestBodyBuilder.addFormDataPart("newImage","0");
                        }else{
                            //Indetinfier for the api
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
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //fetchs the information again to reset the form
                                        binding.editCentreProg.setVisibility(View.VISIBLE);
                                        grpDisplay.setVisibility(View.INVISIBLE);
                                        fetchSingleCentre(centreID);
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onCancel() {

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
        String logicAppUrl = GlobalUrl.getSinCentreUrl.replace("{id}",centreID);

        // Create an instance of OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Create a request
        Request request = new Request.Builder()
                .url(logicAppUrl)
                .build();

        // Execute the request
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
                        String address = jsonResponse.getString("Address");
                        String website = jsonResponse.getString("website");
                        String description = jsonResponse.getString("description");
                        String logoid = jsonResponse.getString("logoName");


                        //TODO Check this comments
                        // Check if "RouteDetails" exists in the JSON
                        //List<ClsRoutes> routes = new ArrayList<>();


                        /*JSONArray routeDetailsArray = jsonResponse.getJSONArray("RouteDetails");
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
                        }*/

                        // Create a ClsCentre object and add it to the list
                        edtCentre = new ClsCentre(id, name, address, description, email, contact, website, logoid, routesList);

                        requireActivity().runOnUiThread(() -> {

                            //Sets necessary information
                            Glide.with(imgLogo.getContext()).load(GlobalUrl.imageUrl + edtCentre.getlogo()).apply(RequestOptions.placeholderOf(R.drawable.placeholder_image)).into(imgLogo);
                            txtName.setText(edtCentre.getCentreName());
                            txtAddress.setText(edtCentre.getAddress());
                            txtContact.setText(edtCentre.getNumber());
                            txtEmail.setText(edtCentre.getEmail());
                            txtWebsite.setText(edtCentre.getWebsite());
                            txtDesc.setText(edtCentre.getDescription());
                            grpDisplay.setVisibility(View.VISIBLE);
                            binding.editCentreProg.setVisibility(View.INVISIBLE);
                        });

                    } catch (JSONException e) {
                        Log.d("TestError", "JSONException: " + e.getMessage());
                    }
                }
            }
        });
    }


    //This was stolen from stackoverflow
    ///It will create a temporary file and path for the selected image and then send it along
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