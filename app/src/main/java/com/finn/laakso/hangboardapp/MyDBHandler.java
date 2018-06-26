package com.finn.laakso.hangboardapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Laakso on 14.6.2018.
 */

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hangboardWorkout.db";
    public static final String TABLE_WORKOUTS = "workouts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_HANGBOARD = "hangboardname";

    public static final String COLUMN_HOLDNUMBERS = "holdnumbers";
    public static final String COLUMN_GRIPTYPES = "griptypes";
    public static final String COLUMN_DIFFICULTIES = "difficulties";

    public static final String COLUMN_GRIPLAPS = "griplaps";
    public static final String COLUMN_HANGLAPS = "hanglaps";
    public static final String COLUMN_TIMEON = "timeon";
    public static final String COLUMN_TIMEOFF = "timeoff";
    public static final String COLUMN_SETS = "sets";
    public static final String COLUMN_REST = "rest";
    public static final String COLUMN_LONGREST = "longrest";

    public static final String COLUMN_HANGSCOMPLETED = "hangscompleted";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABEL = "CREATE TABLE " + TABLE_WORKOUTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_DATE + " INTEGER,"
                + COLUMN_HANGBOARD + " TEXT,"
                + COLUMN_HOLDNUMBERS + " TEXT,"
                + COLUMN_GRIPTYPES + " TEXT,"
                + COLUMN_DIFFICULTIES + " TEXT,"

                + COLUMN_GRIPLAPS + " INTEGER,"
                + COLUMN_HANGLAPS + " INTEGER,"
                + COLUMN_TIMEON + " INTEGER,"
                + COLUMN_TIMEOFF + " INTEGER,"
                + COLUMN_SETS + " INTEGER,"
                + COLUMN_REST + " INTEGER,"
                + COLUMN_LONGREST + " INTEGER,"
                + COLUMN_HANGSCOMPLETED + " TEXT" + ")";

        db.execSQL(CREATE_PRODUCTS_TABEL);
    }
    /* ONUPGRADE() METHOD IS CALLED WHEN THE HANDLER IS INVOKED WITH GREATER DATABASE VERSION NUMBER
    FROM THE ON PREVIOUSLY USED. THE EXACT STEPS OT BE PERFORMED IN THIS INSTANCE WILL BE APPLICATION
     SPECIFIC, SO FOR THE PURPOSES OF THIS TEST WE WILL SIMPLY REMOVE THE OLD DATABASE AND CREATE A NEW ONE:
    */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_WORKOUTS);
        onCreate(db);

    }

    public void addHangboardWorkout(long date, String hangboardName, TimeControls timeControls, ArrayList<Hold> workoutHolds, int[] completed) {

        StringBuilder holdNumbers= new StringBuilder();
        StringBuilder gripTypes = new StringBuilder();
        StringBuilder difficulties = new StringBuilder();

        StringBuilder hangsCompleted = new StringBuilder();
        for (int i: completed) {
            hangsCompleted.append(i+",");
        }

        for (Hold h: workoutHolds) {
            holdNumbers.append(h.getHoldNumber()+ ",");
            gripTypes.append(h.getGripStyleInt() + ",");
            difficulties.append(h.getHoldValue() + ",");

        }

        Log.e("hangs completed"," as string: " + hangsCompleted.toString());

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE,date);
        values.put(COLUMN_HANGBOARD, hangboardName);
        values.put(COLUMN_HOLDNUMBERS, holdNumbers.toString());
        values.put(COLUMN_GRIPTYPES, gripTypes.toString());
        values.put(COLUMN_DIFFICULTIES, difficulties.toString());

        values.put(COLUMN_GRIPLAPS, timeControls.getGripLaps());
        values.put(COLUMN_HANGLAPS, timeControls.getHangLaps());
        values.put(COLUMN_TIMEON, timeControls.getTimeON());
        values.put(COLUMN_TIMEOFF, timeControls.getTimeOFF());
        values.put(COLUMN_SETS, timeControls.getRoutineLaps());
        values.put(COLUMN_REST, timeControls.getRestTime());
        values.put(COLUMN_LONGREST, timeControls.getLongRestTime());

        values.put(COLUMN_HANGSCOMPLETED,hangsCompleted.toString());
        //values.put(COLUMN_TUT,time_under_tension);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_WORKOUTS,null, values);
        db.close();

    }

    public void DELETEALL() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_WORKOUTS);
        onCreate(db);
    }

    public int lookUpWorkoutCount() {

        String countQuery = "SELECT  * FROM " + TABLE_WORKOUTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }

    public long lookUpDate(int position) {

        String query = "SELECT * FROM " + TABLE_WORKOUTS + " WHERE " + COLUMN_ID + " =  \"" + position + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        long date= 0;


        try {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                date = Long.parseLong(cursor.getString(1));
            }
        } catch (Exception e) {
            Log.e(" test" , " stacktrace");
            e.printStackTrace();
        }
        db.close();
        return date;
    }

    public String lookUpHangboard(int position) {
        String query = "SELECT * FROM " + TABLE_WORKOUTS + " WHERE " + COLUMN_ID + " =  \"" + position + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        String hangboard= "" ;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            hangboard = cursor.getString(2);
        }
        db.close();
        return hangboard;

    }

    public void updateCompletedHangs(int position, int[] completed) {

        /*
        int[] completed = lookUpCompletedHangs(position);
        for (int i = 0; i < completed.length/2 ; i++) {
            completed[i] = 0;
        }

        //Log.e(" test" , " " + 1);
*/
        StringBuilder hangsCompleted = new StringBuilder();
        for (int i: completed) {
            hangsCompleted.append(i+",");
        }
        //Log.e(" test" , " " + 2);

        String updatedHangs = hangsCompleted.toString();

        //Log.e(" test" , " " + 3);
        String query = "UPDATE " + TABLE_WORKOUTS + " SET " + COLUMN_HANGSCOMPLETED + " = \"" + updatedHangs + "\" WHERE " + COLUMN_ID + " =  \"" + position + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        //Log.e(" test" , " " + 4);
        db.execSQL(query);
        // Cursor cursor = db.rawQuery(query, null);
        db.close();
    }

    public int[] lookUpCompletedHangs(int position) {
        String query = "SELECT * FROM " + TABLE_WORKOUTS + " WHERE " + COLUMN_ID + " =  \"" + position + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        String hangsCompletedFromDB = "";

        int[] hangsCompleted=new int[4];

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            hangsCompletedFromDB = cursor.getString(13);


            String[] s = hangsCompletedFromDB.split(",");

            hangsCompleted = new int[s.length];
            for (int i = 0; i < s.length; i++) {
                hangsCompleted[i] = Integer.parseInt(s[i]);
            }

        }
        db.close();
        return hangsCompleted;
    }
    public ArrayList<Hold> lookUpHolds(int position) {
        String query = "SELECT * FROM " + TABLE_WORKOUTS + " WHERE " + COLUMN_ID + " =  \"" + position + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Hold> allHolds = new ArrayList<>();

        String allHoldNumbersFromDB="";
        String allGripTypesFromDB="";
        String allHoldValuesFromDB="";

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            allHoldNumbersFromDB = cursor.getString(3);
            allGripTypesFromDB = cursor.getString(4);
            allHoldValuesFromDB = cursor.getString(5);

/*
        Log.e("numbers: ", allHoldNumbersFromDB);
        Log.e("griptypes: ", allGripTypesFromDB);
        Log.e("holdvalues :",allHoldValuesFromDB);
*/
        String[] s = allHoldNumbersFromDB.split(",");

        int[] holdNumbers = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            holdNumbers[i] = Integer.parseInt(s[i]);
        }

        s = allGripTypesFromDB.split(",");

        int[] gripTypes = new int[s.length];
        for (int i = 0 ; i < s.length; i++) {
            gripTypes[i] = Integer.parseInt(s[i]);


        }

        s = allHoldValuesFromDB.split(",");

        int[] holdValues = new int[s.length];
        for (int i = 0 ; i < s.length; i++) {
            holdValues[i] = Integer.parseInt(s[i]);
        }

        Hold tempHold;
        for(int i =0 ; i < s.length ; i++) {
            tempHold = new Hold(holdNumbers[i]);
            tempHold.setGripType(gripTypes[i]);
            tempHold.setHoldValue(holdValues[i]);

            allHolds.add(tempHold);
        }

        //Log.e(" length", " : " + holdNumbers.length + " : " + gripTypes.length + " : " + holdValues.length);
        }
        db.close();

        Log.e("db handler"," holds size: " + allHolds.size());
        return allHolds;

    }

    public TimeControls lookUpTimeControls(int position) {

        String query = "SELECT * FROM " + TABLE_WORKOUTS + " WHERE " + COLUMN_ID + " =  \"" + position + "\"";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        TimeControls timeControls = new TimeControls();
        //int tut=666;

        if ( cursor.moveToFirst() ) {
            cursor.moveToFirst();

            timeControls.setGripLaps(cursor.getInt(6));
            timeControls.setHangLaps(cursor.getInt(7));
            timeControls.setTimeON(cursor.getInt(8));
            timeControls.setTimeOFF(cursor.getInt(9));
            timeControls.setRoutineLaps(cursor.getInt(10));
            timeControls.setRestTime(cursor.getInt(11));
            timeControls.setLongRestTime(cursor.getInt(12));

          //  tut = Integer.parseInt(cursor.getString(2));

        }
       // Log.e("name: ", timeControls.getGripMatrix(false));

        db.close();

        return timeControls ;

    }

}
