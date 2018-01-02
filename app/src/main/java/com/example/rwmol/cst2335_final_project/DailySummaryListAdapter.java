package com.example.rwmol.cst2335_final_project;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Custom ArrayAdapter that takes in an ArrayList of DailySummary. Generates Views for the ListView
 * in AllSummaryListFragment using the data stored in the DailySummary ArrayList.
 *
 * @author Corey MacLeod
 * @version 3.0
 */
public class DailySummaryListAdapter extends ArrayAdapter<DailySummary> {

    private ArrayList<DailySummary> list;
    private LayoutInflater inflater;

    /**
     * Creates a new Adapter and sets the local reference variables to the ones provided as arguments.
     * @param ctx is the Activity that is using this adapter and is required by the ArrayAdapter superclass.
     * @param list is the ArrayList containing the DailySummary objects that hold the values displayed in the custom views.
     */
    DailySummaryListAdapter(Context ctx, ArrayList<DailySummary> list) {
        super(ctx, 0);
        inflater = LayoutInflater.from(ctx);
        this.list = list;
    }

    /**
     * Returns the size of the local NutritionInfo ArrayList.
     * @return the size of the local NutritionInfo ArrayList.
     */
    public int getCount() {
        return list.size();
    }

    /**
     *Returns the DailySummary stored in the list ArrayList using the position argument as the array index.
     * @param position the position of the View in which the method is called.
     * @return the DailySummary reference stored in the list ArrayList whose index matches the position argument.
     */
    public DailySummary getItem(int position) {
        return list.get(position);
    }

    /**
     * Used by the ListView in DailySummaryListFragment to inflate and populate custom layouts for each DailySummary object stored in the linked ArrayList.
     *
     * If the convertView hasn't already been initialized and has a pre-existing tag, a layout is inflated for the convertView argument.
     * A new SummaryListHolder is then initialized to hold the references to the Views from the inflated layout and is set as the tag value for the convertView argument.
     * The values for the convertView's TextViews will then be set to the matching values stored in the DailySummary ArrayList.
     *
     *
     * @param position the location of the convertView argument within it's ListView.
     * @param convertView the reference to the View that needs to be inflated or updated.
     * @param parent the ListView that is holding the multiple convertView Views.
     * @return the custom View inflated or updated depending on if it already exists and whether or not there was a change in data.
     */
    public @NonNull
    View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        SummaryListHolder summaryListHolder;
        if(convertView==null || convertView.getTag() == null)
        {
            convertView = inflater.inflate(R.layout.nutrition_daily_summary_layout, parent, false);
            summaryListHolder = new SummaryListHolder();
            summaryListHolder.date = convertView.findViewById(R.id.daily_summary_date_label);
            summaryListHolder.cal = convertView.findViewById(R.id.daily_summary_calories_value);
            summaryListHolder.fat = convertView.findViewById(R.id.daily_summary_fat_value);
            summaryListHolder.carb = convertView.findViewById(R.id.daily_summary_carbs_value);
            convertView.setTag(summaryListHolder);
        }else{
            summaryListHolder = (SummaryListHolder) convertView.getTag();
        }

        summaryListHolder.date.setText(list.get(position).getDate());
        String calValue = (String.valueOf(list.get(position).getCal())+" " + getContext().getResources().getString(R.string.nutrition_cal_unit));
        String fatValue = (String.valueOf(list.get(position).getFat()) + getContext().getResources().getString(R.string.gram_unit_short));
        String carbValue = (String.valueOf(list.get(position).getCarb()) + getContext().getResources().getString(R.string.gram_unit_short));
        summaryListHolder.cal.setText(calValue);
        summaryListHolder.fat.setText(fatValue);
        summaryListHolder.carb.setText(carbValue);

        return convertView;
    }

    /**
     * ViewHolder class designed to hold the reference values for the views stored in the custom DailySummary views displayed in DailySummaryListFragment's ListView
     */
    static class SummaryListHolder
    {
        TextView date, cal, fat, carb;
    }

}
