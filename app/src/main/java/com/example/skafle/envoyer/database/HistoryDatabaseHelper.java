package com.example.skafle.envoyer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aneeshjindal on 4/20/16.
 */
public class HistoryDatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Comments.db";

    public static final String TABLE_NAME = "Contacts";

    /**Columns*/
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String DATA = "data";
    public static final String DATE = "date";
    public static final String[] ALL_COLUMNS = {_ID, NAME, DATA, DATE};
    /**End column stuff*/

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    NAME + TEXT_TYPE + COMMA_SEP +
                    DATA + TEXT_TYPE + COMMA_SEP +
                    DATE + INTEGER_TYPE +
                    " )";


    public HistoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
