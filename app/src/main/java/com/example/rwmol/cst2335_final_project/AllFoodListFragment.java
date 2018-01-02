package com.example.rwmol.cst2335_final_project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A custom Fragment that holds and displays a ListView linked to an AllFoodListAdapter.
 * <p>Used by the NutritionTrackerActivity to display a visual representation of the Nutrition Database.</p>
 *
 * @author Corey MacLeod
 * @version 3.0
 */
public class AllFoodListFragment extends Fragment {

    AllFoodListAdapter allFoodListAdapter;
    ListView foodList;

    /**
     * Creates a new Instance of AllFoodListFragment that accepts a that uses an existing AlLFoodListAdapter
     *
     * <p>Used by NutritionTrackerActivity to generate a new fragment that can use NutritionTrackertActivity's methods directly.</p>
     * @param foodAdapter the AllFoodListAdapter that is linked to a NutritionInfo ArrayList.
     * @return a new AllFoodListFragment that can call methods from the NutritionTrackerActivity that initialized it.
     */
    public static AllFoodListFragment newInstance(ArrayAdapter<NutritionInfo> foodAdapter) {
        AllFoodListFragment fragment = new AllFoodListFragment();
        fragment.allFoodListAdapter = (AllFoodListAdapter)foodAdapter;
        return fragment;
    }

    public AllFoodListFragment() {}


    /**
     * Overriden version of Fragment's onCreate method that also retains the instance of the AllFoodListFragment,
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
     * Inflates the "nutrition_all_food_list_fragment" layout for AllFoodListFragment's View. Layout consists of a single ListView
     *
     * {@inheritDoc}
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nutrition_all_food_list_fragment, container, false);
    }

    /**
     * Links the local ListView reference to the one inflated by onCreateView , sets it's Adapter to the local AllFoodListAdapter, and adds an onItemClickListener.
     * The onItemClickListener notifies the AllFoodListAdapter of the View selected as well as a data change.
     * {@inheritDoc}
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        foodList = view.findViewById(R.id.total_food_list_view);
        foodList.setAdapter(allFoodListAdapter);
        foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                allFoodListAdapter.selectFood(position);
                allFoodListAdapter.notifyDataSetChanged();
            }
        });
    }
}
