package com.example.rwmol.cst2335_final_project;

/**
 * A data holding class that stores the total Calories, Fats, and Carbohydrates for a specific date.
 * <p>Used by the TotalDayValuesQuery AsyncTask from NutritionTrackerActivity to store the total Calories, Fats, and Carbohydrates
 * for each distinct date that has entries in the Nutrition Database. The values are recalculated anytime a food item is added, edited, or deleted.</p>
 *
 * @author Corey MacLeod
 * @version 3.0
 */

class DailySummary {

    private String date;
    private int totalCal;
    private int totalFat;
    private int totalCarb;

    /**
     * Sets the local date String reference to the distinct date argument.
     * @param date the distinct date that had its total values calculated.
     */
    void setDate(String date){this.date = date;}

    /**
     * @return the local date String reference.
     */
    String getDate(){return date;}

    /**
     * Sets the local totalCal value to the cal argument.
     * @param cal total Calories value calculated during the TotalDayValuesQuery AsyncTask.
     */
    void setCal(int cal){this.totalCal = cal;}

    /**
     * @return the local totalCal value.
     */
    int getCal(){return totalCal;}

    /**
     * Sets the local totalFat value to the fat argument.
     * @param fat total Fat value calculated during the TotalDayValuesQuery AsyncTask.
     */
    void setFat(int fat){this.totalFat = fat;}

    /**
     * @return the local totalFat value.
     */
    int getFat(){return totalFat;}

    /**
     * Sets the local totalCarb value to the cal argument.
     * @param carb total Carbohydrate value calculated during the TotalDayValuesQuery AsyncTask.
     */
    void setCarb(int carb){this.totalCarb = carb;}

    /**
     * @return the local totalCarb value.
     */
    int getCarb(){return totalCarb;}


}
