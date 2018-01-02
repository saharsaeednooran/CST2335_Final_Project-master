package com.example.rwmol.cst2335_final_project;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * An Activity designed to keep track of a user's food intake by allowing them to add every food item they eat during the day.
 * The user can add the name, calorie count, fat/carbohydrate content and the item will be stored in a database linked to the activity.
 * The Activity will then calculate the user's average daily calorie intake and inform them of the total calories they consumed the lst day they added food.
 * A progress bar shows how much of their daily calorie goal they have consumed, the last day they entered a food item.
 * The majority of the screen is used to display one of two listViews.
 * One displays a list of every food item added; showing the item's name, calorie count, and date dded.
 * Selecting one of these food items expands the item to also show it's fat and carbohydrate content as well as two buttons
 * that allow the user to either edit or completely delete the food item.
 * The other listView displays a list of daily summaries that show the total calories, fat, and carbohydrates consumed by the user each day.
 *
 * @author Corey MacLeod
 * @version 3.0
 */
public class NutritionTrackerActivity extends AppCompatActivity {

    final String FOOD_LIST_FRAG = "foodList";
    final String SUMMARY_LIST_FRAG = "summaryList";
    final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    TextView lastDayAddedCalorieTotal;
    TextView averageCalorieTotal;
    Button addFood;
    Button showFoodList;
    Button showSummaryList;
    ProgressBar dailyTotalProgress;
    SQLiteDatabase db;
    Cursor cursor;
    NutritionDatabaseHelper foodDBHelper;
    ArrayList <NutritionInfo> foodStorage;
    ArrayList <DailySummary> dailySummaries;
    AllFoodListAdapter allFoodListAdapter;
    DailySummaryListAdapter dailySummaryListAdapter;
    AllFoodListFragment allFoodListFragment;
    DailySummaryListFragment summaryListFragment;
    int dailyCalorieGoal;


    /**
     * Inflates the layout for the NutritionTrackerActivity and sets the the local View references to the ones in the Context View.
     * Initializes a new NutritionDatabaseHelper and requests an SQLiteDatabase reference.
     * Initializes new ArrayLists of both NutritionInfo and DailySummary as well as their respective ArrayListAdapters.
     *
     * Parses every row of the "food" table from "Nutrition.db" into the NutritionInfo ArrayList
     * and runs a new TotalDailyValuesQuery to calculate average values and generate an ArrayList of DailySummaries.
     *
     * Initializes new instances of AllFoodListFragment and DailySummaryListFragment, linking their respective ArrayListAdapter.
     * Attaches the AllFoodListFragment to the "nutrition_lists_fragment_holder" View when the NutritionTrackerActivity is originally created.
     * Creates onClickListeners for the showFoodList and showSummaryList Buttons that will replace the Fragment held in "nutrition_lists_fragment_holder"
     * with their respective Fragments.
     *
     * Creates an onClickListener for the addFood Button that initializes and displays a custom Dialog that allows the user
     * to add a new food Item; setting the name, calories, fat, and carbohydrate values.
     *
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_tracker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lastDayAddedCalorieTotal = findViewById(R.id.nutrition_last_day_added_calories_value);
        averageCalorieTotal = findViewById(R.id.average_daily_calories_value);
        addFood = findViewById(R.id.add_food_button);
        dailyTotalProgress = findViewById(R.id.daily_food_values_progress);
        dailyTotalProgress.setVisibility(View.VISIBLE);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        dailyCalorieGoal = sharedPref.getInt(getString(R.string.daily_calorie_goal), 2100);

        foodDBHelper = new NutritionDatabaseHelper(this);
        db = foodDBHelper.getWritableDatabase();
        cursor = getTotalCursor();

        foodStorage = new ArrayList <>();
        allFoodListAdapter = new AllFoodListAdapter( this, foodStorage);

        dailySummaries = new ArrayList<>();
        dailySummaryListAdapter = new DailySummaryListAdapter(this, dailySummaries);

        showFoodList = findViewById(R.id.show_full_food_list);
        showSummaryList = findViewById(R.id.show_summary_list);

        cursor.moveToFirst();
        while(!cursor.isAfterLast() ) {
            NutritionInfo nInfoCursor = new NutritionInfo();
            nInfoCursor.setName(cursor.getString(cursor.getColumnIndex(NutritionDatabaseHelper.KEY_NAME)));
            nInfoCursor.setCalories(cursor.getString(cursor.getColumnIndex(NutritionDatabaseHelper.KEY_CALORIES)));
            nInfoCursor.setFat(cursor.getString(cursor.getColumnIndex(NutritionDatabaseHelper.KEY_FAT)));
            nInfoCursor.setCarb(cursor.getString(cursor.getColumnIndex(NutritionDatabaseHelper.KEY_CARBS)));
            nInfoCursor.setDate(cursor.getString(cursor.getColumnIndex(NutritionDatabaseHelper.KEY_DATE)));
            foodStorage.add(nInfoCursor);
            cursor.moveToNext();
        }
        new TotalDayValuesQuery().execute();

        allFoodListFragment = new AllFoodListFragment().newInstance(allFoodListAdapter);
        summaryListFragment = new DailySummaryListFragment().newInstance(dailySummaryListAdapter);

        showFoodList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AllFoodListFragment allFoodFragment = (AllFoodListFragment) getSupportFragmentManager().findFragmentByTag(FOOD_LIST_FRAG);
                if (allFoodFragment == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nutrition_lists_fragment_holder, allFoodListFragment, FOOD_LIST_FRAG)
                            .commit();
                }
            }
        });

        showSummaryList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DailySummaryListFragment summaryFragment = (DailySummaryListFragment) getSupportFragmentManager().findFragmentByTag(SUMMARY_LIST_FRAG);
                if (summaryFragment == null) {
                    getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nutrition_lists_fragment_holder, summaryListFragment, SUMMARY_LIST_FRAG)
                            .commit();
                }
            }
        });

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.nutrition_lists_fragment_holder, allFoodListFragment, FOOD_LIST_FRAG)
                    .commit();
        }

        addFood.setOnClickListener(new View.OnClickListener() {
            Dialog dialog;
            TextView addName;
            TextView addCal;
            TextView addFat;
            TextView addCarb;
            public void onClick(View view) {
                dialog = new Dialog(NutritionTrackerActivity.this);
                dialog.setContentView(R.layout.nutrition_add_food_dialog_layout);
                dialog.setTitle(getResources().getString(R.string.nutrition_add_food));
                addName = dialog.findViewById(R.id.add_food_name);
                addCal = dialog.findViewById(R.id.add_food_calories);
                addFat = dialog.findViewById(R.id.add_food_fat);
                addCarb = dialog.findViewById(R.id.add_food_carb);
                Button saveFood = dialog.findViewById(R.id.add_food_save);
                Button cancelSave = dialog.findViewById(R.id.add_food_cancel);
                saveFood.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        String dateToStr = getCurrentTimeString();
                        String inputCals = checkAddedValue(addCal.getText().toString());
                        String inputFat = checkAddedValue(addFat.getText().toString());
                        String inputCarbs = checkAddedValue(addCarb.getText().toString());
                        NutritionInfo foodStore = new NutritionInfo();
                        foodStore.setName(addName.getText().toString());
                        foodStore.setCalories(inputCals);
                        foodStore.setFat(inputFat);
                        foodStore.setCarb(inputCarbs);
                        foodStore.setDate(dateToStr);
                        foodStorage.add(foodStore);
                        ContentValues cValues = new ContentValues();
                        cValues.put(NutritionDatabaseHelper.KEY_NAME,addName.getText().toString());
                        cValues.put(NutritionDatabaseHelper.KEY_CALORIES,inputCals);
                        cValues.put(NutritionDatabaseHelper.KEY_FAT,inputFat);
                        cValues.put(NutritionDatabaseHelper.KEY_CARBS,inputCarbs);
                        cValues.put(NutritionDatabaseHelper.KEY_DATE,dateToStr);
                        db.insert(NutritionDatabaseHelper.TABLE_NAME, null, cValues);
                        String foodAdded = (getResources().getString(R.string.nutrition_food_added_snackbar) + " " + addName.getText().toString() + " " + getResources().getString(R.string.nutrition_to_database));
                        Snackbar.make(findViewById(R.id.content), foodAdded, Snackbar.LENGTH_LONG)
                                .show();
                        dialog.dismiss();
                        updateNutritionActivity();

                    }
                });

                cancelSave.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    /**
     * Returns a String with the value of "0" if the inputValue argument is an empty String.
     * Otherwise it will return the original inputValue String unchanged.
     * <p>Used when adding a new food item to replace any number value that isn't input with a 0.
     * This ensures that only number values are input into the Nutrition Database as well as the NutritionInfo ArrayList</p>
     *
     * @param inputValue the String input by the user for Calories, Fats, and Carbohydrates when adding a new food.
     * @return a String with the value of "0" if the inputValue argument is an empty String. Otherwise it returns the initial value of the inputValue argument.
     */
    public String checkAddedValue(String inputValue){
        if (Objects.equals(inputValue, "")){
            return String.valueOf(0);
        }
        return inputValue;
    }

    /**
     * Deletes a food item from the Nutrition Database using the "_id" key provided as the id argument.
     * Removes the item from the NutritionInfo ArrayList using the position argument as the ArrayList index value.
     * Executes an AsyncTask to refresh the calculated daily averages values one the item has been removed.
     * Refreshes the local Cursor object to reflect the change as well as notify the allFoodListAdapter that the data has been changed.
     * <p>Used by the AllFoodListAdapter when a user selects and deletes a food item to completely remove the item from program
     * and update the GUI to reflect the change in data.</p>
     *
     * @param id the KEY_ID for the selected item as stored in the Nutrition Database. Retrieved by AllFoodListAdapter's getItemID method.
     * @param position the index value of the NutritionInfo object that represents the food item deleted by the user.
     */
    public void deleteFood(long id, int position) {
        db.execSQL("delete from " + NutritionDatabaseHelper.TABLE_NAME + " where " + NutritionDatabaseHelper.KEY_ID + "='" + id + "'");
        foodStorage.remove(position);
        updateNutritionActivity();
    }

    /**
     * Updates a food item from the Nutrition Database using the "_id" key provided as the id argument.
     * Sets the "name", "calories", "fat", and "carb" fields of the corresponding food item to the values provided by the name, cals, fat, and carbs arguments.
     * Changes the values in the row of the Nutrition Database with an "_id" matching the id argument to the ones provided
     * as the name, cals, fat, and carbs arguments.
     *
     * @param id the "_id" key for the corresponding row in the Nutrition Database.
     * @param position the position of the selected AllFoodList View corresponding to the NutritionInfo ArrayList
     * @param name the new name of the food entered by the user to replace the "name" value in the corresponding row in the Nutrition Database.
     * @param cals the new calorie value entered by the user to replace the "calories" value in the corresponding row in the Nutrition Database.
     * @param fat the new fat value entered by the user to replace the "fat" value in the corresponding row in the Nutrition Database.
     * @param carbs the new carbohydrate value entered by the user to replace the "carb" value in the corresponding row in the Nutrition Database.
     */
    public void updateFood(long id, int position, String name, String cals, String fat, String carbs){
        db.execSQL("update " + NutritionDatabaseHelper.TABLE_NAME
                + " set " + NutritionDatabaseHelper.KEY_NAME + " = \"" + name
                + "\", " + NutritionDatabaseHelper.KEY_CALORIES + " = \"" + cals
                + "\", " + NutritionDatabaseHelper.KEY_FAT + " = \"" + fat
                + "\", " + NutritionDatabaseHelper.KEY_CARBS + " = \"" + carbs
                +"\" where " + NutritionDatabaseHelper.KEY_ID + " = " + id);

        NutritionInfo updateInfo = foodStorage.get(position);
        updateInfo.setName(name);
        updateInfo.setCalories(cals);
        updateInfo.setFat(fat);
        updateInfo.setCarb(carbs);
        updateNutritionActivity();
    }

    /**
     * Updates the Cursor and ArrayListAdapters used by the NutritionTrackerActivity
     * as well as run the TotalDayValuesQuery AsyncTask which calculates average values as well as generate
     * an ArrayList of SummaryInfo that holds the total values for each date with data entered.
     *
     * <p>Used by the deleteFood and updateFood methods as well as the addFood onClickListener to update the GUI
     * elements of the Activity in the event of a change in data.</p>
     */
    private void updateNutritionActivity(){
        cursor = getTotalCursor();
        new TotalDayValuesQuery().execute();
        allFoodListAdapter.notifyDataSetChanged();
        dailySummaryListAdapter.notifyDataSetChanged();
    }

    /**
     * Overriden method of FragmentActivity's onDestroy method that also closes the Cursor used by NutritionTrackerActivity's onCreate method.
     * {@inheritDoc}
     */
    @Override
    public void onDestroy(){
        cursor.close();
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){
        switch(mi.getItemId()) {
            case R.id.activity_tracker_activity:
                setResult(MainActivity.SWITCH_TO_FITNESS);
                finish();
                break;
            case R.id.automobile_activity:
                setResult(MainActivity.SWITCH_TO_AUTOMOBILE);
                finish();
                break;
            case R.id.nutrition_tracker_activity:
               // setResult(MainActivity.SWITCH_TO_NUTRITION);
               // finish();
                break;
        }
        return true;
    }

    /**
     * Used by the TotalDayValuesQuery AsyncTask to calculate the total calorie, fat, and carbohydrate values for the date matching the chosenDate argument.
     * @param chosenDate a distinct date that needs their total calorie, fat, and carbohydrate values summed.
     * @return a Cursor which captures every row in the Nutrition Database with "inputTime" value that has the same date as the chosenDate argument.
     */
    private Cursor getDayCursor (String chosenDate){
        String whereDate = (NutritionDatabaseHelper.KEY_DATE + " LIKE ?");
        String dateIsLike = (chosenDate + "%");
        return db.query(false, NutritionDatabaseHelper.TABLE_NAME, new String[] {NutritionDatabaseHelper.KEY_ID, NutritionDatabaseHelper.KEY_NAME, NutritionDatabaseHelper.KEY_CALORIES, NutritionDatabaseHelper.KEY_FAT, NutritionDatabaseHelper.KEY_CARBS, NutritionDatabaseHelper.KEY_DATE }, whereDate, new String[] {dateIsLike}, null,null, null,null);
    }

    /**
     * Used by multiple methods that use all the data stored in the "food" table of the Nutrition Database.
     * @return a Cursor which captures every row in the "food" table of the Nutrition Database.
     */
    public Cursor getTotalCursor (){
        return db.query(false, NutritionDatabaseHelper.TABLE_NAME, new String[] {NutritionDatabaseHelper.KEY_ID, NutritionDatabaseHelper.KEY_NAME, NutritionDatabaseHelper.KEY_CALORIES, NutritionDatabaseHelper.KEY_FAT, NutritionDatabaseHelper.KEY_CARBS, NutritionDatabaseHelper.KEY_DATE }, null, null, null,null, null,null);
    }

    /**
     * Used by the addFood onClickListener to set the "input_time" of the NutritionDatabase to the exact time the item is added.
     * @return a String value corresponding to the current timestamp using a SimpleDateFormat of "yyyy-MM-dd hh:mm:ss a".
     */
    private String getCurrentTimeString(){
        Calendar cal = Calendar.getInstance();
        return timeFormat.format(cal.getTime());
    }

    /**
     * Used by TotalDayValuesQuery to convert the "time_input" values of the Nutrition Database to a Calender object to easily compare dates.
     * Logs an message in the event that the timeString argument is unable to be parsed into a Calender Object.
     * @param timeString a String that represents a Calender object using a SimpleDateFormat of "yyyy-MM-dd hh:mm:ss a".
     * @return a Calender object that is equivalent to the timeString argument using a SimpleDateFormat of "yyyy-MM-dd hh:mm:ss a".
     */
    public Calendar getTimeValue(String timeString){
        Calendar timeValue = Calendar.getInstance();
        try {
            timeValue.setTime(timeFormat.parse(timeString));
        }catch (ParseException e){
            Log.i("getTimeValue", "Unable to parse the entered date.");
        }
        return timeValue;
    }

    /**
     * Used by TotalDayValuesQuery to convert the date value a SummaryList object into a Calender object to easily compare dates.
     * Logs an message in the event that the dateString argument is unable to be parsed into a Calender Object.
     * @param dateString a String that represents a Calender object using a SimpleDateFormat of "yyyy-MM-dd".
     * @return Calender object that is equivalent to the dateString argument using a SimpleDateFormat of "yyyy-MM-dd".
     */
    public Calendar getDateValue(String dateString){
        Calendar timeValue = Calendar.getInstance();
        try {
            timeValue.setTime(dateFormat.parse(dateString));
        }catch (ParseException e){
            Log.i("getDateValue", "Unable to parse the entered date.");
        }
        return timeValue;
    }

    /**
     * Used by TotalDayValuesQuery to convert a distinct date Calendar object into a String that only shows the year, month, and day.
     * @param cal a Calendar object that needs to be converting into a String.
     * @return a String value representing the cal argument using a SimpleDateFormat of "yyyy-mm-dd".
     */
    public String getDateString(Calendar cal){
        return dateFormat.format(cal.getTime());
    }

    /**
     * Compares the ArrayList of distinct dates to the ArrayList of DailySummary's.
     * If there is a DailySummary object with a date that is no in the individualDates ArrayList,
     * the DailySummary object is removed from the ArrayList.
     *
     * <p>Used by TotalDayValuesQuery to determine if the date of a DailySummary object no longer has entries in the Nutrition Database.
     * If no entries exist for that given date, the DailySummary is removed from the ArrayList in order to correctly represent
     * the values show in the GUI.</p>
     * @param individualDates the ArrayList of Calender Objects that represent distinct dates for every row in the Nutrition Database.
     */
    public void checkIfSummaryDayDeleted(ArrayList<Calendar> individualDates){
        for (int checkSummaryDates = 0; checkSummaryDates<dailySummaries.size(); checkSummaryDates++){
            boolean containsDate = false;
            Calendar getSummaryDate = getDateValue(dailySummaries.get(checkSummaryDates).getDate());
            for (int checkIndividualDates = 0; checkIndividualDates<individualDates.size(); checkIndividualDates++) {
                if(individualDates.get(checkIndividualDates).get(Calendar.YEAR)==getSummaryDate.get(Calendar.YEAR) &&
                        individualDates.get(checkIndividualDates).get(Calendar.DAY_OF_YEAR)==getSummaryDate.get(Calendar.DAY_OF_YEAR)){
                    containsDate=true;
                    break;
                }
            }
            if (!containsDate){
                dailySummaries.remove(checkSummaryDates);
                break;
            }
        }
    }

    /**
     * Compares the "time_input" value from every row of the "food" table in "Nutrition.db" to capture each distinct date value and store it into an ArrayList
     * <p>Used by TotalDayValuesQuery to determine which distinct dates have entries in the "food" table of "Nutrition.db".
     * Allows TotalDayValuesQuery to calculate the total calorie, fat, and carbohydrate values for each distinct date.</p>
     * @return an ArrayList of Calendar objects that represent each distinct date for every row in the "food" table of "Nutrition.db"
     */
    public ArrayList<Calendar> getIndividualDates(){
        Cursor everyEntry = getTotalCursor();
        ArrayList<Calendar> individualDates = new ArrayList<>();
        everyEntry.moveToFirst();
        while(!everyEntry.isAfterLast()){
            Calendar individualDate = getTimeValue(everyEntry.getString(everyEntry.getColumnIndex(NutritionDatabaseHelper.KEY_DATE)));
            boolean containsDate = false;
            for(int x=0;x<individualDates.size();x++){
                containsDate = (individualDate.get(Calendar.YEAR)==individualDates.get(x).get(Calendar.YEAR) &&
                        individualDate.get(Calendar.DAY_OF_YEAR)==individualDates.get(x).get(Calendar.DAY_OF_YEAR));
            }
            if (!containsDate){
                individualDates.add(individualDate);
            }
            everyEntry.moveToNext();
        }
        return individualDates;
    }

    /**
     * A custom AsyncTask that runs important calculations for NutritionTrackerActivity in the background.
     * <p>Used by NutritionTrackerActivity to calculate the total calories for the last day that data has been entered,
     * as well as calculate the average daily calories consumed. Also populates and update the ArrayList of DailySummary
     * objects displayed in the DailySummaryListFragment in order to display the mst update to date summary
     * of all the data stored in the "food" table of "Nutrition.db"</p>
     */
    private class TotalDayValuesQuery extends AsyncTask<String, Integer, String> {

        private  String lastDateEntered = "";
        private int totalCaloriesEnteredLast = 0;
        private int totalDailyAverageCalories = 0;

        /**
         * Calls the getIndividualDates method to generate an ArrayList of distinct dates for all the data stored in the "food" table of "Nutrition.db"
         * Stores the value of the last date entered and calculates the total calories for the last day entered.
         * Calls the checkIfSummaryDayDeleted to determine if any days from the DailySummary ArrayList is missing on the ArrayList of distinct date.
         * Deletes any DailySummary's that are not on the distinct date ArrayList.
         * Calculates the total Calories, Fats, and Carbohydrates for every distinct date and either updates existing
         * DailySummary's or adds a new one to the DailySummary ArrayList.
         * Calculates the average daily calories and passes it to the local variable.
         * Passes the calculated total calories entered for the last date with date entries to the local variable.
         * Passes the String value of the last date with data entries to the local variable.
         *
         * {@inheritDoc}
         * @param args
         * @return
         */
        @Override
        protected String doInBackground(String... args) {

            ArrayList<Calendar> everyDate = getIndividualDates();
            checkIfSummaryDayDeleted(everyDate);
            int totalAverageCalories = 0;
            for (int dateCount = 0; dateCount < everyDate.size(); dateCount++) {
                int totalDailyCalories = 0;
                String selectedDate = getDateString(everyDate.get(dateCount));
                Cursor dailyCursor = getDayCursor(selectedDate);
                dailyCursor.moveToFirst();
                int totalDailyFat = 0;
                int totalDailyCarb = 0;
                while (!dailyCursor.isAfterLast()) {
                    totalDailyFat += Integer.valueOf(dailyCursor.getString(dailyCursor.getColumnIndex(NutritionDatabaseHelper.KEY_FAT)));
                    totalDailyCarb += Integer.valueOf(dailyCursor.getString(dailyCursor.getColumnIndex(NutritionDatabaseHelper.KEY_CARBS)));
                    int foodCalorieValues = Integer.valueOf(dailyCursor.getString(dailyCursor.getColumnIndex(NutritionDatabaseHelper.KEY_CALORIES)));
                    totalDailyCalories += foodCalorieValues;
                    dailyCursor.moveToNext();
                }

                boolean summaryExists = false;
                for (int dateCheck = 0; dateCheck < dailySummaries.size(); dateCheck++) {
                    if (Objects.equals(dailySummaries.get(dateCheck).getDate(), selectedDate)) {
                        summaryExists = true;
                        dailySummaries.get(dateCheck).setCal(totalDailyCalories);
                        dailySummaries.get(dateCheck).setFat(totalDailyFat);
                        dailySummaries.get(dateCheck).setCarb(totalDailyCarb);
                        break;
                    }
                }

                if (!summaryExists) {
                    DailySummary dailySummary = new DailySummary();
                    dailySummary.setDate(selectedDate);
                    dailySummary.setCal(totalDailyCalories);
                    dailySummary.setFat(totalDailyFat);
                    dailySummary.setCarb(totalDailyCarb);
                    dailySummaries.add(dailySummary);
                }
                dailyCursor.close();
                totalAverageCalories += totalDailyCalories;
            }
            if (everyDate.size() <1){
                lastDateEntered = getResources().getString(R.string.nutrition_none_added);
                totalDailyAverageCalories = 0;

            }else {
                lastDateEntered = getDateString(everyDate.get(everyDate.size() - 1));
                totalDailyAverageCalories = (totalAverageCalories / everyDate.size());
            }

            if (dailySummaries.size()<1){
                totalCaloriesEnteredLast = 0;
            }else {
                totalCaloriesEnteredLast = dailySummaries.get(dailySummaries.size() - 1).getCal();
            }
            return "Calculated Averages";
        }

        /**
         * Executed once the doInBackground method to ensure all calculations are done.
         * Generates the text to be displayed in the lasDayAddedCalorieTotal and averageCalorieTotal TextViews at the top of NutritionTrackerActivity's layout.
         * Calculates the percentage of user's daily calorie goal met the last day data was entered.
         * Sets the dailyTotalProgress bar's progress value to the newly calculated percentage.
         * {@inheritDoc}
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            String currentDate = dateFormat.format(Calendar.getInstance().getTime());
            String lastDayAddedTotalValues;
            if(Objects.equals(lastDateEntered, currentDate)){
                lastDateEntered = getResources().getString(R.string.nutrition_today_string);
            }

            String dailyAverageTotalValues = (" " + Integer.toString(totalDailyAverageCalories) + " " + getResources().getString(R.string.nutrition_cal_per_day));

            if(Objects.equals(lastDateEntered, getResources().getString(R.string.nutrition_none_added))){
              lastDayAddedTotalValues = ": " + lastDateEntered;
            }else {
                lastDayAddedTotalValues = (" " + lastDateEntered + ":  " + Integer.toString(totalCaloriesEnteredLast)
                        + " " + getResources().getString(R.string.nutriton_of_string) + " " + dailyCalorieGoal + " " + getResources().getString(R.string.nutrition_cal_unit));
            }
            lastDayAddedCalorieTotal.setText(lastDayAddedTotalValues);
            int dailyGoalProgress = ((totalCaloriesEnteredLast * 100) / dailyCalorieGoal);
            dailyTotalProgress.setProgress(dailyGoalProgress);
            averageCalorieTotal.setText(dailyAverageTotalValues);
        }
    }
}
