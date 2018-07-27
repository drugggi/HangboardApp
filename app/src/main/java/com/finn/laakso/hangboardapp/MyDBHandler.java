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
    private static final String TABLE_WORKOUTS = "workouts";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_HANGBOARD = "hangboardname";

    private static final String COLUMN_HOLDNUMBERS = "holdnumbers";
    private static final String COLUMN_GRIPTYPES = "griptypes";
    private static final String COLUMN_DIFFICULTIES = "difficulties";

    private static final String COLUMN_GRIPLAPS = "griplaps";
    private static final String COLUMN_HANGLAPS = "hanglaps";
    private static final String COLUMN_TIMEON = "timeon";
    private static final String COLUMN_TIMEOFF = "timeoff";
    private static final String COLUMN_SETS = "sets";
    private static final String COLUMN_REST = "rest";
    private static final String COLUMN_LONGREST = "longrest";

    private static final String COLUMN_HANGSCOMPLETED = "hangscompleted";
    private static final String COLUMN_ISHIDDEN = "ishidden";
    private static final String COLUMN_DESCRIPTION = "description";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    // Creates the database which holds information from workouts. Workout information consist of
    // date, hangboard used, various time controls, holds used and how successfull each hang was
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

                + COLUMN_HANGSCOMPLETED + " TEXT,"
                + COLUMN_ISHIDDEN + " INTEGER,"
                + COLUMN_DESCRIPTION + " TEXT" + ")";

        db.execSQL(CREATE_PRODUCTS_TABEL);
    }
    /* ONUPGRADE() METHOD IS CALLED WHEN THE HANDLER IS INVOKED WITH GREATER DATABASE VERSION NUMBER
    FROM THE ON PREVIOUSLY USED. THE EXACT STEPS TO BE PERFORMED IN THIS INSTANCE WILL BE APPLICATION
     SPECIFIC, SO FOR THE PURPOSES OF THIS TEST WE WILL SIMPLY REMOVE THE OLD DATABASE AND CREATE A NEW ONE:
    */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_WORKOUTS);
        onCreate(db);

    }

    // addHangboardWorkout puts single workout into database int values hangsComlpeted, hold number,
    // grip style and difficulty are converted into String
    public void addHangboardWorkout(long date, String hangboardName, TimeControls timeControls,
                                    ArrayList<Hold> workoutHolds, int[] completed, String workoutDescription) {

        StringBuilder holdNumbers= new StringBuilder();
        StringBuilder gripTypes = new StringBuilder();
        StringBuilder difficulties = new StringBuilder();

        StringBuilder hangsCompleted = new StringBuilder();

        // WorkoutHolds integer information will be converted to Strings -> 5,1,4,5,6,7,....., etc
        for (int i: completed) {
            hangsCompleted.append(i+",");
        }

        for (Hold currentHold: workoutHolds) {
            holdNumbers.append(currentHold.getHoldNumber()+ ",");
            gripTypes.append(currentHold.getGripStyleInt() + ",");
            difficulties.append(currentHold.getHoldValue() + ",");

        }

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
        // Every workout will be visible at first
        values.put(COLUMN_ISHIDDEN,0);
        values.put(COLUMN_DESCRIPTION, workoutDescription);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_WORKOUTS,null, values);
        db.close();

    }

    // getListContents returns cursor that helps move around database
    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WORKOUTS, null);
        return cursor;
    }

    public Cursor getSortedContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,null,null,null,null,COLUMN_DATE + " DESC",null);
        return cursor;
    }

    public Cursor getNonHiddenContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        return cursor;
    }

    public Cursor getHiddenContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,null,null,null,null,COLUMN_DATE + " DESC",null);
        return cursor;
    }

    // this helps to test database
    public void DELETEALL() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_WORKOUTS);
        onCreate(db);
    }

    // Deletes a single workout entry
    // THIS DOES NOT TAKE INTO ACCOUNT IF INCLUDEHIDDEN PARAMETER IS TRUE OR FALSE, CONCIDER SAFER OPTION!!
    public void delete(int position) {

        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor data = db.rawQuery("SELECT * FROM " + TABLE_WORKOUTS, null);
        Cursor cursor = db.query(TABLE_WORKOUTS,null,null,null,null,null,COLUMN_DATE + " DESC",null);

        if (cursor.move(position)) {
            int deleteID = cursor.getInt(0);
            String query = "DELETE FROM " + TABLE_WORKOUTS + " WHERE " + COLUMN_ID + " = \"" + deleteID + "\"";
            db.execSQL(query);
        }

        db.close();

    }



    // Checks how many workouts there are in database
    public int lookUpWorkoutCount() {

        String countQuery = "SELECT  * FROM " + TABLE_WORKOUTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }



    public int lookUpUnHiddenWorkoutCount() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        return cursor.getCount();

    }

    public int lookUpId(int position, boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        int id = 0;

        if (cursor.move(position)) {
            id = cursor.getInt(0);
        }

        db.close();
        return id;

    }

    // returns date when the workout was done
    public long lookUpDate(int position, boolean includeHidden) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        long date= 0;

        try {
            if (cursor.move(position)) {
                date = Long.parseLong(cursor.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return date;
    }

    public String lookUpWorkoutDescription(int position, boolean includeHidden) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        String workoutDescription = "Err";

        if (cursor.move(position)) {
            workoutDescription = cursor.getString(15);
        }
        db.close();
        return workoutDescription;
    }

    public void updateWorkoutDescription(int position, String workoutDescription, boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        if (cursor.move(position)) {
            int columnID = cursor.getInt(0);

            String query = "UPDATE " + TABLE_WORKOUTS + " SET " + COLUMN_DESCRIPTION + " = \"" + workoutDescription + "\" WHERE " + COLUMN_ID + " =  \"" + columnID + "\"";
            db.execSQL(query);
        }

        db.close();
    }

    public void updateHangboardName(int position, String newBoardName, boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        if (cursor.move(position)) {
            int columnID = cursor.getInt(0);

            String query = "UPDATE " + TABLE_WORKOUTS + " SET " + COLUMN_HANGBOARD + " = \"" + newBoardName + "\" WHERE " + COLUMN_ID + " =  \"" + columnID + "\"";
            db.execSQL(query);
        }

        db.close();
    }

    public void updateDate(int position, long newDate, boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        if (cursor.move(position)) {
           // isHidden = cursor.getInt(14);
            int columnID = cursor.getInt(0);

            String query = "UPDATE " + TABLE_WORKOUTS + " SET " + COLUMN_DATE + " = \"" + newDate + "\" WHERE " + COLUMN_ID + " =  \"" + columnID + "\"";
            db.execSQL(query);
            //Log.e("Moved to"," " + position);
        }

        db.close();
    }

    public void hideWorkoutNumber(int position, boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        Log.e("hideworkoutnumber"," " );
        Log.e("Move pos"," " + position);
        int isHidden = 1;
        int columnID = 0;

        if (cursor.move(position)) {
            isHidden = cursor.getInt(14);
            columnID = cursor.getInt(0);

            if (isHidden == 0) {
                isHidden = 1;
            }
            else {
                isHidden = 0;
            }


            String query = "UPDATE " + TABLE_WORKOUTS + " SET " + COLUMN_ISHIDDEN + " = \"" + isHidden + "\" WHERE " + COLUMN_ID + " =  \"" + columnID + "\"";
            db.execSQL(query);
            Log.e("Moved to"," " + position);

        }

        db.close();
        return;

    }

    public void hideOrUnhideWorkoutNumber(int position) {


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,null,null,null,null,COLUMN_DATE + " DESC",null);

        int isHidden = 0;
        int columnID = 0;
        Log.e("hideORUNHIDEwoer"," ");
        Log.e("Move pos"," " + position);

        if (cursor.move(position)) {
            isHidden = cursor.getInt(14);
            columnID = cursor.getInt(0);

            if (isHidden == 0) {
                isHidden = 1;
            }
            else {
                isHidden = 0;
            }

            Log.e("set ishidden to"," " + isHidden);

            String query = "UPDATE " + TABLE_WORKOUTS + " SET " + COLUMN_ISHIDDEN + " = \"" + isHidden + "\" WHERE " + COLUMN_ID + " =  \"" + columnID + "\"";
            db.execSQL(query);
        }

        db.close();

    }

    public void setIsHidden(int position, int isHidden) {

        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WORKOUTS, null);
        Cursor cursor = db.query(TABLE_WORKOUTS,null,null,null,null,null,COLUMN_DATE + " DESC",null);

        int columnID;

        if (cursor.move(position)) {
            columnID = cursor.getInt(0);
        }
        else {
            db.close();
            return;
        }
        String query = "UPDATE " + TABLE_WORKOUTS + " SET " + COLUMN_ISHIDDEN + " = \"" + isHidden + "\" WHERE " + COLUMN_ID + " =  \"" + columnID + "\"";

        db.execSQL(query);

        db.close();

    }

    public boolean lookUpIsHidden(int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,null,null,null,null,COLUMN_DATE + " DESC",null);

        int isHidden;

        if (cursor.move(position)) {
            isHidden = cursor.getInt(14);
        }
        else {
            isHidden = 0;
        }

        db.close();

        if (isHidden == 0) {return false;}
        else {return true;}

    }

    // Returns the hangboard name which were used in the workout
    public String lookUpHangboard(int position, boolean includeHidden) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }
        String hangboard= "" ;

        if (cursor.move(position)) {
            hangboard = cursor.getString(2);
        }
        else {
            hangboard = "Error getting board";
        }
        db.close();
        return hangboard;

    }

    // Updates the completed hangs, which user can manually mark as done or not done
    public void updateCompletedHangs(int position, int[] completed, boolean includeHidden) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        int columnID;

        if (cursor.move(position)) {
            columnID = cursor.getInt(0);
        }
        else {
            db.close();
            return;
        }
        StringBuilder hangsCompleted = new StringBuilder();
        for (int i: completed) {
            hangsCompleted.append(i+",");
        }

        String updatedHangs = hangsCompleted.toString();
        String query = "UPDATE " + TABLE_WORKOUTS + " SET " + COLUMN_HANGSCOMPLETED + " = \"" + updatedHangs + "\" WHERE " + COLUMN_ID + " =  \"" + columnID + "\"";

        db.execSQL(query);

        db.close();
    }


    // return completed hangs which are stored as String
    public int[] lookUpCompletedHangs(int position, boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }
        String hangsCompletedFromDB = "";

        int[] hangsCompleted=new int[4];

        if (cursor.move(position)) {
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

    // return the used holds, each hold has a number, grip type and value.
    // In the ArrayList even valued holds are for left hand and odd values are for right hand
    public ArrayList<Hold> lookUpHolds(int position, boolean includeHidden) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }
        ArrayList<Hold> allHolds = new ArrayList<>();

        String allHoldNumbersFromDB="";
        String allGripTypesFromDB="";
        String allHoldValuesFromDB="";

        // A lot of parsing because hold information is stored as String
        if (cursor.move(position)) {
            allHoldNumbersFromDB = cursor.getString(3);
            allGripTypesFromDB = cursor.getString(4);
            allHoldValuesFromDB = cursor.getString(5);

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

        }
        db.close();

        return allHolds;

    }

    // Returns time controls that are used in workout
    public TimeControls lookUpTimeControls(int position, boolean includeHidden ) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }
        TimeControls timeControls = new TimeControls();

        if ( cursor.move(position) ) {
            timeControls.setGripLaps(cursor.getInt(6));
            timeControls.setHangLaps(cursor.getInt(7));
            timeControls.setTimeON(cursor.getInt(8));
            timeControls.setTimeOFF(cursor.getInt(9));
            timeControls.setRoutineLaps(cursor.getInt(10));
            timeControls.setRestTime(cursor.getInt(11));
            timeControls.setLongRestTime(cursor.getInt(12));

        }
        db.close();

        return timeControls ;

    }


}
