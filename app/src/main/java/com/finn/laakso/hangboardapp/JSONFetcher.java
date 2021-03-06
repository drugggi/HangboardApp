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
        ArrayList<String> allHangboardNames = dbHandler.lookUpAllHangboards(includeHidden);
        ArrayList<Long> allDates = dbHandler.lookUpAllDates(includeHidden);
        ArrayList<String> allTimeControlsAsString = new ArrayList<>();

        ArrayList<String> allHoldNumbers = dbHandler.lookUpAllWorkoutHoldNumbers(includeHidden);
        ArrayList<String> allHoldGripTypes = dbHandler.lookUpAllWorkoutHoldGripTypes(includeHidden);
        ArrayList<String> allHoldDifficulties = dbHandler.lookUpAllWorkoutHoldDifficulties(includeHidden);

        ArrayList<String> allCompletedHangsAsString = dbHandler.lookUpAllCompletedAsString(includeHidden);
        ArrayList<Boolean> allWorkoutIsHidden = dbHandler.lookUpAllWorkoutIsHidden(includeHidden);
        ArrayList<String> allDescriptions = dbHandler.lookUpAllWorkoutDescriptions(includeHidden);

        StringBuilder jsonStringBuilder = new StringBuilder();
        jsonStringBuilder.append("[");

        JSONArray jsonArray = new JSONArray();
        for (int i = 0 ; i < allDates.size() ; i++ ) {
            allTimeControlsAsString.add(allTimeControls.get(i).getTimeControlsAsJSONGString() );

            jsonStringBuilder.append("{\n");

            jsonStringBuilder.append("\"ID\"").append(" : ").append(allWorkoutIDs.get(i)).append(" ,\n");
            jsonStringBuilder.append("\"Date\"").append(" : ").append(allDates.get(i) ).append(" ,\n");
            jsonStringBuilder.append("\"Hangboard\"").append(" : \"").append(allHangboardNames.get(i) ).append("\" ,\n");
            jsonStringBuilder.append("\"TC\"").append(" : \"").append(allTimeControlsAsString.get(i) ).append("\" ,\n");

            jsonStringBuilder.append("\"Numbers\"").append(" : \"").append(allHoldNumbers.get(i) ).append("\" ,\n");
            jsonStringBuilder.append("\"GripTypes\"").append(" : \"").append(allHoldGripTypes.get(i) ).append("\" ,\n");
            jsonStringBuilder.append("\"Difficulties\"").append(" : \"").append(allHoldDifficulties.get(i) ).append("\" ,\n");

            jsonStringBuilder.append("\"Completed\"").append(" : \"").append(allCompletedHangsAsString.get(i) ).append("\" ,\n");
            jsonStringBuilder.append("\"IsHidden\"").append(" : ").append(allWorkoutIsHidden.get(i) ).append(" ,\n");
            jsonStringBuilder.append("\"Description\"").append(" : \"").append(allDescriptions.get(i) ).append("\" \n");

            if (i == allDates.size() - 1) {
                jsonStringBuilder.append("}");
            }
            else {
                jsonStringBuilder.append("},\n");
            }
            /*
            Log.d("ID",": " + allWorkoutIDs.get(i) );
            Log.d("Date","" + allDates.get(i) );
            Log.d("Hangboard",allHangboardNames.get(i) );
            Log.d("TC",allTimeControlsAsString.get(i) );

            Log.d("Numbers",allHoldNumbers.get(i) );
            Log.d("GripTypes",allHoldGripTypes.get(i) );
            Log.d("Difficulties",allHoldDifficulties.get(i) );

            Log.d("Completed",allCompletedHangsAsString.get(i) );
            Log.d("IsHidden",": " + allWorkoutIsHidden.get(i) );
            Log.d("Description",allDescriptions.get(i) );
*/
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ID",allWorkoutIDs.get(i) );
                jsonObject.put("Date",allDates.get(i) );
                jsonObject.put("Hangboard",allHangboardNames.get(i) );
                jsonObject.put("Timecontrols",allTimeControls.get(i) );

                jsonObject.put("Numbers",allHoldNumbers.get(i) );
                jsonObject.put("Griptypes",allHoldGripTypes.get(i) );
                jsonObject.put("Difficulties",allHoldDifficulties.get(i) );

                jsonObject.put("Completed",allCompletedHangsAsString.get(i) );
                jsonObject.put("Ishidden",allWorkoutIsHidden.get(i) );
                jsonObject.put("Description",allDescriptions.get(i) );

                jsonArray.put(jsonObject);
            } catch (JSONException jsone) {
                jsone.printStackTrace();
            }
        }

        jsonStringBuilder.append("]\n");

        String jsonText = jsonStringBuilder.toString();

        int maxLogSize = 1000;
        for(int i = 0; i <= jsonText.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > jsonText.length() ? jsonText.length() : end;

            Log.v("json", jsonText.substring(start, end));
        }

        //Log.d("JsonString",jsonStringBuilder.toString() );

        // Log.d("JSON","length: " + jsonArray.length() );

        // dbHandler.look
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            //URL url = new URL("https://api.myjson.com/bins/1b0ybk");
            URL url = new URL("https://api.myjson.com/bins/fha8c");

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
                int ID = (int) JO.getInt("ID");
                long date = (long) JO.getLong("Date");
                String hangboardName = (String) JO.getString("Hangboard");
                String timeControls = (String) JO.getString("TC");

                String holdNumbers = (String) JO.getString("Numbers");
                String holdGripTypes = (String) JO.getString("GripTypes");
                String holdDifficulties = (String) JO.getString("Difficulties");

                String completedHangs = (String) JO.getString("Completed");
                boolean isHidden = (boolean) JO.getBoolean("IsHidden");
                String description = (String) JO.getString("Description");


/*


                Log.d("ID",": " + ID );
                Log.d("Date","" + date );
                Log.d("Hangboard",hangboardName );
                Log.d("TC",timeControls );

                Log.d("Numbers",holdNumbers );
                Log.d("GripTypes", holdGripTypes );
                Log.d("Difficulties", holdDifficulties );

                Log.d("Completed",completedHangs );
                Log.d("IsHidden",": " + isHidden );
                Log.d("Descriptions", description );
*/

                TimeControls tempControls = new TimeControls();
                tempControls.setTimeControlsFromString(timeControls);

                ArrayList<Hold> tempWorkoutHolds = new ArrayList<>();
                int[] tempHoldNumbers = parceCompletedHangs(holdNumbers);
                int[] tempHoldGripTypes = parceCompletedHangs(holdGripTypes);
                int[] tempHoldDifficulties = parceCompletedHangs(holdDifficulties);

                Hold tempHold;
                for (int j = 0 ; j < tempHoldNumbers.length ; j++ ) {

                    tempHold = new Hold(tempHoldNumbers[j] );
                    tempHold.setGripType(tempHoldGripTypes[j] );
                    tempHold.setHoldValue(tempHoldDifficulties[j] );

                    tempWorkoutHolds.add(tempHold);
                }

                int[] tempCompleted = parceCompletedHangs(completedHangs);

                // Adding deserialized JSONobject to database
                 dbHandler.addHangboardWorkout(date, hangboardName,tempControls,tempWorkoutHolds,tempCompleted,description);

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
       //  WorkoutDetailsActivity.workoutDetailsTextView.setText(data);
    }

    public static int[] parceCompletedHangs(String completedHangs) {
        String[] parcedCompletedHangs = completedHangs.split(",");

        int[] completed = new int[parcedCompletedHangs.length];

        for (int i = 0; i < parcedCompletedHangs.length; i++) {
            completed[i] = Integer.parseInt(parcedCompletedHangs[i]);
        }

        return completed;
    }
}
