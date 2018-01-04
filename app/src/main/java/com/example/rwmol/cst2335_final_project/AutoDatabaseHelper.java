package com.example.rwmol.cst2335_final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A Database Helper which extends SQLiteOpenHelper to create and access a custom Automobile Database.
 *
 * @author Ryan Molitor
 * @version 1.2
 */
public class AutoDatabaseHelper extends SQLiteOpenHelper {

    protected static final String ACTIVITY_NAME = "AutoDatabaseHelper";
    private static final String DATABASE_NAME = "AutoPurchase.db";
    private static final int VERSION_NUM = 5;

    static final String TABLE_NAME = "AUTO_PURCHASE_LOG";
    static final String KEY_ID = "_ID";
    static final String DATE = "Date";
    static final String LITRES = "Litres";
    static final String COST = "Cost";
    static final String KILOMETERS = "Kilometers";

    /**
     * Constructor that accesses the "AutoPurchase.db" Database linked the the AutomobileActivity, or creates a new one if it doesn't exist.
     * @param context the instance AutomobileActivity that is initializing this AutomobileDatabaseHelper
     */
    AutoDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "
                + TABLE_NAME
                + "("
                + KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE
                + " VARCHAR(10), "
                + LITRES
                + " DOUBLE, "
                + COST
                + " DECIMAL(3,2), "
                + KILOMETERS
                + " INTEGER);"
        );
    }

    /**
     * Drops an older version of the "food" table in the event of a new VERSION_NUM being set and creates a new database using that new VERSION_NUM.
     * @param db the "AutoPurchase.db" database created by the AutomobileDatabaseHelper constructor.
     * @param oldVersion the version number of the older version of the AutoPurchase Database.
     * @param newVersion the version number of the new version of the AutoPurchase Database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(db);
    }
}
