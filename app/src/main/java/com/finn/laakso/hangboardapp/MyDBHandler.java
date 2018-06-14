package com.finn.laakso.hangboardapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Laakso on 14.6.2018.
 */

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hangboardWorkout.db";
    public static final String TABLE_WORKOUTS = "workouts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_HANGBOARD = "hangboardname";
    public static final String COLUMN_TUT = "timeunertension";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABEL = "CREATE TABLE " + TABLE_WORKOUTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_HANGBOARD + " TEXT,"
                + COLUMN_TUT + " INTEGER" + ")";
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

    public void addHangboardWorkout(String hangboardName, int time_under_tension) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_HANGBOARD, hangboardName);
        values.put(COLUMN_TUT,time_under_tension);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_WORKOUTS,null, values);
        db.close();

    }

    public void DELETEALL() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_WORKOUTS);
        onCreate(db);
    }

    public String lookUpTUT(int position) {

        String query = "SELECT * FROM " + TABLE_WORKOUTS + " WHERE " + COLUMN_ID + " =  \"" + position + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        String name="ei oleee";
        int tut=666;

        if ( cursor.moveToFirst() ) {
            cursor.moveToFirst();
            name = cursor.getString(1);
            tut = Integer.parseInt(cursor.getString(2));

        }
        Log.e("name: ", name);
        Log.e("tut: ", "" + tut);
        db.close();

        return name + " tut: " + tut;

    }

}
