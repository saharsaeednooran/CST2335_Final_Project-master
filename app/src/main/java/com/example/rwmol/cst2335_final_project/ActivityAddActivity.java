package com.example.rwmol.cst2335_final_project;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.Snackbar;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This Activity is designed to let users add their activity
 * The user can add the type of exercise (Run, Walk, Skate, Bike, Swim,...),
 * The Duration of exercise, and their comments about it.
 * They should click add button so that the exercise be added to the database.
 * They can add as many exercises as they want and finally they can click
 * Back to Home button to go back to home page.
 *
 * @author Sahar Saeednooran
 * @version 1.0
 *
 */
public class ActivityAddActivity extends AppCompatActivity {

    ImageButton runningImgBtn, walkingImgBtn, skatingImgBtn, bikingImgBtn, swimmingImgBtn;
    SeekBar seekBar;
    TextView progressTextView;
    EditText commentEditText;
    Button addActivityBtn, cancelBtn;
    String exerciseType, comment, date;
    int progress, id;
    SQLiteDatabase activityDB;
    boolean edit = false;


    /**
     * Creates Add Activity and inflates to activity_add view
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        runningImgBtn = (ImageButton) findViewById(R.id.runningImageButton);
        walkingImgBtn = (ImageButton) findViewById(R.id.walkingImageButton);
        swimmingImgBtn = (ImageButton) findViewById(R.id.swimmingImageButton);
        skatingImgBtn = (ImageButton) findViewById(R.id.skatingImageButton);
        bikingImgBtn = (ImageButton) findViewById(R.id.bikingImageButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        progressTextView = (TextView) findViewById(R.id.progressTextView);
        commentEditText = (EditText) findViewById(R.id.commentsEditText);
        addActivityBtn = (Button) findViewById(R.id.addActivityButton);
        cancelBtn=(Button)findViewById(R.id.cancelButton);

        //Select Running
        runningImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runningImgBtn.setSelected(!runningImgBtn.isSelected());
                if (runningImgBtn.isSelected()) {
                    bikingImgBtn.setSelected(false);
                    walkingImgBtn.setSelected(false);
                    swimmingImgBtn.setSelected(false);
                    skatingImgBtn.setSelected(false);

                    Toast.makeText(getApplicationContext(), "Running",
                            Toast.LENGTH_SHORT).show();
                }
                exerciseType = "Run";

            }
        });

        //Select Walking
        walkingImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walkingImgBtn.setSelected(!walkingImgBtn.isSelected());
                if (walkingImgBtn.isSelected()) {
                    bikingImgBtn.setSelected(false);
                    runningImgBtn.setSelected(false);
                    swimmingImgBtn.setSelected(false);
                    skatingImgBtn.setSelected(false);

                    Toast.makeText(getApplicationContext(), "Walking",
                            Toast.LENGTH_SHORT).show();
                }
                exerciseType = "Walk";
            }
        });

        //Select Swimming
        swimmingImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swimmingImgBtn.setSelected(!swimmingImgBtn.isSelected());
                if (swimmingImgBtn.isSelected()) {
                    bikingImgBtn.setSelected(false);
                    runningImgBtn.setSelected(false);
                    walkingImgBtn.setSelected(false);
                    skatingImgBtn.setSelected(false);

                    Toast.makeText(getApplicationContext(), "Swimming",
                            Toast.LENGTH_SHORT).show();
                }
                exerciseType = "Swim";
            }
        });

        //Select Swimming
        skatingImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skatingImgBtn.setSelected(!skatingImgBtn.isSelected());
                if (skatingImgBtn.isSelected()) {
                    bikingImgBtn.setSelected(false);
                    runningImgBtn.setSelected(false);
                    walkingImgBtn.setSelected(false);
                    swimmingImgBtn.setSelected(false);

                    Toast.makeText(getApplicationContext(), "Skating",
                            Toast.LENGTH_SHORT).show();
                }
                exerciseType = "Skate";
            }
        });


        //Select Biking
        bikingImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bikingImgBtn.setSelected(!bikingImgBtn.isSelected());
                if (bikingImgBtn.isSelected()) {
                    runningImgBtn.setSelected(false);
                    walkingImgBtn.setSelected(false);
                    swimmingImgBtn.setSelected(false);
                    skatingImgBtn.setSelected(false);

                    Toast.makeText(getApplicationContext(), "Biking",
                            Toast.LENGTH_SHORT).show();
                }
                exerciseType = "Bike";
            }
        });

        //Progress bar to set the Duration
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressTextView.setText(i + " min");
                progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        ActivityDatabaseHelper dbHelper = new ActivityDatabaseHelper(this);
        activityDB = dbHelper.getWritableDatabase();


        //User clicks Add Activity
        addActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                comment = commentEditText.getText().toString();
                date = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

                //check if exercise type is not selected or duration is 0 or comment is empty!!!
                if (exerciseType == null) {
                    Toast.makeText(getApplicationContext(), "Please select your exercise",
                            Toast.LENGTH_SHORT).show();
                } else if (progress == 0) {
                    Toast.makeText(getApplicationContext(), "Please add your exercise period",
                            Toast.LENGTH_SHORT).show();
                } else if (comment.equals("")) {

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ActivityAddActivity.this);
                    alertBuilder.setMessage("You haven't added any comments, Do you want to save?");
                    alertBuilder.setCancelable(true);
                    alertBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveActivity();
                        }
                    });

                    alertBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();

                } else {
                    saveActivity();
                }


            }
        });

        //User clicks back to Home button
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }// end of OnCreate()

    @Override
    /**
     * Start Add Activity again with the details of current activity
     * and process the modification to it.
     */
    protected void onStart()
    {
        super.onStart();
        edit = getIntent().getBooleanExtra("edit", false);

        if(edit) //back from Edit an exercise
        {
            id=Integer.parseInt(getIntent().getStringExtra("id"));
            exerciseType=getIntent().getStringExtra("activity");
            progress=Integer.parseInt(getIntent().getStringExtra("duration"));
            comment=getIntent().getStringExtra("comments");

            if (exerciseType.equals("Run")){
                runningImgBtn.setSelected(!runningImgBtn.isSelected());
                bikingImgBtn.setSelected(false);
                walkingImgBtn.setSelected(false);
                swimmingImgBtn.setSelected(false);
                skatingImgBtn.setSelected(false);

            }else if (exerciseType.equals("Walk")){
                walkingImgBtn.setSelected(!walkingImgBtn.isSelected());
                bikingImgBtn.setSelected(false);
                runningImgBtn.setSelected(false);
                swimmingImgBtn.setSelected(false);
                skatingImgBtn.setSelected(false);


            } else if (exerciseType.equals("Swim")){
                swimmingImgBtn.setSelected(!swimmingImgBtn.isSelected());
                bikingImgBtn.setSelected(false);
                runningImgBtn.setSelected(false);
                walkingImgBtn.setSelected(false);
                skatingImgBtn.setSelected(false);

            }else if (exerciseType.equals("Skate")){
                skatingImgBtn.setSelected(!skatingImgBtn.isSelected());
                bikingImgBtn.setSelected(false);
                runningImgBtn.setSelected(false);
                walkingImgBtn.setSelected(false);
                swimmingImgBtn.setSelected(false);

            }else if (exerciseType.equals("Bike")){
                bikingImgBtn.setSelected(!bikingImgBtn.isSelected());
                skatingImgBtn.setSelected(false);
                runningImgBtn.setSelected(false);
                walkingImgBtn.setSelected(false);
                swimmingImgBtn.setSelected(false);

            }
            progressTextView.setText(progress + " min");
            seekBar.setProgress(progress);
            commentEditText.setText(comment);
            addActivityBtn.setText("Update Activity");
        }
    }

    /**
     * Saves or modify an activity in the database
     */
     void saveActivity() {
        ContentValues cValues = new ContentValues();

        //inserts data into the database
        cValues.put(ActivityDatabaseHelper.COL_COMMENT, comment);
        cValues.put(ActivityDatabaseHelper.COL_DATE, date);
        cValues.put(ActivityDatabaseHelper.COL_EXERCISETYPE, exerciseType);
        cValues.put(ActivityDatabaseHelper.COL_PROGRESS, progress);

        if (edit){
            activityDB.updateWithOnConflict(ActivityDatabaseHelper.TABLE_NAME,cValues,ActivityDatabaseHelper.COL_ID + " = " + id, null, SQLiteDatabase.CONFLICT_IGNORE);
            Snackbar.make(findViewById(R.id.coordinatorLayout), "Changes Saved", Snackbar.LENGTH_LONG).show();
        } else {
            activityDB.insert(ActivityDatabaseHelper.TABLE_NAME, "null", cValues);
            Snackbar.make(findViewById(R.id.coordinatorLayout), "Activity Added", Snackbar.LENGTH_SHORT).show();
        }
    }

}
