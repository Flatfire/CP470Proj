package com.cp470.healthyhawk;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ExerciseLogDatabaseHelper extends SQLiteOpenHelper {
    // Constants
    public static final String ACTIVITY_NAME = "ExerciseLogDatabaseHelper";
    public static final String DATABASE_NAME = "exercise_log.db";
    public static int VERSION_NUM = 1;

    // Constants - Keys
    public static final String TABLE_NAME = "exercise_activities";
    public static final String KEY_ID = "id";
    public static final String KEY_TYPE = "type";
    public static final String KEY_STAT_NUM = "stat_num";
    public static final String KEY_STAT_NAME = "stat_name";
    public static final String KEY_DATE_TIME = "time_added";

    // Constants - SQL
    private static final String DATABASE_CREATE = "create table if not exists " + TABLE_NAME + "(" + KEY_ID + " integer primary key autoincrement, " + KEY_TYPE + " text not null, " + KEY_STAT_NUM + " text not null, " + KEY_STAT_NAME + " text not null, " + KEY_DATE_TIME + " text not null);";
    private static final String DATABASE_UPGRADE = "drop table if exists " + TABLE_NAME;
    public static final String DATABASE_SELECT_ALL = "select * from " + TABLE_NAME;

    public ExerciseLogDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(ACTIVITY_NAME, "Creating database");
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        db.execSQL(DATABASE_UPGRADE);
        onCreate(db);
    }
}
