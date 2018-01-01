package com.example.rwmol.cst2335_final_project;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * This Activity is designed to add a Fragment that is
 * loaded in to a FrameLayout
 *
 * @author Sahar Saeednooran
 * @version 1.0
 *
 */
public class ActivityDetails extends Activity {
    private ArrayList<String> selectedActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        selectedActivity=getIntent().getStringArrayListExtra("selectedActivity");
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("selectedActivity", selectedActivity);

        ActivityFragment activityFragment = new ActivityFragment();
        activityFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.fragment_container, activityFragment).commit();
    }


}
