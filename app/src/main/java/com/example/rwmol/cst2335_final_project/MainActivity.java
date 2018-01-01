package com.example.rwmol.cst2335_final_project;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    final static int START_ACTIVITY = 10;
    final static int SWITCH_TO_FITNESS = 11;
    final static int SWITCH_TO_AUTOMOBILE = 12;
    final static int SWITCH_TO_NUTRITION = 13;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button activityTrackerButton = (Button) findViewById(R.id.activityTrackerButton);
        activityTrackerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityTrackerActivity.class);
                startActivityForResult(intent, START_ACTIVITY);
            }
        });

        Button nutritionTrackerButton = (Button) findViewById(R.id.nutritionTrackerButton);
        nutritionTrackerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NutritionTrackerActivity.class);
                startActivityForResult(intent, START_ACTIVITY);
            }
        });

//        Button houseThermostatButton = (Button) findViewById(R.id.houseThermostatButton);
//        houseThermostatButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, HouseThermostatActivity.class);
//                startActivityForResult(intent, START_ACTIVITY);
//            }
//        });

        Button automobileButton = (Button) findViewById(R.id.automobileButton);
        automobileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, AutomobileActivity.class);
                //startActivityForResult(intent, START_ACTIVITY);
            }
        });
    }

    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){
        switch(mi.getItemId()) {
            case R.id.activity_tracker_activity:
                Intent activityIntent = new Intent(MainActivity.this, ActivityTrackerActivity.class);
                startActivityForResult(activityIntent, START_ACTIVITY);
                break;
            case R.id.automobile_activity:
                //Intent automobileIntent = new Intent(MainActivity.this, AutomobileActivity.class);
                //startActivityForResult(automobileIntent, START_ACTIVITY);
                break;
            case R.id.nutrition_tracker_activity:
                Intent nutritionIntent = new Intent(MainActivity.this, NutritionTrackerActivity.class);
                startActivityForResult(nutritionIntent, START_ACTIVITY);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode==START_ACTIVITY){
            switch(resultCode) {
                case SWITCH_TO_FITNESS:
                    Intent activityIntent = new Intent(MainActivity.this, ActivityTrackerActivity.class);
                    startActivityForResult(activityIntent, START_ACTIVITY);
                    break;

                case SWITCH_TO_AUTOMOBILE:
                   // Intent automobileIntent = new Intent(MainActivity.this, AutomobileActivity.class);
                    //startActivityForResult(automobileIntent, START_ACTIVITY);
                    break;

                case SWITCH_TO_NUTRITION:
                    Intent nutritionIntent = new Intent(MainActivity.this, NutritionTrackerActivity.class);
                    startActivityForResult(nutritionIntent, START_ACTIVITY);
                    break;
            }
        }
    }
}