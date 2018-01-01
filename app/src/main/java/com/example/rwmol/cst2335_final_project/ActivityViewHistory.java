package com.example.rwmol.cst2335_final_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * This Activity is designed to displays the activities to users,
 * either ordered by time or as a daily summary of activities and time exercising.
 *
 * @author Sahar Saeednooran
 * @version 1.0
 *
 */
public class ActivityViewHistory extends Activity {

    private static ArrayList<ArrayList<String>> activityList = new ArrayList<>();
    private ArrayList<String> selectedActivity;
    Button sortByDateButton, sortByTimeButton, addActivityButton, viewMonthlyStatusButton;
    ListView activityListView;
    Cursor cursor;
    ActivityAdapter activityAdapter;
    ActivityDatabaseHelper activityDatabaseHelper;
    SQLiteDatabase activityDB;
    Boolean frameLayoutExists;
    ActivityFragment activityFragment;
    public static final int REQ_CODE = 10;
    public static final int RES_DEL_CODE = 30;
    public static final int RES_DEL_CODE_TABLET = 40;

    @Override
    /**
     * Creates ActivityView Activity and inflates to activity_view_history view
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        sortByDateButton = (Button) findViewById(R.id.sortByDateButton);
        sortByTimeButton = (Button) findViewById(R.id.sortByTimeButton);
        addActivityButton = (Button) findViewById(R.id.addActivityButton);
        viewMonthlyStatusButton = (Button) findViewById(R.id.viewMonthlyStatusButton);
        activityListView = (ListView) findViewById(R.id.activityListView);

        activityAdapter = new ActivityAdapter(this);
        activityListView.setAdapter(activityAdapter);

        //read from database
        activityDatabaseHelper = new ActivityDatabaseHelper(this);
        activityDB = activityDatabaseHelper.getWritableDatabase();

        activityList.clear();
        cursor = activityDB.query(ActivityDatabaseHelper.TABLE_NAME, new String[]{ActivityDatabaseHelper.COL_ID, ActivityDatabaseHelper.COL_COMMENT, ActivityDatabaseHelper.COL_DATE, ActivityDatabaseHelper.COL_EXERCISETYPE, ActivityDatabaseHelper.COL_PROGRESS}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            for (int row = 0; row < cursor.getCount(); row++) {
                activityList.add(new ArrayList<String>());
                for (int col = 0; col < cursor.getColumnCount(); col++) {
                    activityList.get(row).add(cursor.getString(col));
                }
                cursor.moveToNext();
            }
        }
        activityAdapter.notifyDataSetChanged();

        frameLayoutExists = (findViewById(R.id.frameLayout) != null) ? true : false;

        activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedActivity = activityList.get(position);
                //Tablet
                if (frameLayoutExists || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("selectedActivity", selectedActivity);
                    activityFragment = ActivityFragment.newInstance(ActivityViewHistory.this);//chatWindow not null, on tablet
                    activityFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.frameLayout, activityFragment).commit();
                }
                //Phone
                else {
                    Intent intent = new Intent(ActivityViewHistory.this, ActivityDetails.class);
                    intent.putStringArrayListExtra("selectedActivity", selectedActivity);
                    startActivityForResult(intent, REQ_CODE);
                }
            }
        });

        sortByDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(activityList, new Comparator<ArrayList<String>>() {
                    @Override
                    public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                        Integer arg1 = o1.get(2).trim().length() == 0 ? 0 : Integer.parseInt(o1.get(2));
                        Integer arg2 = o2.get(2).trim().length() == 0 ? 0 : Integer.parseInt(o2.get(2));
                        return arg1.compareTo(arg2);
                    }
                });
                activityAdapter.notifyDataSetChanged();

            }
        });


        sortByTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(activityList, new Comparator<ArrayList<String>>() {
                    @Override
                    public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                        Integer arg1 = o1.get(4).trim().length() == 0 ? 0 : Integer.parseInt(o1.get(4));
                        Integer arg2 = o2.get(4).trim().length() == 0 ? 0 : Integer.parseInt(o2.get(4));
                        return arg2.compareTo(arg1);
                    }
                });
                activityAdapter.notifyDataSetChanged();

            }
        });

        addActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityViewHistory.this, ActivityAddActivity.class);
                startActivity(intent);
            }
        });


        viewMonthlyStatusButton.setOnClickListener(new View.OnClickListener() {

//            int currentMonthTotal = 0;  int prevMonthTotal = 0;
//
//            @Override
            public void onClick(View view) {

                Intent intent=new Intent(ActivityViewHistory.this,ActivityStats.class);
                startActivity(intent);
           }
        });

    }//end OnCreate()


    /**
     * Displays the activities
     */
    private class ActivityAdapter extends ArrayAdapter<String> {
        LayoutInflater inflater;

        public ActivityAdapter(Context context) {

            super(context, 0);
            inflater = ActivityViewHistory.this.getLayoutInflater();
        }

        public int getCount() {
            return activityList.size();
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View result = inflater.inflate(R.layout.activity_row, null);

            TextView activityType = result.findViewById(R.id.activityType);
            TextView activityDate = result.findViewById(R.id.activityDate);
            TextView activityDuration = result.findViewById(R.id.activityDuration);

            ArrayList<String> row = activityList.get(position);

            String activity = row.get(3) + " - ";
            String date = row.get(2) + " - ";
            String duration = row.get(4).isEmpty() ? "0 min" : row.get(4) + " min ";

            activityType.setText(activity);
            activityDate.setText(date);
            activityDuration.setText(duration);

            return result;

        }
    }

    /**
     * Deletes an activity from database by id
     * @param id activity id
     */
    public void deleteActivity(int id) {

        activityDB.delete(ActivityDatabaseHelper.TABLE_NAME, ActivityDatabaseHelper.COL_ID + " = " + id, null);

        //update the list
        cursor = activityDB.query(ActivityDatabaseHelper.TABLE_NAME, new String[]{ActivityDatabaseHelper.COL_ID, ActivityDatabaseHelper.COL_COMMENT, ActivityDatabaseHelper.COL_DATE, ActivityDatabaseHelper.COL_EXERCISETYPE, ActivityDatabaseHelper.COL_PROGRESS}, null, null, null, null, null);
        activityList.clear();
        if (cursor.moveToFirst()) {
            for (int row = 0; row < cursor.getCount(); row++) {
                activityList.add(new ArrayList<String>());
                for (int col = 0; col < cursor.getColumnCount(); col++) {
                    activityList.get(row).add(cursor.getString(col));
                }
                cursor.moveToNext();
            }
        }
    }

    /**
     * Deletes an activity from database by id
     * if the app is on tablet or landscape mode
     * @param id actvity id
     */
    public void deleteTabletActivity(int id) {
        deleteActivity(id);
        activityAdapter.notifyDataSetChanged();
        getFragmentManager().beginTransaction().remove(activityFragment).commit();
    }

    /**
     *  Deletes Activity when ActivityFragment exits by giving the requestCode, the resultCode, and id.
     *
     * @param requestCode The integer request code to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult()
     * @param data an intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE) {   //Come back from Cell Phone delete
            if (resultCode == RES_DEL_CODE) {
                int a = Integer.parseInt(data.getStringExtra("id"));
                deleteActivity(a);
                activityAdapter.notifyDataSetChanged();
            }
            else if(resultCode==RES_DEL_CODE_TABLET){
                int a = Integer.parseInt(data.getStringExtra("id"));
                deleteTabletActivity(a);
            }

        }
    }

}
