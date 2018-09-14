package com.finn.laakso.hangboardapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONFetcher extends AsyncTask<Void,Void,Void> {

    private String data;

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.myjson.com/bins/1b0ybk");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder dataBuilder = new StringBuilder();
            String line = "";

            while (line != null) {
                line = bufferedReader.readLine();
                dataBuilder.append(line);
            }

            data = dataBuilder.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Log.d("datasite","data: " + data.length() );
    }
}
