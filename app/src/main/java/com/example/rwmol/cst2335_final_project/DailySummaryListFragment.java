package com.example.rwmol.cst2335_final_project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A custom Fragment that holds and displays a ListView linked to an DailySummaryListAdapter.
 * <p>Used by the NutritionTrackerActivity to display a visual representation of the DailySummary ArrayList generated in the TotalDayValuesQuery AsyncTask.
 * Displays the total amounts of Calories, Fats, and Carbohydrates for each date that data has been input.</p>
 *
 * @author Corey MacLeod
 * @version 3.0
 */
public class DailySummaryListFragment extends Fragment {

    DailySummaryListAdapter dailySummaryListAdapter;
    ListView summaryList;

    /**
     * Creates a new Instance of AllFoodListFragment that accepts a DailySummaryListAdapter reference as arguments.
     *
     * <p>Used by NutritionTrackerActivity to generate a new fragment that uses an existing DailySummaryListAdapter</p>
     * @param dailySummaryAdapter the DailySummaryListAdapter that is linked to a DailySummary ArrayList.
     * @return a new DailyFoodListFragment that uses a DailySummaryListAdapter that's already been initialized.
     */
    public static DailySummaryListFragment newInstance(ArrayAdapter<DailySummary> dailySummaryAdapter) {
        DailySummaryListFragment fragment = new DailySummaryListFragment();
        fragment.dailySummaryListAdapter = (DailySummaryListAdapter)dailySummaryAdapter;
        return fragment;
    }

    public DailySummaryListFragment() {}

    /**
     * Overriden version of Fragment's onCreate method that also retains the instance of the DailySummaryListFragment,
     * allowing it to persist after an Orientation change.
     * {@inheritDoc}
     * @param bundle
     */
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    /**
     * Inflates the "nutrition_daily_summary_list_fragment" layout for DailySummaryListFragment's View. Layout consists of a single ListView
     *
     * {@inheritDoc}
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return an inflated View using the "nutrition_daily_summary_list_fragment" layout.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nutrition_daily_summary_list_fragment, container, false);
    }

    /**
     * Links the local ListView reference to the one inflated by onCreateView , sets it's Adapter to the local DailySummaryListAdapter, and adds an onItemClickListener.
     * The onItemClickListener notifies the DailySummaryListAdapter of the View selected as well as a data change.
     * {@inheritDoc}
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        summaryList= view.findViewById(R.id.daily_summary_list);
        summaryList.setAdapter(dailySummaryListAdapter);
    }
}
