package com.cp470.healthyhawk;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Weight_Database_Helper extends SQLiteOpenHelper {
    private static final String CLASS_NAME = "WeightDatabaseHelper";

    private static final String DATABASE_NAME = "HealthyHawk.db";
    private static final int VERSION_NUM = 1;

    public static final String KEY_ID = "_id";
    public static final String KEY_DATE_WEIGHT_LOGGED = "date_logged";
    public static final String KEY_WEIGHT = "weight";
    public static final String TABLE_NAME = "weight_log";

    public Weight_Database_Helper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(CLASS_NAME, "Calling onCreate");
        db.execSQL("CREATE TABLE "
                + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_DATE_WEIGHT_LOGGED + " DATE NOT NULL, "
                + KEY_WEIGHT + " FLOAT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i(CLASS_NAME, "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i(CLASS_NAME, "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
