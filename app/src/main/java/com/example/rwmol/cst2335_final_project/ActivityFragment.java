package com.example.rwmol.cst2335_final_project;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * This Activity is designed to process the Activity Fragment
 * to edit or delete a selected activity
 *
 * @author Sahar Saeednooran
 * @version 1.0
 *
 */

public class ActivityFragment extends Fragment {

    String activity, duration, comments, id;
    TextView activityTextView, durationTextView, commentsTextView;
    Button editBtn, deleteBtn, cancelBtn;
    ActivityViewHistory activityViewHistory;
    View myView;
    public static Boolean edit = true;
    private ArrayList<String> selectedActivity;


    /**
     * Default constructor
     */
    public ActivityFragment(){

    }

    /**
     * Creates a new instance of activity fragment
     *
     * @param activityViewHistory an instance of ActivityViewHistory class
     * @return an istance of activity fragment
     */
    public static ActivityFragment newInstance(ActivityViewHistory activityViewHistory) {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
        fragment.activityViewHistory = activityViewHistory;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    /**
     * Inflates the "fragment_activity" layout to see the Activity details
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_activity, container, false);

        deleteBtn=(Button) myView.findViewById(R.id.deleteButton);
        editBtn=(Button) myView.findViewById(R.id.editButton);
        cancelBtn=(Button)myView.findViewById(R.id.cancelButton);
        activityTextView=(TextView)myView.findViewById(R.id.activityTextView);
        durationTextView=(TextView)myView.findViewById(R.id.durationTextView);
        commentsTextView=(TextView)myView.findViewById(R.id.commentsTextView);

        selectedActivity=this.getArguments().getStringArrayList("selectedActivity");
        activity = selectedActivity.get(3);
        duration = selectedActivity.get(4);
        comments= selectedActivity.get(1);
        id=selectedActivity.get(0);

        activityTextView.setText(activity);
        durationTextView.setText(duration + " min");
        commentsTextView.setText(comments);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityViewHistory==null) {//on phone
                    Intent intent = new Intent(getActivity(), ActivityFragment.class);
                    getActivity().setResult(ActivityViewHistory.RES_DEL_CODE, intent.putExtra("id",id));
                    getActivity().finish();
                }
                else//on tablet
                {
                    activityViewHistory.deleteTabletActivity(Integer.parseInt(id));
                }
            }
        });


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ActivityAddActivity.class);
                    intent.putExtra("edit", edit);
                    intent.putExtra("activity", activity);
                    intent.putExtra("duration", duration);
                    intent.putExtra("comments", comments);
                    intent.putExtra("id", id);
                    startActivity(intent);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return myView;
    }



}
