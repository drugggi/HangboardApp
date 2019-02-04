package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class BenchmarkWorkoutsAdapter extends BaseAdapter {

    private LayoutInflater mInflator;
    private final Context mContext;

    private ArrayList<String> benchmarkDescriptions;
    private ArrayList<TimeControls> benchmarkTimeControls;
    private ArrayList<ArrayList<Hold>> benchmarkWorkoutHolds;

    private String[] hangboardNames;

    String[] allBenchmarks;

    static class BenchmarkInfoViewHolder {

        TextView benchmarkTitleTextView;
        TextView benchmarkDescriptionTextView;
        ImageView benchmarkGradeImageView;
        int position;

    }


    public BenchmarkWorkoutsAdapter(Context context,int selectedHangboard,String[] benchmarkResources) {

        hangboardNames = HangboardResources.getHangboardNames();
        allBenchmarks = benchmarkResources;
        parceBenchmarkPrograms(selectedHangboard);

        Log.d("Sizes",benchmarkWorkoutHolds.size() + " " + benchmarkDescriptions.size() + " " );

        this.mContext = context;
        this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // this is the same as hangsCompleted.length
    @Override
    public int getCount() {
        // return timeControls.getGripLaps()*timeControls.getRoutineLaps();
        return benchmarkTimeControls.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       BenchmarkInfoViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            convertView = inflater.inflate(R.layout.benchmark_listview,parent,false);
            viewHolder = new BenchmarkInfoViewHolder();

            viewHolder.benchmarkGradeImageView = (ImageView) convertView.findViewById(R.id.benchmarkGradeImageView);
            viewHolder.benchmarkTitleTextView = (TextView) convertView.findViewById(R.id.benchmarkTitleTextView);
            viewHolder.benchmarkDescriptionTextView = (TextView) convertView.findViewById(R.id.benchmarkDescriptionTextView);
            viewHolder.position = position;

            convertView.setTag(viewHolder);
        }
        else {

            viewHolder = (BenchmarkInfoViewHolder) convertView.getTag();

        }

        viewHolder.benchmarkDescriptionTextView.setText(benchmarkDescriptions.get(position) );
        viewHolder.benchmarkTitleTextView.setText("Benchmark (Single hangs)");

        Random rng = new Random();

        if (rng.nextBoolean() ) {
            viewHolder.benchmarkGradeImageView.setImageResource(R.drawable.grade_logo_test);
        } else {
            viewHolder.benchmarkGradeImageView.setImageResource(R.drawable.benchmark_icon);
        }
        return convertView;
    }

    public String getBenchmarkInfoText(int selectedBenchmark) {

        if (selectedBenchmark < 0 || selectedBenchmark >= benchmarkTimeControls.size() ) {
            return "ERROR";
        }

        TimeControls tempControls = benchmarkTimeControls.get(selectedBenchmark);

        int gripLaps = tempControls.getGripLaps();
        int sets = tempControls.getRoutineLaps();
        int hangs = tempControls.getHangLaps();

        int[] tempCompleted = new int[gripLaps * sets];

        for (int i = 0 ; i < tempCompleted.length ;  i++) {
            tempCompleted[i] = hangs;
        }

        CalculateWorkoutDetails benchmarkDetails = new CalculateWorkoutDetails(tempControls,
                benchmarkWorkoutHolds.get(selectedBenchmark),tempCompleted);

        String benchmarkInfo = "";
        String repeaters;
        if (tempControls.isRepeaters() ) { repeaters = "Repeaters"; }
        else {repeaters = "Single hangs"; }

        String intensity = "0." + (int)(100 * benchmarkDetails.getIntensity());
        String workoutPower = (int) benchmarkDetails.getWorkoutPower() + ".";
        workoutPower += (int) (100 * (benchmarkDetails.getWorkoutPower() - (int) benchmarkDetails.getWorkoutPower() ) );

        String workload = "" + (int) benchmarkDetails.getWorkload();

        benchmarkInfo += repeaters + "\n";
        benchmarkInfo += "Total time: " + tempControls.getTotalTime()/60 + "min\n";
        benchmarkInfo += "TUT: " + tempControls.getTimeUnderTension() + "s\n";
        benchmarkInfo += "Intensity: " + intensity +"\n";
        benchmarkInfo += "avg Difficulty: " + (int)benchmarkDetails.getAverageDifficutly() + "\n";
        benchmarkInfo += "Power: " + workoutPower + "\n";
        benchmarkInfo += "Workload: " + workload + "\n";
        benchmarkInfo += "Time Controls: \n" + tempControls.getTimeControlsAsJSONGString();

        return benchmarkInfo;

    }

    private void parceBenchmarkPrograms(int selectedHangboardPosition) {

        hangboardNames = HangboardResources.getHangboardNames();

        benchmarkDescriptions = new ArrayList<>();
        benchmarkTimeControls = new ArrayList<>();
        benchmarkWorkoutHolds = new ArrayList<>();

  // currentHangboard = new Hangboard(res,HangboardResources.getHangboardName(0));
        // currentHangboard.setGripAmount;


        for (int i = 0 ; i < allBenchmarks.length ; i++) {
            benchmarkDescriptions.add(allBenchmarks[i]);

            i++;
            TimeControls tempControls = new TimeControls();
            tempControls.setTimeControlsFromString(allBenchmarks[i]);
            benchmarkTimeControls.add(tempControls);

            i++;
            ArrayList<Hold> tempWorkoutHolds = new ArrayList<>();
            int[] tempHoldNumbers = parceStringToInt(allBenchmarks[i]);
            i++;
            int[] tempHoldGripTypes = parceStringToInt(allBenchmarks[i]);


            if (tempControls.getGripLaps()*2 != tempHoldNumbers.length ||
                    tempControls.getGripLaps()*2 != tempHoldGripTypes.length ) {

                Log.e("ERR","timecontrols griplaps different size than needed workoutholds at: " + i);
               //  Toast.makeText(this,"ERROr PARCE SIZES",Toast.LENGTH_LONG).show();
                //Log.d("DESC",benchmarkDescriptions.get(i));
                Log.d("time controls",tempControls.getTimeControlsAsJSONGString() );
                Log.d("Hold numbers",": " + tempHoldNumbers.length);
                Log.d("grip types",": " + tempHoldGripTypes.length);

                StringBuilder griptypes = new StringBuilder();
                for (int j = 0 ; j < tempHoldNumbers.length ; j++) {
                    griptypes.append("1,");
                }
                Log.d("wanted grip types",griptypes.toString() );

            }

            Hold tempHold;
            for (int j = 0 ; j < tempHoldNumbers.length ; j++ ) {

                tempHold = new Hold(tempHoldNumbers[j] );
                tempHold.setGripType(tempHoldGripTypes[j] );

                int holdDifficulty = HangboardResources.getHoldDifficulty(tempHold,hangboardNames[selectedHangboardPosition] );
                tempHold.setHoldValue(holdDifficulty);

                tempWorkoutHolds.add(tempHold);
            }

            benchmarkWorkoutHolds.add(tempWorkoutHolds);

            //   Log.d("benchmark","data line: " + i + " = " +allBenchmarks[i]);
        }

     /*   for (int i = 0 ; i <benchmarkDescriptions.size() ; i++) {
            Log.d("DESC",benchmarkDescriptions.get(i) );
            Log.d("TC",benchmarkTimeControls.get(i).getTimeControlsAsJSONGString() );

            String holds = "";
            String grips = "";
            String diffis = "";
            for (int j = 0 ; j < benchmarkWorkoutHolds.get(i).size() ; j=j+2) {

                Log.d("HOLDS",benchmarkWorkoutHolds.get(i).get(j).getHoldInfo( benchmarkWorkoutHolds.get(i).get(j+1) ).replace("\n"," ") );

             //   holds += benchmarkWorkoutHolds.get(i).get(j).getHoldNumber() + "";
              //  grips += benchmarkWorkoutHolds.get(i).get(j).getGripStyleInt() + "";
              //  diffis += benchmarkWorkoutHolds.get(i).get(j).getHoldValue() + "";

            }
            Log.d("HANGS",holds +  "   " +grips + "    " + diffis);

        }
*/
    }


    public static int[] parceStringToInt(String arrayIntLine) {
        String[] parcedCompletedHangs = arrayIntLine.split(",");

        int[] completed = new int[parcedCompletedHangs.length];

        for (int i = 0; i < parcedCompletedHangs.length; i++) {
            completed[i] = Integer.parseInt(parcedCompletedHangs[i]);
        }

        return completed;
    }
}
