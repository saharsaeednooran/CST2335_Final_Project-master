package com.example.rwmol.cst2335_final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A Database Helper which extends SQLiteOpenHelper to create and access a custom Activity Database.
 *
 * @author Sahar Saeednooran
 * @version 1.0
 */

public class ActivityDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Activity.db" ;
    public static final String TABLE_NAME = "Activity_TABLE" ;
    public static final int VERSION_NUM = 210;


    // column names
    public static final  String COL_ID = "_id";
    public static final  String COL_COMMENT = "_comment";
    public static final  String COL_PROGRESS= "_progress";
    public static final  String COL_EXERCISETYPE = "_exerciseType";
    public static final  String COL_DATE ="_date" ;


    //  SQL Instructions: Create table
    private static final String CREATE_TABLE_MESSAGES = "create table "  + TABLE_NAME  +
            " ( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_COMMENT + " TEXT, "
            + COL_DATE + " TEXT, "
            + COL_EXERCISETYPE + " TEXT, "
            + COL_PROGRESS + " INTEGER "
            + " ); " ;

    /**
     * Constructor that accesses the "Activity.db" Database.
     * @param ctx
     */
    public ActivityDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    /**
     * Creates a "Activity_TABLE" table if it doesn't already exist.
     * @param db the "Activity.db" database created by the ActivityDatabaseHelper constructor.
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MESSAGES);
    }

    @Override
    /**
     * Drops an older version of the "Activity_TABLE" table and creates a new database using that new VERSION_NUM.
     * @param db the "Activity.db" database created by the ActivityDatabaseHelper constructor.
     * @param oldVersion the version number of the older version of the Activity Database.
     * @param newVersion the version number of the new version of the Activity Database.
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
