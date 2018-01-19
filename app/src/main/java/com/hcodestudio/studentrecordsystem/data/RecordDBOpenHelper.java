package com.hcodestudio.studentrecordsystem.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.hcodestudio.studentrecordsystem.data.RecordContract.RecordsEntry.RECORD_ID;
import static com.hcodestudio.studentrecordsystem.data.RecordContract.RecordsEntry.ROLL_NUMBER;
import static com.hcodestudio.studentrecordsystem.data.RecordContract.RecordsEntry.STUDENT_MARK;
import static com.hcodestudio.studentrecordsystem.data.RecordContract.RecordsEntry.STUDENT_NAME;
import static com.hcodestudio.studentrecordsystem.data.RecordContract.RecordsEntry.TABLE_NAME;

/**
 * Created by hassan on 11/27/2017.
 */

public class RecordDBOpenHelper extends SQLiteOpenHelper {

    //Constants for db name and version
    private static final String DATABASE_NAME = "records.db";
    private static final int DATABASE_VERSION = 4;

    //SQL to create table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ROLL_NUMBER + " INTEGER NOT NULL , " +
                    STUDENT_NAME + " TEXT NOT NULL,  " +
                    STUDENT_MARK + " INTEGER NOT NULL" + ", UNIQUE(" + ROLL_NUMBER + ")" +
                    ")";

    public RecordDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
