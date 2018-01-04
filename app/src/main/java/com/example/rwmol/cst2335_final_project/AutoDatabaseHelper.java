package com.example.rwmol.cst2335_final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(db);
    }
}
