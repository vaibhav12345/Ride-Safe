package com.example.bhavya.safego.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bhavya on 7/4/18.
 */

public class magnetometerDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "mag.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 2;

    // Constructor
    public magnetometerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + magnetometerContract.magnetometer.TABLE_NAME + " (" +
                magnetometerContract.magnetometer.X+ " FLOAT ," +
                magnetometerContract.magnetometer.Y + " FLOAT , " +
                magnetometerContract.magnetometer.Z + " FLOAT , " +
                magnetometerContract.magnetometer.COLUMN_TIMESTAMP + " STRING ," +
                magnetometerContract.magnetometer.LATITUDE+ " FLOAT ," +
                magnetometerContract.magnetometer.LONGITUDE+ " FLOAT " +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +magnetometerContract.magnetometer.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

