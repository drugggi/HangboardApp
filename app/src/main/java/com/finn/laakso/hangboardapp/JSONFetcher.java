package com.finn.laakso.hangboardapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class JSONFetcher extends AsyncTask<Void,Void,Void> {

    private String data;
    private String dataParced;
    private String singleParced;


    private WorkoutDBHandler dbHandler;
    // Simply create JSON parcer and fetcer from the DB
    // and send them all over the internet

    public JSONFetcher(WorkoutDBHandler db) {
        this.dbHandler = db;

    }

    public void constructJSONObjects() {
        boolean includeHidden = true;

        // ArrayList<int> allIDs = dbHandler.lookUpAllIDs(includeHidden);

        ArrayList<TimeControls> allTimeControls = dbHandler.lookUpAllTimeControls(includeHidden);
        // ArrayList<boolean> allIsHidden = dbHandler.lookUpAllIsHidden(includeHidden);
        // ArrayList<String> allDescriptions = dbHandler.lookUpAllDescriptions(includeHidden);


        ArrayList<Integer> allWorkoutIDs = dbHandler.lookUpAllWorkoutIDs(includeHidden);
        ArrayList<String> allHanboardNames = dbHandler.lookUpAllHangboards(includeHidden);
        ArrayList<Long> allDates = dbHandler.lookUpAllDates(includeHidden);
        ArrayList<String> allTimeControlsAsString = new ArrayList<>();

        ArrayList<String> allHoldNumbers = dbHandler.lookUpAllWorkoutHoldNumbers(includeHidden);
        ArrayList<String> allHoldGripTypes = dbHandler.lookUpAllWorkoutHoldGripTypes(includeHidden);
        ArrayList<String> allHoldDifficulties = dbHandler.lookUpAllWorkoutHoldDifficulties(includeHidden);

        ArrayList<String> allCompletedHangsAsString = dbHandler.lookUpAllCompletedAsString(includeHidden);
        ArrayList<Boolean> allWorkoutIsHidden = dbHandler.lookUpAllWorkoutIsHidden(includeHidden);
        ArrayList<String> allDescriptions = dbHandler.lookUpAllWorkoutDescriptions(includeHidden);

        for (int i = 0 ; i < allDates.size() ; i++ ) {
            allTimeControlsAsString.add(allTimeControls.get(i).getTimeControlsAsJSONGString() );
            Log.d("ID",": " + allWorkoutIDs.get(i) );
            Log.d("Date","" + allDates.get(i) );
            Log.d("Hangboard",allHanboardNames.get(i) );
            Log.d("TC",allTimeControlsAsString.get(i) );

            Log.d("Numbers",allHoldNumbers.get(i) );
            Log.d("GripTypes",allHoldGripTypes.get(i) );
            Log.d("Difficulties",allHoldDifficulties.get(i) );

            Log.d("Completed",allCompletedHangsAsString.get(i) );
            Log.d("IsHidden",": " + allWorkoutIsHidden.get(i) );
            Log.d("Descriptions",allDescriptions.get(i) );

        }

        // dbHandler.look
    }

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

            JSONArray JA = new JSONArray(data);

            for (int i = 0 ; i < JA.length() ; i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                String timeControls = "TC: " + JO.get("TimeControls");
                Log.d("Tc",timeControls);
            }




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Log.d("datasite","data: " + data.length() );
        WorkoutDetailsActivity.workoutDetailsTextView.setText(data);
    }
}
