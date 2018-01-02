package com.example.rwmol.cst2335_final_project;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 *Custom ArrayAdapter that takes in an ArrayList of NutritionInfo. Generates Views for the ListView
 * in AllFoodListFragment using the data stored in the NutritionInfo ArrayList.
 *
 * @author Corey MacLeod
 * @version 3.0
 */
public class AllFoodListAdapter extends ArrayAdapter<NutritionInfo> {

    private int selectPosition = -1;//Used by the AllFoodListFragment ListView to designated which view is selected by the user. Initialized to -1 so that none of the views are selected.

    /*Views used by onClickListeners and need to be defined outside of said onClickListeners*/
    private Dialog dialog;
    private EditText editName;
    private EditText editCal;
    private EditText editFat;
    private EditText editCarbs;
    private TextView deleteName;

    /*Reference variables set by the constructor to allow the Adapter to interact with the ArrayList as well as allow the getItemID method to use the most updated Cursor*/
    private LayoutInflater inflater;
    private NutritionTrackerActivity ctx;
    private ArrayList<NutritionInfo> list;

    /**
     * Creates a new Adapter and sets the local reference variables to the ones provided as arguments.
     * @param ctx is the Activity that is using this adapter and is required by the ArrayAdapter superclass.
     * @param list is the ArrayList containing the NutritionInfo objects that hold the values displayed in the custom views.
     */
    AllFoodListAdapter(Context ctx, ArrayList <NutritionInfo> list) {
        super(ctx, 0);
        inflater=LayoutInflater.from(ctx);
        this.list = list;
        this.ctx=(NutritionTrackerActivity) ctx;
    }

    /**
     * Returns the size of the local NutritionInfo ArrayList.
     * @return the size of the local NutritionInfo ArrayList.
     */
    public int getCount(){
        return list.size();
    }

    /**
     *Returns the NutritionInfo stored in the list ArrayList using the position argument as the array index.
     * @param position the position of the View in which the method is called.
     * @return the NutritionInfo reference stored in the list ArrayList whose index matches the position argument.
     */
    public NutritionInfo getItem(int position){
        return list.get(position);
    }

    /**
     * Used by the ListView in AllFoodListFragment to inflate and populate custom layouts for each NutritionInfo object stored in the linked ArrayList.
     *
     * If the convertView hasn't already been initialized and has a pre-existing tag, a layout is inflated for the convertView argument.
     * A new FoodListHolder is then initialized to hold the references to the Views from the inflated layout and is set as the tag value for the convertView argument.
     * The values for the convertView's TextViews will then be set to the matching values stored in the NutritionInfo ArrayList.
     *
     * If the convertView matches the position of the selected View from the food list ListView, a different layout is inflated.
     * This layout displays the name, calorie value, and date entered stored in the NutritionInfo ArrayList but also contains
     * the fat and carbohydrate values as well as a two Buttons for Editing or Deleting the selected item.
     * Both Edit and Delete Buttons have onClickListeners initialized that will inflate custom dialogs when selected.
     *
     * If the "Edit" button is selected, a dialog is shown that asks the user to input new values. If the "Save" button is selected,
     * the new values are saved to both the NutritionInfo ArrayList as well as the Nutrition Database. Otherwise the dialog is dismissed.
     *
     * If the "Delete" button is selected, a dialog is shown that asks the user if they wish to delete the item. If the "Delete" button is selected,
     * the item is removed from both the NutritionInfo ArrayList as well as the Nutrition Database. Otherwise the dialog is dismissed.
     *
     *
     * @param position the location of the convertView argument within it's ListView.
     * @param convertView the reference to the View that needs to be inflated or updated.
     * @param parent the ListView that is holding the multiple convertView Views.
     * @return the custom View inflated or updated depending on if it already exists and whether or not there was a change in data.
     */
    public @NonNull
    View getView(final int position, View convertView, @NonNull ViewGroup parent){

        FoodListHolder foodListHolder;

        if(convertView==null || convertView.getTag() == null)
        {
            convertView = inflater.inflate(R.layout.nutrition_info_view_layout, parent, false);
            foodListHolder = new FoodListHolder();
            foodListHolder.name = convertView.findViewById(R.id.foodName);
            foodListHolder.cal = convertView.findViewById(R.id.calorieCount);
            foodListHolder.date = convertView.findViewById(R.id.dateSaved);
            convertView.setTag(foodListHolder);
        }else{
            foodListHolder = (FoodListHolder) convertView.getTag();
        }
        foodListHolder.name.setText(list.get(position).getName());
        foodListHolder.cal.setText((list.get(position).getCalories()+ " " + getContext().getResources().getString(R.string.nutrition_calories)));
        String dateAdded =(getContext().getResources().getString(R.string.nutrition_added) + " " + (list.get(position).getDate()));
        foodListHolder.date.setText(dateAdded);

        if (this.selectPosition==position){
            View selectedView = inflater.inflate(R.layout.nutrition_info_view_selected_layout, parent, false);

            TextView name = selectedView.findViewById(R.id.detail_list_food_name);
            TextView cal = selectedView.findViewById(R.id.detail_list_food_calorie);
            TextView fat = selectedView.findViewById(R.id.detail_list_food_fat);
            TextView carb = selectedView.findViewById(R.id.detail_list_food_carb);
            TextView date = selectedView.findViewById(R.id.detail_list_food_date);
            Button edit = selectedView.findViewById(R.id.edit_food);
            Button delete = selectedView.findViewById(R.id.delete_food);

            name.setText(list.get(position).getName());
            cal.setText((list.get(position).getCalories()+ " " + getContext().getResources().getString(R.string.nutrition_calories)));
            fat.setText((list.get(position).getFat() + getContext().getResources().getString(R.string.gram_unit_short) + " " + getContext().getResources().getString(R.string.nutrition_of_fat)));
            carb.setText((list.get(position).getCarb() + getContext().getResources().getString(R.string.gram_unit_short) + " " + getContext().getResources().getString(R.string.nutrition_of_carbs)));
            date.setText(dateAdded);

            edit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    dialog = new Dialog(ctx);
                    dialog.setContentView(R.layout.nutrition_edit_food_dialog_layout);
                    dialog.setTitle(getContext().getResources().getString(R.string.nutrition_edit_food));
                    editName = dialog.findViewById(R.id.edit_food_name_value);
                    editCal = dialog.findViewById(R.id.edit_food_cals_value);
                    editFat = dialog.findViewById(R.id.edit_food_fat_value);
                    editCarbs = dialog.findViewById(R.id.edit_food_carb_value);
                    editName.setText(getItem(position).getName());
                    editCal.setText(getItem(position).getCalories());
                    editFat.setText(getItem(position).getFat());
                    editCarbs.setText(getItem(position).getCarb());
                    Button saveFood = dialog.findViewById(R.id.edit_food_save);
                    Button cancelSave = dialog.findViewById(R.id.edit_food_cancel);
                    saveFood.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            String newName = editName.getText().toString();
                            String newCal = editCal.getText().toString();
                            String newFat = editFat.getText().toString();
                            String newCarbs = editCarbs.getText().toString();
                            String text = (getContext().getResources().getString(R.string.nutrition_saved_changes_to) + " " + newName);
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(ctx, text, duration);
                            toast.show();
                            ctx.updateFood(getItemId(position),position, newName, newCal, newFat,newCarbs);
                            dialog.dismiss();
                        }
                    });

                    cancelSave.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            dialog.dismiss();
                            selectPosition = -1;
                            notifyDataSetChanged();
                        }
                    });
                    dialog.show();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    dialog = new Dialog(ctx);
                    dialog.setContentView(R.layout.nutrition_delete_food_dialog_layout);
                    dialog.setTitle(getContext().getResources().getString(R.string.nutrition_delete_food_title));
                    deleteName=dialog.findViewById(R.id.delete_food_name);
                    Button deleteFoodButton = dialog.findViewById(R.id.delete_prompt_delete_button);
                    Button cancelDeleteButton = dialog.findViewById(R.id.delete_prompt_cancel_button);
                    String deleteQuestion = (getItem(position).getName() + ctx.getResources().getString(R.string.question_mark));
                    deleteName.setText(deleteQuestion);
                    deleteFoodButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            String fName = list.get(position).getName();
                            String text = (getContext().getResources().getString(R.string.nutrition_deleting) +" " + fName + " " + getContext().getResources().getString(R.string.nutrition_from_database));
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(ctx, text, duration);
                            toast.show();
                            ctx.deleteFood(getItemId(position),position);
                            dialog.dismiss();
                            selectPosition = -1;
                            notifyDataSetChanged();
                        }
                        });

                    cancelDeleteButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            dialog.dismiss();
                            selectPosition = -1;
                            notifyDataSetChanged();
                        }
                    });
                    dialog.show();
                }
            });
            return selectedView;
        }
        return convertView;
    }

    /**
     *Used by NutritionTrackerActivity's deleteFood and updateFood methods to determine which row in the Nutrition Database to interact with.
     * @param position the position of the View last selected by the user.
     * @return the id value stored in the Nutrition Database correlating view selected by the user.
     */
    public long getItemId(int position){
        Cursor cursor = ctx.getTotalCursor();
        if(cursor.moveToFirst()) {
            cursor.moveToPosition(position);
            long itemId =cursor.getLong(cursor.getColumnIndex(NutritionDatabaseHelper.KEY_ID));
            cursor.close();
            return itemId;
        }
        else{
            cursor.close();
            return -1;
        }
    }

    /**
     * Method used by AllFoodListFragment's ListView to set which View is selected by the user so that getView can inflate a distinct layout.
     * @param position is the position of the view last selected by the user.
     */
    void selectFood(int position){
        this.selectPosition=position;
    }

    /**
     * ViewHolder class designed to hold the reference values for the views stored in the custom FoodList views displayed in AllFoodListFragment's ListView
     */
    static class FoodListHolder
    {
        TextView name, cal, date;
    }

}
