package com.example.betabreaker.Frags;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentAddRouteBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragAddRoute extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Uri imageURI;
    private FragmentAddRouteBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddRouteBinding.inflate(inflater, container, false);


        imageView = binding.spRClimb;
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        TextView tvArea;
        TextView tvColour;
        TextView tvDate;
        TextView tvGrade;
        TextView tvSetter;
        ImageView ivRoute;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String centreID = preferences.getString("adminOf", "");

        tvArea = binding.edArea;
        tvColour = binding.edColour;
        tvDate = binding.edDate;
        tvGrade = binding.edGrade;
        tvSetter = binding.edSetter;
        ivRoute = binding.spRClimb;
        Button uploadButton = binding.rotInsert;

        ivRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String area =tvArea.getText().toString();
                String colour = tvColour.getText().toString();
                String date = tvDate.getText().toString();
                String grade = tvGrade.getText().toString();
                String setter = tvSetter.getText().toString();
                File routeFile;
                try {
                    routeFile = createTemporaryFileFromUri(imageURI);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (area.isEmpty() || colour.isEmpty() || date.isEmpty() || grade.isEmpty() || setter.isEmpty()) {
                    // Show a message indicating that all fields are required
                    Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
                    return; // Exit the method
                }

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("area", area)
                        .addFormDataPart("colour", colour)
                        .addFormDataPart("date", date)
                        .addFormDataPart("grade", grade)
                        .addFormDataPart("setter", setter)
                        .addFormDataPart("image", "image.jpg",
                                RequestBody.create(MediaType.parse("image/*"), routeFile))
                        .build();
                Request request = new Request.Builder()
                        .url(GlobalUrl.addRouteUrl.replace("{id}",centreID))
                        .post(requestBody)
                        .build();
                // Create OkHttpClient instance
                OkHttpClient client = new OkHttpClient();
                binding.addRoutProg.setVisibility(View.VISIBLE);
                uploadButton.setVisibility(View.GONE);
                // Send the request asynchronously
                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
                                    NavController navController = navHostFragment.getNavController();
                                    navController.popBackStack();
                                    navController.navigate(R.id.action_refreshAddRoute);
                                }
                            });

                        } else {
                            Log.d("TestError", "onResponse: Failed");
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
                                NavController navController = navHostFragment.getNavController();
                                navController.popBackStack();
                                navController.navigate(R.id.action_refreshAddRoute);
                                Toast.makeText(getContext(), "AN ERROR HAS OCCURRED PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Toast.makeText(getContext(), "AN ERROR HAS OCCURRED PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();
                    }
                });



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
            imageView.setImageURI(imageUri);
        }
    }
}
