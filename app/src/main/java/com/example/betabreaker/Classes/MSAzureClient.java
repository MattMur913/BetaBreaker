package com.example.betabreaker.Classes;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MSAzureClient extends AsyncTask<String, Void, String> {

    private ResponseCallBack callback;

    public MSAzureClient(ResponseCallBack callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        String method = params[0];
        String url = params[1];
        String requestBody = params.length > 2 ? params[2] : null;
        Log.d("SHOW DB", requestBody);

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResponse = null;

        try {
            // Create connection
            URL apiUrl = new URL(url);
            urlConnection = (HttpURLConnection) apiUrl.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);
            Log.d("SHOW DB", "sending");

            // Write request body for POST and PUT requests
            if (requestBody != null) {
                Log.d("SHOW DB", "sending non empty url");
                OutputStream outputStream = urlConnection.getOutputStream();
                outputStream.write(requestBody.getBytes());
                outputStream.flush();
                outputStream.close();
            }else{
                Log.d("SHOW DB", "sedning empty url");
            }

            // Read response
            StringBuilder response = new StringBuilder();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (urlConnection.getInputStream() != null) {
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                }
            } else {
                if (urlConnection.getErrorStream() != null) {
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                }
            }
            if (reader != null) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                jsonResponse = response.toString();
            } else {
                // Handle the case where the BufferedReader is null
            }


        } catch (IOException e) {
            Log.e("AzureHttpClient", "Error making HTTP request", e);
        } finally {
            // Close connections
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("AzureHttpClient", "Error closing reader", e);
                }
            }
        }
        return jsonResponse;
    }

    @Override
    protected void onPostExecute(String jsonResponse) {
        // Handle response in UI thread
        if (jsonResponse != null) {
            Log.d("SHOW DB", jsonResponse);
            callback.onResponseReceived(jsonResponse);
        } else {
            Log.d("SHOW DB", "Null json");
            callback.onResponseReceived(jsonResponse);
        }
    }
}