package com.example.rwmol.cst2335_final_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


/**
 * This Activity is designed as Activity tracker home page
 * The user can add a new activity or view their previous
 * exercises.
 *
 * @author Sahar Saeednooran
 * @version 1.0
 *
 */
public class ActivityTrackerActivity extends AppCompatActivity {
    private Button addActivityBtn,viewHistoryBtn ;

    @Override
    /**
     * Creates Add Activity Tracker and inflates to activity_tracker view
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activtyTrackerTitle);


        addActivityBtn = (Button) findViewById(R.id.addNewActivityButton);
        addActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityTrackerActivity.this,ActivityAddActivity.class);
                startActivity(intent);
            }
        });

        viewHistoryBtn=(Button) findViewById(R.id.viewHistoryButton);
        viewHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityTrackerActivity.this,ActivityViewHistory.class);
                startActivity(intent);
            }
        });
    }//end of OnCreate

    /**
     * Creates the toolbar by inflating it from toolbar_menu
     * @param m Menu
     * @return true if options menu is specified
     */
    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.activity_tracker_activity:
                //setResult(MainActivity.SWITCH_TO_FITNESS);
                //finish();
                break;
            case R.id.automobile_activity:
                setResult(MainActivity.SWITCH_TO_AUTOMOBILE);
                finish();
                break;
            case R.id.nutrition_tracker_activity:
                setResult(MainActivity.SWITCH_TO_NUTRITION);
                finish();
                break;

            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Activity Tracker");
                builder.setMessage(R.string.help_tracker);
                // Add the buttons
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return true;
    }



}
