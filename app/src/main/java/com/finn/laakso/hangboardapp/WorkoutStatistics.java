package com.finn.laakso.hangboardapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class WorkoutStatistics extends AppCompatActivity {

    TextView workoutInfoTextView;
    TextView holdInfoTextView;

    ListView workoutHistoryListView;
    ArrayList<ArrayList<Hold>> arrayList_workoutHolds;
    ArrayList<TimeControls> allTimeControls;
    ArrayList<String> dates;
    Random rng;

    ArrayList<Hold> workoutHolds;
    TimeControls timeControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_statistics);
        rng = new Random();
        workoutInfoTextView = (TextView) findViewById(R.id.workoutInfoTextView);
        holdInfoTextView = (TextView) findViewById(R.id.holdInfoTextView);

        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);


/*
        HangBoard testBoard = new HangBoard(getResources());

        workoutHistoryListView = (ListView) findViewById(R.id.workoutHistoryListView);
        final ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(this, R.layout.gradetextview, testBoard.getGrades());
        workoutHistoryListView.setAdapter(gradeAdapter);*/

        // Holds that will be used in this workout program
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.HOLDS")) {
            workoutHolds = getIntent().getExtras().getParcelableArrayList("com.finn.laakso.hangboardapp.HOLDS");
        }
/*
        // Hangboard image that user has selected
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.BOARDIMAGE")) {
            int image_resource = getIntent().getIntExtra("com.finn.laakso.hangboardapp.BOARDIMAGE", 0);
            boardimage.setImageResource(image_resource);
            pinchZoomBoardImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),image_resource));
            pinchZoomBoardImage.setVisibility(View.VISIBLE);
        }
*/
        // This Intent brings the time controls to the workout program
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.TIMECONTROLS")) {
            int[] time_controls = getIntent().getExtras().getIntArray("com.finn.laakso.hangboardapp.TIMECONTROLS");

            timeControls = new TimeControls();
            timeControls.setTimeControls(time_controls);

            // SECURITY CHECK, WILL MAKE SURE IN FUTURE TO NEVER HAPPEN
            if (timeControls.getGripLaps()*2 != workoutHolds.size()) {
                timeControls.setGripLaps(workoutHolds.size()/2);
            }
        }

        dbHandler.addHangboardWorkout(112233,"test board", timeControls, workoutHolds);

       // Log.d("before tc","tc next");
        allTimeControls = new ArrayList<TimeControls>();
        dates = new ArrayList<String>();
        arrayList_workoutHolds = new ArrayList<ArrayList<Hold>>();
       // Log.d("after tc","tc was");
        for (int i = 0; i <50;i++ ) {
            allTimeControls.add(getRandomTimeControls());
            dates.add(getRandomDate());
            arrayList_workoutHolds.add(getRandomWorkoutHolds(allTimeControls.get(i).getGripLaps()));
        }

/*
        Date c = Calendar.getInstance().getTime();
        Toast.makeText(this,"arraylist sizee: " + arrayList_workoutHolds.size(),Toast.LENGTH_SHORT).show();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        Toast.makeText(this, "arraylist 1 item size" +arrayList_workoutHolds.get(0).size(),Toast.LENGTH_LONG).show();
*/
        //String[] dates = {formattedDate,"tuesday","wednesday"};
        // String[] hangboards = {"bm100","bm2000","zlag"};
        // String[] difficulty = {"hard","easy","etc"};

        final WorkoutHistoryAdapter workoutAdapter = new WorkoutHistoryAdapter(this,dates, allTimeControls,arrayList_workoutHolds);

        workoutHistoryListView = (ListView) findViewById(R.id.workoutHistoryListView);
        workoutHistoryListView.setAdapter(workoutAdapter);

        workoutHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);
                // dbHandler.addHangboardWorkout(allTimeControls.get(0).getTotalTime(),"Metolius",allTimeControls.get(position+1),arrayList_workoutHolds.get(position+1));

                // Get Selected items workoutmatrix
                // workoutInfoTextView.setText(workoutAdapter.getSelectedTimeControls(position).getGripMatrix(true));
                // String holdinfo = workoutAdapter.getSelectedHoldInfo(position);

                if ( position == 0) {
                    holdInfoTextView.setText("" + dbHandler.lookUpDate(position));

                }

                String he = "";
                ArrayList<Hold> test = dbHandler.lookUpHolds(position);

                String text = "";
                for (int i = 0 ; i < test.size() ; i++) {
                    
                    text = text + test.get(i).getHoldNumber() +test.get(i).getHoldText() +test.get(i).getHoldValue() + "\n";
                    /*
                    Log.e("hld nro: " , ": " + test.get(i).getHoldNumber());
                    Log.e("grip type: " , ": " + test.get(i).getHoldText());
                    Log.e("hold value: " , ": " + test.get(i).getHoldValue());*/
                }
                workoutInfoTextView.setText(text);
                TimeControls temp = dbHandler.lookUpTimeControls(position);
                holdInfoTextView.setText(temp.getGripMatrix(true));

            }
        });



        holdInfoTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);
                dbHandler.DELETEALL();

                Toast.makeText(WorkoutStatistics.this, "All DELETED, Happy now",Toast.LENGTH_LONG).show();


                return true;
            }
        });


    }

    private ArrayList<Hold> getRandomWorkoutHolds(int number_of_holds) {

        ArrayList<Hold> newHolds = new ArrayList<Hold>();
        for (int i = 0;i < number_of_holds; i++) {
            newHolds.add(getNewRandomHold());      // left hand
            newHolds.add(getNewRandomHold()); // right hand

        }
        return newHolds;
    }

    private Hold getNewRandomHold() {
        Hold newHold = new Hold(rng.nextInt(20)+1);
        newHold.setHoldValue(rng.nextInt(100)+1);
        int i_hold_bot_info = (rng.nextInt(8)+1)*10;


        i_hold_bot_info = i_hold_bot_info + rng.nextInt(1);
        newHold.setGripTypeAndSingleHang(i_hold_bot_info);
        return newHold;
    }

    private TimeControls getRandomTimeControls() {
       // Log.d("heh: ", "i next");
         int[] i = {rng.nextInt(30)+1,rng.nextInt(10)+1,rng.nextInt(9)+1,rng.nextInt(5),rng.nextInt(4)+1,rng.nextInt(200)+1,rng.nextInt(500)+1};
        //int h = rng.nextInt(50);
        //int[] i = {6,6,7,3,3,150,555};
        //Log.d("heh: ", "" + i.length);
        TimeControls randomTimeControls = new TimeControls();
        randomTimeControls.setTimeControls(i);
        // rng.nextInt()+1;
       // Log.d("heh: ", "" + i.length);
        return  randomTimeControls;
    }

    private String getRandomDate() {
        int paiva = rng.nextInt(30)+1;
        int vuosi = rng.nextInt(28)+1990;
        String kk = "Jun";

        int i = rng.nextInt(6);
        if (i == 0) {kk = "Jan"; }
        else if (i==1) {kk = "Feb"; }
        else if (i == 2) {kk = "May"; }
        else if (i==3) {kk = "Aug"; }
        else if ( i == 4) { kk = "Apr"; }

        return paiva + "-" + kk + "-" + vuosi;
    }
}
