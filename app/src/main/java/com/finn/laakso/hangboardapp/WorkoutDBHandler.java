package com.finn.laakso.hangboardapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Laakso on 14.6.2018.
 * WorkoutDBHandler creates an SQLite database consisting everything that need to be stored in a workout;
 * Hangboard, date, holds used, time controls, and how successful each hang were. WorkoutDBHandler provides
 * methods to get and update these properties.
 */

public class WorkoutDBHandler extends SQLiteOpenHelper {

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

    public WorkoutDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
        /*
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_WORKOUTS);
        onCreate(db);
        */
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
            hangsCompleted.append(i);
            hangsCompleted.append(",");
        }

        for (Hold currentHold: workoutHolds) {
            holdNumbers.append(currentHold.getHoldNumber() );
            holdNumbers.append(",");
            gripTypes.append(currentHold.getGripStyleInt() );
            gripTypes.append(",");
            difficulties.append(currentHold.getHoldValue() );
            difficulties.append(",");

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

    // this helps to test database
/*

    public void DELETEALL() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_WORKOUTS);
        onCreate(db);
    }
*/

    // Deletes a single workout entry
    // Does not check if COLUMN_ISHIDDEN truly is hidden so it can be deleted
    public void delete(int position,boolean includeHidden) {

        // Only hidden entries can be deleted
        if ( !includeHidden ) {return; }

        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor data = db.rawQuery("SELECT * FROM " + TABLE_WORKOUTS, null);
        Cursor cursor = db.query(TABLE_WORKOUTS,null,null,null,null,null,COLUMN_DATE + " DESC",null);
        try {
            if (cursor.move(position) ) {

                    int index = cursor.getColumnIndex(COLUMN_ID);
                    int deleteID = cursor.getInt(index);
                    String query = "DELETE FROM " + TABLE_WORKOUTS + " WHERE " + COLUMN_ID + " = \"" + deleteID + "\"";
                    db.execSQL(query);

            }
        } finally {
            db.close();
            cursor.close();
        }

    }

    // Checks how many workouts there are in database
    protected int lookUpWorkoutCount(boolean includeHidden) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }


    public int lookUpId(int position, boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        int id = 0;
        try {
            if (cursor.move(position)) {
                int index = cursor.getColumnIndex(COLUMN_ID);
                id = cursor.getInt(index);
            }
        } finally {
            cursor.close();
            db.close();
        }
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
        } finally {
            cursor.close();
        }
        db.close();

        return date;
    }

    public ArrayList<Long> lookUpAllDates(boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        ArrayList<Long> dbDateList = new ArrayList<>();
        try {
            int index = cursor.getColumnIndex( COLUMN_DATE );
            while (cursor.moveToNext() ) {
                dbDateList.add(cursor.getLong(index));
            }
        } finally {
            cursor.close();
            db.close();
        }
        return dbDateList;
    }

    public String lookUpWorkoutDescription(int position, boolean includeHidden) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        String workoutDescription = "Workout description";

        try {
            if (cursor.move(position)) {
                int index = cursor.getColumnIndex(COLUMN_DESCRIPTION);
                workoutDescription = cursor.getString(index);
            }
        } finally {
            cursor.close();
            db.close();
        }

        return workoutDescription;
    }

    public void updateWorkoutDescription(int position, String workoutDescription, boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }
        try {
            if (cursor.move(position)) {
                int index = cursor.getColumnIndex(COLUMN_ID);
                int columnID = cursor.getInt(index);

                String query = "UPDATE " + TABLE_WORKOUTS + " SET " + COLUMN_DESCRIPTION + " = \"" + workoutDescription + "\" WHERE " + COLUMN_ID + " =  \"" + columnID + "\"";
                db.execSQL(query);
            }
        } finally {
            cursor.close();
            db.close();
        }

    }

    public void updateHangboardName(int position, String newBoardName, boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }
        try {
            if (cursor.move(position)) {
                int index = cursor.getColumnIndex(COLUMN_ID);
                int columnID = cursor.getInt(index);

                String query = "UPDATE " + TABLE_WORKOUTS + " SET " + COLUMN_HANGBOARD + " = \"" + newBoardName + "\" WHERE " + COLUMN_ID + " =  \"" + columnID + "\"";
                db.execSQL(query);
            }
        } finally {
            cursor.close();
            db.close();
        }

    }

    public void updateDate(int position, long newDate, boolean includeHidden) {

        // Dates are represented in long format, valid dates between 1970 - 20xx
        if (newDate < 0) {
            newDate = 0;
        }
        else if (newDate > 4120720611736L) {
            newDate = 3120720611736L;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        try {
            if (cursor.move(position)) {
                int index = cursor.getColumnIndex(COLUMN_ID);
                int columnID = cursor.getInt(index);

                String query = "UPDATE " + TABLE_WORKOUTS + " SET " + COLUMN_DATE + " = \"" + newDate + "\" WHERE " + COLUMN_ID + " =  \"" + columnID + "\"";
                db.execSQL(query);
            }
        } finally {
            cursor.close();
            db.close();
        }
    }

    public void hideWorkoutNumber(int position, boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        int isHidden;
        int columnID;

        try {
            if (cursor.move(position)) {
                int indexHidden = cursor.getColumnIndex(COLUMN_ISHIDDEN);
                int indexID = cursor.getColumnIndex(COLUMN_ID);
                isHidden = cursor.getInt(indexHidden);
                columnID = cursor.getInt(indexID);

                if (isHidden == 0) {
                    isHidden = 1;
                } else {
                    isHidden = 0;
                }


                String query = "UPDATE " + TABLE_WORKOUTS + " SET " + COLUMN_ISHIDDEN + " = \"" + isHidden + "\" WHERE " + COLUMN_ID + " =  \"" + columnID + "\"";
                db.execSQL(query);

            }
        }finally {
            cursor.close();
            db.close();
        }

    }


    public boolean lookUpIsHidden(int position, boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }
        Integer isHidden;

        try {
            if (cursor.move(position)) {
                int index = cursor.getColumnIndex(COLUMN_ISHIDDEN);
                isHidden = cursor.getInt(index);
            } else {
                isHidden = 0;
            }
        } finally {
            cursor.close();
            db.close();
        }

        // Editor made me do this
        return !isHidden.equals(0);

    }


    // Returns the hangboard name which were used in the workout
    public String lookUpHangboard(int position, boolean includeHidden) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }
        String hangboard= "" ;

        try {
            if (cursor.move(position)) {
                int index = cursor.getColumnIndex( COLUMN_HANGBOARD );
                hangboard = cursor.getString(index);
            }
        } finally {
            cursor.close();
            db.close();
        }

        return hangboard;

    }

    public ArrayList<String> lookUpAllHangboards(boolean includeHidden) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        ArrayList<String> hangboardNames = new ArrayList<>();
        try {
            int index = cursor.getColumnIndex( COLUMN_HANGBOARD );
            while (cursor.moveToNext() ) {
                hangboardNames.add(cursor.getString(index));
            }

        } finally {
            cursor.close();
        }
        db.close();
        return hangboardNames;


    }

    // Updates the completed hangs, which user can manually mark as done or not done
    public void updateCompletedHangs(int position, int[] completed, boolean includeHidden) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        int columnID;


        try {

            if (cursor.move(position)) {
                int index = cursor.getColumnIndex(COLUMN_ID);
                columnID = cursor.getInt(index);


                StringBuilder hangsCompleted = new StringBuilder();
                for (int i : completed) {
                    hangsCompleted.append(i);
                    hangsCompleted.append(",");
                }

                String updatedHangs = hangsCompleted.toString();
                String query = "UPDATE " + TABLE_WORKOUTS + " SET " + COLUMN_HANGSCOMPLETED + " = \"" + updatedHangs + "\" WHERE " + COLUMN_ID + " =  \"" + columnID + "\"";

                db.execSQL(query);
            }

        } finally {
            cursor.close();
            db.close();
        }

    }


    // return completed hangs which are stored as String
    public int[] lookUpCompletedHangs(int position, boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        String hangsCompletedFromDB;
        int[] hangsCompleted = new int[1];

        try {
            if (cursor.move(position)) {
                int index = cursor.getColumnIndex(COLUMN_HANGSCOMPLETED);
                 hangsCompletedFromDB = cursor.getString(index);

                String[] s = hangsCompletedFromDB.split(",");

                hangsCompleted = new int[s.length];
                for (int i = 0; i < s.length; i++) {
                    hangsCompleted[i] = Integer.parseInt(s[i]);
                }

            }
        } finally {
            cursor.close();
            db.close();
        }
        return hangsCompleted;
    }

    public ArrayList<int[]> lookUpAllCompletedHangs(boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        String hangsCompletedFromDB;
        String[] temp;
        int[] hangsCompleted;
        ArrayList<int[]> allCompletedArrays = new ArrayList<>();

        try {
            int index = cursor.getColumnIndex(COLUMN_HANGSCOMPLETED);
            while (cursor.moveToNext() ) {
                hangsCompletedFromDB = cursor.getString(index);

                temp = hangsCompletedFromDB.split(",");

                hangsCompleted = new int[temp.length];
                for (int i = 0 ; i < temp.length ; i++) {
                    hangsCompleted[i] = Integer.parseInt(temp[i]);
                }
                allCompletedArrays.add(hangsCompleted);
            }
        } finally {
            cursor.close();
            db.close();
        }

        return allCompletedArrays;

    }


    // return the used holds, each hold has a number, grip type and value.
    // In the ArrayList even valued holds are for left hand and odd values are for right hand
    public ArrayList<Hold> lookUpWorkoutHolds(int position, boolean includeHidden) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }
        ArrayList<Hold> allHolds = new ArrayList<>();

        String allHoldNumbersFromDB;
        String allGripTypesFromDB;
        String allHoldValuesFromDB;

        // A lot of parsing because hold information is stored as String
        try {
            if (cursor.move(position)) {
                int indexHoldNumbers = cursor.getColumnIndex(COLUMN_HOLDNUMBERS);
                int indexGripTypes = cursor.getColumnIndex(COLUMN_GRIPTYPES);
                int indexHoldValues = cursor.getColumnIndex(COLUMN_DIFFICULTIES);
                allHoldNumbersFromDB = cursor.getString(indexHoldNumbers);
                allGripTypesFromDB = cursor.getString(indexGripTypes);
                allHoldValuesFromDB = cursor.getString(indexHoldValues);

                String[] s = allHoldNumbersFromDB.split(",");

                int[] holdNumbers = new int[s.length];
                for (int i = 0; i < s.length; i++) {
                    holdNumbers[i] = Integer.parseInt(s[i]);
                }

                s = allGripTypesFromDB.split(",");

                int[] gripTypes = new int[s.length];
                for (int i = 0; i < s.length; i++) {
                    gripTypes[i] = Integer.parseInt(s[i]);
                }

                s = allHoldValuesFromDB.split(",");

                int[] holdValues = new int[s.length];
                for (int i = 0; i < s.length; i++) {
                    holdValues[i] = Integer.parseInt(s[i]);
                }

                Hold tempHold;
                for (int i = 0; i < s.length; i++) {
                    tempHold = new Hold(holdNumbers[i]);
                    tempHold.setGripType(gripTypes[i]);
                    tempHold.setHoldValue(holdValues[i]);

                    allHolds.add(tempHold);
                }

            }
        } finally {
            cursor.close();
            db.close();
        }

        return allHolds;

    }


    public ArrayList<Integer> lookUpAllWorkoutIDs(boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        int workoutIDFromDB;
        ArrayList<Integer> allWorkoutIDs = new ArrayList<>();

        try {
            int index = cursor.getColumnIndex(COLUMN_ID);
            while (cursor.moveToNext() ) {
                workoutIDFromDB = cursor.getInt(index);

                allWorkoutIDs.add(workoutIDFromDB);
            }
        } finally {
            cursor.close();
            db.close();
        }

        return allWorkoutIDs;

    }


    public ArrayList<String> lookUpAllCompletedAsString(boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        String hangsCompletedFromDB;
        ArrayList<String> allCompletedHangs = new ArrayList<>();

        try {
            int index = cursor.getColumnIndex(COLUMN_HANGSCOMPLETED);
            while (cursor.moveToNext() ) {
                hangsCompletedFromDB = cursor.getString(index);

                allCompletedHangs.add(hangsCompletedFromDB);
            }
        } finally {
            cursor.close();
            db.close();
        }

        return allCompletedHangs;

    }


    public ArrayList<Boolean> lookUpAllWorkoutIsHidden(boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        int isHidden;
        ArrayList<Boolean> allWorkoutIsHidden = new ArrayList<>();

        try {
            int index = cursor.getColumnIndex(COLUMN_ISHIDDEN);
            while (cursor.moveToNext() ) {
                isHidden = cursor.getInt(index);

                if (isHidden == 0) {
                    allWorkoutIsHidden.add(false);
                }
                else {
                    allWorkoutIsHidden.add(true);
                }
            }
        } finally {
            cursor.close();
            db.close();
        }

        return allWorkoutIsHidden;

    }


    public ArrayList<String> lookUpAllWorkoutDescriptions(boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        String workoutDescriptionsFromDB;
        ArrayList<String> allWorkoutDescriptions = new ArrayList<>();

        try {
            int index = cursor.getColumnIndex(COLUMN_DESCRIPTION);
            while (cursor.moveToNext() ) {
                workoutDescriptionsFromDB = cursor.getString(index);

                allWorkoutDescriptions.add(workoutDescriptionsFromDB);
            }
        } finally {
            cursor.close();
            db.close();
        }

        return allWorkoutDescriptions;

    }


    public ArrayList<String> lookUpAllWorkoutHoldNumbers(boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        String holdNumbersFromDB;
        ArrayList<String> allHoldNumbersFromDB = new ArrayList<>();


        try {

            while (cursor.moveToNext()) {

                int indexHoldNumbers = cursor.getColumnIndex(COLUMN_HOLDNUMBERS);
                holdNumbersFromDB = cursor.getString(indexHoldNumbers);

                allHoldNumbersFromDB.add(holdNumbersFromDB);

            }
        } finally {
            db.close();
            cursor.close();
        }

        return allHoldNumbersFromDB;
    }

    public ArrayList<String> lookUpAllWorkoutHoldGripTypes(boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        String holdGripTypesFromDB;
        ArrayList<String> allHoldGripTypesFromDB = new ArrayList<>();


        try {

            while (cursor.moveToNext()) {

                int indexHoldNumbers = cursor.getColumnIndex(COLUMN_GRIPTYPES);
                holdGripTypesFromDB = cursor.getString(indexHoldNumbers);

                allHoldGripTypesFromDB.add(holdGripTypesFromDB);

            }
        } finally {
            db.close();
            cursor.close();
        }

        return allHoldGripTypesFromDB;
    }

    public ArrayList<String> lookUpAllWorkoutHoldDifficulties(boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        String holdDifficultiesFromDB;
        ArrayList<String> allHoldDifficultiesFromDB = new ArrayList<>();


        try {

            while (cursor.moveToNext()) {

                int indexHoldNumbers = cursor.getColumnIndex(COLUMN_DIFFICULTIES);
                holdDifficultiesFromDB = cursor.getString(indexHoldNumbers);

                allHoldDifficultiesFromDB.add(holdDifficultiesFromDB);

            }
        } finally {
            db.close();
            cursor.close();
        }

        return allHoldDifficultiesFromDB;
    }


    public ArrayList<ArrayList<Hold>> lookUpAllWorkoutHolds(boolean includeHidden) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        String allHoldNumbersFromDB;
        String allGripTypesFromDB;
        String allHoldValuesFromDB;

        ArrayList<ArrayList<Hold>> allWorkoutHolds = new ArrayList<>();
        ArrayList<Hold> workoutHolds;

        try {
            while  (cursor.moveToNext() ) {
                workoutHolds = new ArrayList<>();

                int indexHoldNumbers = cursor.getColumnIndex(COLUMN_HOLDNUMBERS);
                int indexGripTypes = cursor.getColumnIndex(COLUMN_GRIPTYPES);
                int indexHoldValues = cursor.getColumnIndex(COLUMN_DIFFICULTIES);
                allHoldNumbersFromDB = cursor.getString(indexHoldNumbers);
                allGripTypesFromDB = cursor.getString(indexGripTypes);
                allHoldValuesFromDB = cursor.getString(indexHoldValues);

                String[] s = allHoldNumbersFromDB.split(",");

                int[] holdNumbers = new int[s.length];
                for (int i = 0; i < s.length; i++) {
                    holdNumbers[i] = Integer.parseInt(s[i]);
                }

                s = allGripTypesFromDB.split(",");

                int[] gripTypes = new int[s.length];
                for (int i = 0; i < s.length; i++) {
                    gripTypes[i] = Integer.parseInt(s[i]);
                }

                s = allHoldValuesFromDB.split(",");

                int[] holdValues = new int[s.length];
                for (int i = 0; i < s.length; i++) {
                    holdValues[i] = Integer.parseInt(s[i]);
                }

                Hold tempHold;
                for (int i = 0; i < s.length; i++) {
                    tempHold = new Hold(holdNumbers[i]);
                    tempHold.setGripType(gripTypes[i]);
                    tempHold.setHoldValue(holdValues[i]);

                    workoutHolds.add(tempHold);
                }

                allWorkoutHolds.add(workoutHolds);

            }
        } finally {
            cursor.close();
            db.close();
        }

        return allWorkoutHolds;
    }

    // Returns time controls that are used in workout
    public TimeControls lookUpTimeControls(int position, boolean includeHidden ) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }
        TimeControls timeControls = new TimeControls();

        try {
            if (cursor.move(position)) {
                int indexGL = cursor.getColumnIndex(COLUMN_GRIPLAPS);
                int indexHL = cursor.getColumnIndex(COLUMN_HANGLAPS);
                int indexTimeON = cursor.getColumnIndex(COLUMN_TIMEON);
                int indexTimeOFF = cursor.getColumnIndex(COLUMN_TIMEOFF);
                int indexLaps = cursor.getColumnIndex(COLUMN_SETS);
                int indexRest = cursor.getColumnIndex(COLUMN_REST);
                int indexLongRest = cursor.getColumnIndex(COLUMN_LONGREST);

                timeControls.setGripLaps(cursor.getInt(indexGL));
                timeControls.setHangLaps(cursor.getInt(indexHL));
                timeControls.setTimeON(cursor.getInt(indexTimeON));
                timeControls.setTimeOFF(cursor.getInt(indexTimeOFF));
                timeControls.setRoutineLaps(cursor.getInt(indexLaps));
                timeControls.setRestTime(cursor.getInt(indexRest));
                timeControls.setLongRestTime(cursor.getInt(indexLongRest));

            }
        } finally {
            cursor.close();
            db.close();
        }

        return timeControls ;

    }

    public ArrayList<TimeControls> lookUpAllTimeControls(boolean includeHidden) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,null,COLUMN_ISHIDDEN+"=0",null,null,null,COLUMN_DATE + " DESC",null);
        if (includeHidden) {
            cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, COLUMN_DATE + " DESC", null);
        }

        ArrayList<TimeControls> allTimeControls = new ArrayList<>();
        TimeControls tempControl;

        try {
            int indexGL = cursor.getColumnIndex(COLUMN_GRIPLAPS);
            int indexHL = cursor.getColumnIndex(COLUMN_HANGLAPS);
            int indexTimeON = cursor.getColumnIndex(COLUMN_TIMEON);
            int indexTimeOFF = cursor.getColumnIndex(COLUMN_TIMEOFF);
            int indexLaps = cursor.getColumnIndex(COLUMN_SETS);
            int indexRest = cursor.getColumnIndex(COLUMN_REST);
            int indexLongRest = cursor.getColumnIndex(COLUMN_LONGREST);

            while (cursor.moveToNext()) {
                tempControl = new TimeControls();
                tempControl.setGripLaps(cursor.getInt(indexGL));
                tempControl.setHangLaps(cursor.getInt(indexHL));
                tempControl.setTimeON(cursor.getInt(indexTimeON));
                tempControl.setTimeOFF(cursor.getInt(indexTimeOFF));
                tempControl.setRoutineLaps(cursor.getInt(indexLaps));
                tempControl.setRestTime(cursor.getInt(indexRest));
                tempControl.setLongRestTime(cursor.getInt(indexLongRest));

                allTimeControls.add(tempControl);
            }
        } finally {
            cursor.close();
            db.close();
        }
        return allTimeControls;
    }


}
