package com.example.tonykogias.navigator;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpConnectionPostAsync extends AsyncTask<String, String, String> {

    JSONObject data;
    HttpAsyncResponse response;


    public HttpConnectionPostAsync(Map<String, String> data, HttpAsyncResponse response) {

        this.data= new JSONObject(data);
        this.response = response;
    }

    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection connection = null;

        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            if(this.data != null) {
                OutputStreamWriter owriter = new OutputStreamWriter(connection.getOutputStream());
                owriter.write(data.toString());
                owriter.flush();
                owriter.close();
            }

            connection.connect();
            int statusCode = connection.getResponseCode();
            if(statusCode != 0) {

                StringBuilder sb = new StringBuilder();
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader bin = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while((inputLine = bin.readLine()) != null) {
                    sb.append(inputLine);
                }

                return sb.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return " ";
    }

    @Override
    protected void onPostExecute(String s) {
        response.postData(s);
    }
}
