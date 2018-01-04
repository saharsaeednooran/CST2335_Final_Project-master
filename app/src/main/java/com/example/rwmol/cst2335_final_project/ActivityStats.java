package com.example.rwmol.cst2335_final_project;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This Activity is designed to let users watch their total activity duration
 * during the current and previous month.
 *
 * @author Sahar Saeednooran
 * @version 1.0
 *
 */
public class ActivityStats extends Activity {

    private static ArrayList<ArrayList<String>> activityList = new ArrayList<>();
    private ProgressBar normProgBar;
    private Button okBtn;
    private TextView currentMonthTextView, prevMonthTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        okBtn= findViewById(R.id.okButton);
        currentMonthTextView= findViewById(R.id.currentMonthTextView);
        prevMonthTextView= findViewById(R.id.prevMonthTextView);
        normProgBar = findViewById(R.id.progressBar);
        normProgBar.setVisibility(View.VISIBLE);
        normProgBar.setMax(100);

        new ActivityStatsQuery().execute(null, null, null);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    /**
     * Calculates the total monthly exercise in the background
     */
    private class ActivityStatsQuery extends AsyncTask<String, Integer, String> {

        int currentMonthTotal = 0;  int prevMonthTotal = 0;

        @Override
        protected String doInBackground(String... strings) {

            //Read from DB
            ActivityDatabaseHelper activityDatabaseHelper = new ActivityDatabaseHelper(ActivityStats.this);
            SQLiteDatabase activityDB = activityDatabaseHelper.getWritableDatabase();
            final ContentValues cValues = new ContentValues();
            Cursor cursor = activityDB.query(ActivityDatabaseHelper.TABLE_NAME, new String[]{ActivityDatabaseHelper.COL_DATE, ActivityDatabaseHelper.COL_PROGRESS}, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                for (int row = 0; row < cursor.getCount(); row++) {
                    activityList.add(new ArrayList<String>());
                    for (int col = 0; col < cursor.getColumnCount(); col++) {
                        activityList.get(row).add(cursor.getString(col));
                    }
                    cursor.moveToNext();
                }
            }

            int counter=1;
                for (ArrayList<String> activity : activityList) {
                    String currentDate = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
                    int currentInt = Integer.parseInt(currentDate.substring(0, 6));
                    String activityDate = activity.get(0);
                    int activityInt = Integer.parseInt(activityDate.substring(0, 6));

                    if (currentInt == activityInt) {
                        int currentMonth = activity.get(1).isEmpty() ? 0 : Integer.parseInt(activity.get(1));
                        currentMonthTotal += currentMonth;
                    }
                    if (currentInt - 1 == activityInt) {
                        int prevMonth = activity.get(1).isEmpty() ? 0 : Integer.parseInt(activity.get(1));
                        prevMonthTotal += prevMonth;
                    }

                    float progress = (1/Float.valueOf(activityList.size()) * counter*100);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publishProgress((int)progress);
                    counter++;
                }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            normProgBar.setProgress(values[0]);
            normProgBar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            currentMonthTextView.setText(currentMonthTotal+ " min");
            prevMonthTextView.setText(prevMonthTotal+ " min");
            normProgBar.setVisibility(View.INVISIBLE);

        }

    }


}
