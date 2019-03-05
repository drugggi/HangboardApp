package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

// BenchmarkWorkoutsAdapter manages the premade Workotus list in BenchmarkActivity. It also
// gets them fro strings.xml and parce necessary information for textviews
public class BenchmarkWorkoutsAdapter extends BaseAdapter {

    private final Context mContext;

    // All the premade workouts for single hangboard are stored in these
    private ArrayList<String> benchmarkTitle;
    private ArrayList<String> benchmarkDescriptions;
    private ArrayList<TimeControls> benchmarkTimeControls;
    private ArrayList<ArrayList<Hold>> benchmarkWorkoutHolds;
    private int[] benchmarkGradeImageResources;

    private String[] hangboardNames;
    private String[] allBenchmarks;

    static class BenchmarkInfoViewHolder {
        TextView benchmarkTitleTextView;
        TextView benchmarkDescriptionTextView;
        ImageView benchmarkGradeImageView;
        int position;
    }

    public ArrayList<Hold> getWorkoutHolds(int position) {
        if (position > 0 && position < benchmarkWorkoutHolds.size()) {
            return benchmarkWorkoutHolds.get(position);
        } else {
            return benchmarkWorkoutHolds.get(0);
        }
    }


    public TimeControls getWorkoutTimeControls(int position) {
        if (position > 0 && position < benchmarkTimeControls.size()) {
            return benchmarkTimeControls.get(position);
        } else {
            return benchmarkTimeControls.get(0);
        }
    }

    // A lot of parcing to get from five lines of strings to workout's timecontrols and holds
    public BenchmarkWorkoutsAdapter(Context context, int selectedHangboard, String[] benchmarkResources) {

        hangboardNames = HangboardResources.getHangboardNames();
        allBenchmarks = benchmarkResources;
        parceBenchmarkPrograms(selectedHangboard);
        benchmarkGradeImageResources = new int[benchmarkTimeControls.size()];

        for (int i = 0; i < benchmarkTitle.size(); i++) {
            // There has to be title at least 3 lenfth to include grade
            if (benchmarkTitle.get(i).length() < 3) {
                benchmarkGradeImageResources[i] = getImageResources("Errr");
                benchmarkTitle.set(i,"Error: title too short");
                continue;
            }
            // If the grade is found show it and shorten the title
            benchmarkGradeImageResources[i] = getImageResources(benchmarkTitle.get(i).substring(0, 3));
            if (benchmarkGradeImageResources[i] != R.drawable.questiongrade ) {

                if (benchmarkTitle.get(i).charAt(0) == ' ') {
                    benchmarkTitle.set(i, benchmarkTitle.get(i).substring(4));
                } else {
                    benchmarkTitle.set(i, benchmarkTitle.get(i).substring(3));
                }

            }
        }

        this.mContext = context;
        TESTisCustomHolds();
    }

    @Override
    public int getCount() {
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

            convertView = inflater.inflate(R.layout.benchmark_listview, parent, false);
            viewHolder = new BenchmarkInfoViewHolder();

            viewHolder.benchmarkGradeImageView = (ImageView) convertView.findViewById(R.id.benchmarkGradeImageView);
            viewHolder.benchmarkTitleTextView = (TextView) convertView.findViewById(R.id.benchmarkTitleTextView);
            viewHolder.benchmarkDescriptionTextView = (TextView) convertView.findViewById(R.id.benchmarkDescriptionTextView);
            viewHolder.position = position;

            convertView.setTag(viewHolder);
        } else {

            viewHolder = (BenchmarkInfoViewHolder) convertView.getTag();

        }

        viewHolder.benchmarkDescriptionTextView.setText(benchmarkDescriptions.get(position));
        viewHolder.benchmarkTitleTextView.setText(benchmarkTitle.get(position));
        viewHolder.benchmarkGradeImageView.setImageResource(benchmarkGradeImageResources[position]);

        // When scrolling the list, benchmarks are fade in smoothly
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in500ms);
        convertView.startAnimation(animation);

        return convertView;
    }

    // getAnimatinoInfo method compares two workouts, and return the parameters' difference in string
    public String getAnimationInfo(TimeControls prevTC, ArrayList<Hold> prevHolds,
                                          TimeControls selectedTC, ArrayList<Hold> selectedHolds) {

        int totalTimeChange = selectedTC.getTotalTime() - prevTC.getTotalTime();
        int TUTChange = selectedTC.getTimeUnderTension() - prevTC.getTimeUnderTension();

        int prevGripLaps = prevTC.getGripLaps();
        int prevSets = prevTC.getRoutineLaps();
        int prevHangs = prevTC.getHangLaps();

        int[] prevTempCompleted = new int[prevGripLaps * prevSets];

        for (int i = 0; i < prevTempCompleted.length; i++) {
            prevTempCompleted[i] = prevHangs;
        }

        int selectedGripLaps = selectedTC.getGripLaps();
        int selectedSets = selectedTC.getRoutineLaps();
        int selectedHangs = selectedTC.getHangLaps();

        int[] selectedTempCompleted = new int[selectedGripLaps * selectedSets];

        // We have to make completed matrix so we get avgD, power, workload and power parameters
        for (int i = 0; i < selectedTempCompleted.length; i++) {
            selectedTempCompleted[i] = selectedHangs;
        }

        CalculateWorkoutDetails prevDetails = new CalculateWorkoutDetails(prevTC, prevHolds, prevTempCompleted);
        CalculateWorkoutDetails selectedDetails = new CalculateWorkoutDetails(selectedTC,selectedHolds, selectedTempCompleted);

        float intensityChange = (selectedDetails.getIntensity() - prevDetails.getIntensity());
        int avgDChange = (int) (selectedDetails.getAverageDifficutly() - prevDetails.getAverageDifficutly());
        float powerChange = (selectedDetails.getWorkoutPower() - prevDetails.getWorkoutPower());
        int workloadChange = (int) (selectedDetails.getWorkload() - prevDetails.getWorkload());

        String totalTime = "" + totalTimeChange + "s";
        String TUT = "" + TUTChange + "s";
        String intensity;
        String avgD;
        String power;
        String workload;

        if (intensityChange < 0) {
            intensity = "" + String.format(java.util.Locale.US, "%.2f", intensityChange);
        } else if (intensityChange > 0) {
            intensity = "+" + String.format(java.util.Locale.US, "%.2f", intensityChange);
        } else {
            intensity = "";
        }

        if (avgDChange < 0) {
            avgD = "" + avgDChange;
        } else if (avgDChange > 0) {
            avgD = "+" + avgDChange;
        } else {
            avgD = "";
        }

        if (totalTimeChange == 0) {
            totalTime = "";
        }
        if (TUTChange == 0) {
            TUT = "";
        }

        if (powerChange < 0) {
            power = "" + String.format(java.util.Locale.US, "%.2f", powerChange);
        } else if (powerChange > 0) {
            power = "+" + String.format(java.util.Locale.US, "%.2f", powerChange);
        } else {
            power = "";
        }

        if (workloadChange > 0) {
            workload = "+" + workloadChange;
        } else if (workloadChange < 0) {
            workload = "" + workloadChange;
        } else {
            workload = "";
        }


        return "\n" + totalTime + "\n" + TUT + "\n" + intensity +
                "\n" + avgD + "\n" + power + "\n" + workload + "\n\n";


    }
/*

    // getAnimationInfoText is same than getAnimationInfo except this compares two pre made workouts
    public String getAnimationInfoText(int previousPosition, int selectedPosition) {


        int totalTimeChange = benchmarkTimeControls.get(selectedPosition).getTotalTime() -
                benchmarkTimeControls.get(previousPosition).getTotalTime();
        int TUTChange = benchmarkTimeControls.get(selectedPosition).getTimeUnderTension() -
                benchmarkTimeControls.get(previousPosition).getTimeUnderTension();

        int prevGripLaps = benchmarkTimeControls.get(previousPosition).getGripLaps();
        int prevSets = benchmarkTimeControls.get(previousPosition).getRoutineLaps();
        int prevHangs = benchmarkTimeControls.get(previousPosition).getHangLaps();

        int[] prevTempCompleted = new int[prevGripLaps * prevSets];

        for (int i = 0; i < prevTempCompleted.length; i++) {
            prevTempCompleted[i] = prevHangs;
        }

        int selectedGripLaps = benchmarkTimeControls.get(selectedPosition).getGripLaps();
        int selectedSets = benchmarkTimeControls.get(selectedPosition).getRoutineLaps();
        int selectedHangs = benchmarkTimeControls.get(selectedPosition).getHangLaps();

        int[] selectedTempCompleted = new int[selectedGripLaps * selectedSets];

        for (int i = 0; i < selectedTempCompleted.length; i++) {
            selectedTempCompleted[i] = selectedHangs;
        }

        CalculateWorkoutDetails prevDetails = new CalculateWorkoutDetails(benchmarkTimeControls.get(previousPosition),
                benchmarkWorkoutHolds.get(previousPosition), prevTempCompleted);

        CalculateWorkoutDetails selectedDetails = new CalculateWorkoutDetails(benchmarkTimeControls.get(selectedPosition),
                benchmarkWorkoutHolds.get(selectedPosition), selectedTempCompleted);

        float intensityChange = (selectedDetails.getIntensity() - prevDetails.getIntensity());
        int avgDChange = (int) (selectedDetails.getAverageDifficutly() - prevDetails.getAverageDifficutly());
        float powerChange = (selectedDetails.getWorkoutPower() - prevDetails.getWorkoutPower());
        int workloadChange = (int) (selectedDetails.getWorkload() - prevDetails.getWorkload());

        String totalTime = "" + totalTimeChange + "s";
        String TUT = "" + TUTChange + "s";
        String intensity;
        String avgD;
        String power;
        String workload;

        if (intensityChange < 0) {
            intensity = "" + String.format(java.util.Locale.US, "%.2f", intensityChange);
        } else if (intensityChange > 0) {
            intensity = "+" + String.format(java.util.Locale.US, "%.2f", intensityChange);
        } else {
            intensity = "";
        }

        if (avgDChange < 0) {
            avgD = "" + avgDChange;
        } else if (avgDChange > 0) {
            avgD = "+" + avgDChange;
        } else {
            avgD = "";
        }

        if (totalTimeChange == 0) {
            totalTime = "";
        }
        if (TUTChange == 0) {
            TUT = "";
        }

        if (powerChange < 0) {
            power = "" + String.format(java.util.Locale.US, "%.2f", powerChange);
        } else if (powerChange > 0) {
            power = "+" + String.format(java.util.Locale.US, "%.2f", powerChange);
        } else {
            power = "";
        }

        if (workloadChange > 0) {
            workload = "+" + workloadChange;
        } else if (workloadChange < 0) {
            workload = "" + workloadChange;
        } else {
            workload = "";
        }


        return "\n" + totalTime + "\n" + TUT + "\n" + intensity +
                "\n" + avgD + "\n" + power + "\n" + workload + "\n\n";

    }
*/

    // getBenchmarkInfo method returns workout's info in string form that is easily shown in TextView
    public static String getBenchmarkInfo(TimeControls tempControls, ArrayList<Hold> workoutHolds) {

        int gripLaps = tempControls.getGripLaps();
        int sets = tempControls.getRoutineLaps();
        int hangs = tempControls.getHangLaps();

        int[] tempCompleted = new int[gripLaps * sets];

        for (int i = 0; i < tempCompleted.length; i++) {
            tempCompleted[i] = hangs;
        }

        CalculateWorkoutDetails benchmarkDetails = new CalculateWorkoutDetails(tempControls,
                workoutHolds, tempCompleted);

        String benchmarkInfo = "";
        String repeaters;
        if (tempControls.isRepeaters()) {
            repeaters = "Repeaters";
        } else {
            repeaters = "Single hangs";
        }

        String intensity = "0." + (int) (100 * benchmarkDetails.getIntensity());
        String workoutPower = (int) benchmarkDetails.getWorkoutPower() + ".";
        workoutPower += (int) (100 * (benchmarkDetails.getWorkoutPower() - (int) benchmarkDetails.getWorkoutPower()));

        String workload = "" + (int) benchmarkDetails.getWorkload();

        benchmarkInfo += repeaters + "\n";
        benchmarkInfo += "Total time: " + tempControls.getTotalTime() / 60 + "min\n";
        benchmarkInfo += "TUT: " + tempControls.getTimeUnderTension() + "s\n";
        benchmarkInfo += "Intensity: " + intensity + "\n";
        benchmarkInfo += "avg Difficulty: " + (int) benchmarkDetails.getAverageDifficutly() + "\n";
        benchmarkInfo += "Power: " + workoutPower + "\n";
        benchmarkInfo += "Workload: " + workload + "\n";
        benchmarkInfo += "Time Controls: \n" + tempControls.getTimeControlsAsJSONGString();

        return benchmarkInfo;




    }

    public String getBenchmarkInfoText(int selectedBenchmark) {

        if (selectedBenchmark < 0 || selectedBenchmark >= benchmarkTimeControls.size()) {
            return "ERROR";
        }
        return getBenchmarkInfo(benchmarkTimeControls.get(selectedBenchmark),benchmarkWorkoutHolds.get(selectedBenchmark));

    }

    // A lot of errar handlin is done so that even if pre made workotus are wronly typed in strings.xml
    // Whole program does not crash
    private void addErrorTimeControlsAndHolds() {
        TimeControls errorControls = new TimeControls();
        errorControls.setTimeControls(new int[] {1,1,10,0,1,150,150});
        benchmarkTimeControls.add(errorControls);
        Hold leftHand = new Hold(1);
        leftHand.setGripTypeAndSingleHold(80);
        ArrayList holds = new ArrayList();
        holds.add(leftHand);
        holds.add(leftHand);
        benchmarkWorkoutHolds.add(holds);

    }

    // parceBenchmarkProgram parces strings to TimeControls and Holds and titles
    private void parceBenchmarkPrograms(int selectedHangboardPosition) {

        hangboardNames = HangboardResources.getHangboardNames();

        benchmarkTitle = new ArrayList<>();
        benchmarkDescriptions = new ArrayList<>();
        benchmarkTimeControls = new ArrayList<>();
        benchmarkWorkoutHolds = new ArrayList<>();

        if (allBenchmarks.length % 5 != 0){
            benchmarkTitle.add("SIZE ERROR");
            benchmarkDescriptions.add("size: " + allBenchmarks.length);
            addErrorTimeControlsAndHolds();
            return;
        }

        for (int i = 0; i < allBenchmarks.length; i++) {

            //String title = allBenchmarks[i];
            if (allBenchmarks[i].length() < 3) {
                benchmarkTitle.add("Error too short title");
            } else {
                benchmarkTitle.add(allBenchmarks[i]);
            }
            i++;

            String desc = allBenchmarks[i];
            i++;

            TimeControls tempControls = new TimeControls();
            tempControls.setTimeControlsFromString(allBenchmarks[i]);
            benchmarkTimeControls.add(tempControls);

            // It is really useful to know if workout is repeaters of single hangs
            if (tempControls.isRepeaters() ) {
                benchmarkDescriptions.add(desc+"  (repeaters)");
            } else {
                benchmarkDescriptions.add(desc+"  (single hangs)");
            }

            i++;
            ArrayList<Hold> tempWorkoutHolds = new ArrayList<>();
            int[] tempHoldNumbers = parceStringToInt(allBenchmarks[i]);
            i++;
            int[] tempHoldGripTypes = parceStringToInt(allBenchmarks[i]);


            if (tempControls.getGripLaps() * 2 != tempHoldNumbers.length ||
                    tempControls.getGripLaps() * 2 != tempHoldGripTypes.length) {

                Log.e("ERR", "timecontrols griplaps different size than needed workoutholds at: " + i);
                //  Toast.makeText(this,"ERROr PARCE SIZES",Toast.LENGTH_LONG).show();
                //Log.d("DESC",benchmarkDescriptions.get(i));
                Log.d("time controls", tempControls.getTimeControlsAsJSONGString());
                Log.d("Hold numbers", ": " + tempHoldNumbers.length);
                Log.d("grip types", ": " + tempHoldGripTypes.length);

                StringBuilder griptypes = new StringBuilder();
                for (int j = 0; j < tempHoldNumbers.length; j++) {
                    griptypes.append("1,");
                }
                Log.d("wanted grip types", griptypes.toString());

            }

            Hold tempHold;
            for (int j = 0; j < tempHoldNumbers.length; j++) {

                tempHold = new Hold(tempHoldNumbers[j]);
                tempHold.setGripType(tempHoldGripTypes[j]);

                int holdDifficulty = HangboardResources.getHoldDifficulty(tempHold, hangboardNames[selectedHangboardPosition]);
                tempHold.setHoldValue(holdDifficulty);

                tempWorkoutHolds.add(tempHold);
            }

            benchmarkWorkoutHolds.add(tempWorkoutHolds);

        }

    }

    // Time controls, griptypes and holds are stores as ints separated by ',' -> "6,6,7,3,3,150,360"
    public static int[] parceStringToInt(String arrayIntLine) {

        if (arrayIntLine.length() == 0) {
            Log.e("parceStringToInt","lenght was 0 ");
            return new int[] {1,1};
        }

        String[] parcedCompletedHangs = arrayIntLine.replaceAll("\\s+","").split(",");

        int[] completed = new int[parcedCompletedHangs.length];

        try {
            for (int i = 0; i < parcedCompletedHangs.length; i++) {
                completed[i] = Integer.parseInt(parcedCompletedHangs[i]);
            }
        } catch (NumberFormatException e) {
            Log.e("parceStringtoInt","NumberFormatException");
            e.printStackTrace();
            return new int[] {1,1};
        }

        return completed;
    }

    // For visuals
    private int getImageResources(String grade) {
        if (grade.equals("5A ")) {
            return R.drawable.fivea;
        } else if (grade.equals("5B ")) {
            return R.drawable.fiveb;
        } else if (grade.equals("5C ")) {
            return R.drawable.fivec;
        } else if (grade.equals("6A ")) {
            return R.drawable.sixa;
        } else if (grade.equals("6A+")) {
            return R.drawable.sixaplus;
        } else if (grade.equals("6B ")) {
            return R.drawable.sixb;
        } else if (grade.equals("6B+")) {
            return R.drawable.sixbplus;
        } else if (grade.equals("6C ")) {
            return R.drawable.sixc;
        } else if (grade.equals("6C+")) {
            return R.drawable.sixcplus;
        } else if (grade.equals("7A ")) {
            return R.drawable.sevena;
        } else if (grade.equals("7A+")) {
            return R.drawable.sevenaplus;
        } else if (grade.equals("7B ")) {
            return R.drawable.sevenb;
        } else if (grade.equals("7B+")) {
            return R.drawable.sevenbplus;
        } else if (grade.equals("7C ")) {
            return R.drawable.sevenc;
        } else if (grade.equals("7C+")) {
            return R.drawable.sevencplus;
        } else if (grade.equals("8A ")) {
            return R.drawable.eighta;
        } else if (grade.equals("8A+")) {
            return R.drawable.eightaplus;
        } else if (grade.equals("8B ")) {
            return R.drawable.eightb;
        } else if (grade.equals("8B+")) {
            return R.drawable.eightbplus;
        } else if (grade.equals("8C ")) {
            return R.drawable.eightc;
        } else {
            return R.drawable.questiongrade;
        }

    }
    public static void TESTaddBenchmarksIntoDatabase(WorkoutDBHandler dbHandler, String[] benchmarkResources, int testHangboardNumber) {

        long time = System.currentTimeMillis() - 1000*24*60*60*1000L;

        ArrayList<String> testBenchmarkDescriptions = new ArrayList<>();
        ArrayList<TimeControls> testBenchmarkTimeControls = new ArrayList<>();
        ArrayList<ArrayList<Hold>> testBenchmarkWorkoutHolds = new ArrayList<>();

        String[] testHangboardNames = HangboardResources.getHangboardNames();
        String[] testAllBenchmarks = benchmarkResources;

        //testHangboardNames = HangboardResources.getHangboardNames();
        /// testAllBenchmarks = benchmarkResources;


        for (int i = 0; i < testAllBenchmarks.length; i++) {
            // i++; // skip Title
            testBenchmarkDescriptions.add(testAllBenchmarks[i]+"\n" + testAllBenchmarks[i+1]);
            i++;
            i++;
            TimeControls tempControls = new TimeControls();
            // Log.d("timecontrols",testAllBenchmarks[i]);
            tempControls.setTimeControlsFromString(testAllBenchmarks[i]);
            testBenchmarkTimeControls.add(tempControls);

            i++;
            ArrayList<Hold> tempWorkoutHolds = new ArrayList<>();
            int[] tempHoldNumbers = parceStringToInt(testAllBenchmarks[i]);
            i++;
            int[] tempHoldGripTypes = parceStringToInt(testAllBenchmarks[i]);


            if (tempControls.getGripLaps() * 2 != tempHoldNumbers.length ||
                    tempControls.getGripLaps() * 2 != tempHoldGripTypes.length) {

                Log.e("ERR", "timecontrols griplaps different size than needed workoutholds at: " + i);
                //  Toast.makeText(this,"ERROr PARCE SIZES",Toast.LENGTH_LONG).show();
                //Log.d("DESC",benchmarkDescriptions.get(i));
                Log.d("time controls", tempControls.getTimeControlsAsJSONGString());
                Log.d("Hold numbers", ": " + tempHoldNumbers.length);
                Log.d("grip types", ": " + tempHoldGripTypes.length);

                StringBuilder griptypes = new StringBuilder();
                for (int j = 0; j < tempHoldNumbers.length; j++) {
                    griptypes.append("1,");
                }
                Log.d("wanted grip types", griptypes.toString());

            }

            Hold tempHold;
            for (int j = 0; j < tempHoldNumbers.length; j++) {

                tempHold = new Hold(tempHoldNumbers[j]);
                tempHold.setGripType(tempHoldGripTypes[j]);

                int holdDifficulty = HangboardResources.getHoldDifficulty(tempHold, testHangboardNames[testHangboardNumber]);
                tempHold.setHoldValue(holdDifficulty);

                tempWorkoutHolds.add(tempHold);
            }

            testBenchmarkWorkoutHolds.add(tempWorkoutHolds);

        }

        for (int  i = 0 ; i < testBenchmarkTimeControls.size() ; i ++) {

            time += 24*60*60*1000L;

            TimeControls tempControls = testBenchmarkTimeControls.get(i);

            int gripLaps = tempControls.getGripLaps();
            int sets = tempControls.getRoutineLaps();
            int hangs = tempControls.getHangLaps();

            int[] tempCompleted = new int[gripLaps * sets];

            for (int j = 0; j < tempCompleted.length; j++) {
                tempCompleted[j] = hangs;
            }

            dbHandler.addHangboardWorkout(time,
                    testHangboardNames[testHangboardNumber],
                    testBenchmarkTimeControls.get(i),
                    testBenchmarkWorkoutHolds.get(i),
                    tempCompleted,
                    testBenchmarkDescriptions.get(i));
        }
    }

    private void TESTisCustomHolds() {
        for (int i = 0; i < benchmarkWorkoutHolds.size() ; i++) {
            for (int j = 0 ; j < benchmarkWorkoutHolds.get(i).size() ; j++) {
                if (benchmarkWorkoutHolds.get(i).get(j).getHoldValue() == 0) {
                    Log.e("TEST for custom hold","ERROR CUSTOM HOLD FOUND"+i+ " " + j);
                }
            }
        }
    }

}