package com.example.rwmol.cst2335_final_project;

/**
 * Stores the Nutritional Information input by the user so that AllFoodListAdapter can handle every field as a single object.
 * <p>An ArrayList of NutritionInfo is populate when the NutritionTrackerActivity is created.
 * A NutritionInfo object is created for every row that exists in the "food" table of the Nutrition Database.</p>
 *
 * @author Corey MacLeod
 * @version 3.0
 */

class NutritionInfo {

    private String name;
    private String calories;
    private String fat;
    private String carb;
    private String date;


    /**
     * @return the local name String reference.
     */
    String getName(){return name;}

    /**
     * Sets the local name String reference to the name argument.
     * @param name the "name" value of the corresponding row in the Nutrition Database.
     */
    void setName( String name ){this.name = name;}

    /**
     * @return the local calories String reference.
     */
    String getCalories(){return calories;}

    /**
     * Sets the local calories String reference to the calories argument.
     * @param calories the "calories" value of the corresponding row in the Nutrition Database.
     */
    void setCalories( String calories ){this.calories = calories;}

    /**
     * @return the local fat String reference.
     */
    String getFat(){return fat;}

    /**
     * Sets the local fat String reference to the fat argument.
     * @param fat the "fat" value of the corresponding row in the Nutrition Database.
     */
    void setFat( String fat ){this.fat = fat;}

    /**
     * @return the local carb String reference.
     */
    String getCarb(){return carb;}

    /**
     * Sets the local carb String reference to the carb argument.
     * @param carb the "carb" value of the corresponding row in the Nutrition Database.
     */
    void setCarb( String carb ){this.carb = carb;}

    /**
     * @return the local date String reference.
     */
    String getDate(){return date;}

    /**
     * Sets the local date String reference to the fate argument.
     * @param date the "input_time" value of the corresponding row in the Nutrition Database.
     */
    void setDate( String date ){this.date = date;}

}
