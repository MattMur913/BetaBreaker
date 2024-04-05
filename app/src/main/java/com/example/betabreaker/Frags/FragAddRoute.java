package com.example.betabreaker.Frags;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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

import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.databinding.FragmentAddRouteBinding;

import java.io.File;
import java.io.IOException;

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
    private String centreID;
    private FragmentAddRouteBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddRouteBinding.inflate(inflater, container, false);


        imageView = binding.spRClimb;
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        TextView tvArea;
        TextView tvColour;
        TextView tvDate;
        TextView tvGrade;
        TextView tvSetter;
        TextView tvVotes;
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
                String area = binding.edArea.getText().toString();
                String colour = binding.edColour.getText().toString();
                String date = binding.edDate.getText().toString();
                String grade = binding.edGrade.getText().toString();
                String setter = binding.edSetter.getText().toString();

                File routeFile = new File(getRealPathFromURI(imageURI));

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

                // Send the request asynchronously
                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // Handle the response here
                        if (response.isSuccessful()) {
                            // Request successful
                            String responseData = response.body().string();
                            // Process responseData as needed
                        } else {
                            // Request failed
                            // Log error or show appropriate message
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

    }
    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
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
