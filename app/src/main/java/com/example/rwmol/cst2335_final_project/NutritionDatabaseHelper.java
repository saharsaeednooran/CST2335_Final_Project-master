package com.example.rwmol.cst2335_final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * A Database Helper which extends SQLiteOpenHelper to create and access a custom Nutrition Database.
 * <p>Used by NutritionTrackerActivity to create a Nutrition Database if one doesn't exist as well as a "food" table and to store Nutrition Info.</p>
 *
 * @author Corey MacLeod
 * @version 3.0
 */
public class NutritionDatabaseHelper extends SQLiteOpenHelper {

    static final private String DATABASE_NAME = "Nutrition.db";
    static final private int VERSION_NUM = 3;
    static final String TABLE_NAME ="food";
    static final String KEY_ID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_CALORIES ="calories";
    static final String KEY_FAT = "fat";
    static final String KEY_CARBS="carb";
    static final String KEY_DATE = "input_time";
    static final private String KEY_CREATE = "CREATE TABLE "
            + TABLE_NAME + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT, "
            + KEY_CALORIES + " INTEGER, "
            + KEY_FAT + " INTEGER, "
            + KEY_CARBS + " INTEGER, "
            + KEY_DATE + " TEXT);";

    /**
     * Constructor that accesses the "Nutrition.db" Database linked the the NutritionTrackerActivity, or creates a new one if it doesn't exist.
     * @param ctx the instance NutritionTrackerActivity that is initializing this NutritionDatabaseHelper
     */
    NutritionDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * Creates a "food" table using the KEY_CREATE String as as query in teh event that such a table doesn't already exist.
     * @param db the "Nutrition.db" database created by the NutritionDatabaseHelper constructor.
     * {@inheritDoc}
     */
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(KEY_CREATE);
        Log.i("NutritionDatabaseHelper", "Calling onCreate");
    }


    /**
     * Drops an older version of the "food" table in the event of a new VERSION_NUM being set and creates a new database using that new VERSION_NUM.
     * @param db the "Nutrition.db" database created by the NutritionDatabaseHelper constructor.
     * @param oldVersion the version number of the older version of the Nutrition Database.
     * @param newVersion the version number of the new version of the Nutrition Database.
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i("NutritionDatabaseHelper", "Calling onUpgrade, oldVersion = " + oldVersion + " newVersion = " + newVersion);

    }

}
