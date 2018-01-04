package com.example.rwmol.cst2335_final_project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * An Activity designed to keep track of a user\'s fuel purchases.
 * The main screen shows the calculated amounts for average price and fuel in last 30 days.
 * The user has the option of adding a purchase, viewing a monthly summary of fuel purchase, or seeing a list of purchases.
 * When adding a purchase, the program automatically sets the date to today, though this can be overriden by the user.  This is done to give the user an example of date formatt.  The user inputs the price they paid, the amount of litres purchased and the number of kilometers.  Hitting save adds the purchase to the database and cancel removes the dialoge box.
 * Monthly gas summary tallies the amount of gas purchased by the user each month.  If there are no purchases, the system displays NA
 * Purchase history displays a listview of purchase dates.  When one is selected, detailed information is presented to the user.  The user has the option to edit, delete or cancel.
 * When ever the values in the database are changed, a new average is calculated.
 *
 * @author Ryan Molitor
 * @version 1.2
 */
public class AutomobileActivity extends AppCompatActivity{

    protected static final String ACTIVITY_NAME = "AutomobileActivity";
    final String AUTO_PURCHASE_LIST = "purchaseList";
    final String MONTHLY_GAS = "gasList";
    Button addPurchase, purchaseHistory, monthlySummary;
    AutoDatabaseHelper autoDBHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ArrayList<PurchaseInfo> purchaseStorage;
    AllAutoPurchaseListAdapter purchaseListAdapter;
    AllAutoPurchaseFragment purchaseFragment;
    AutomobileMonthlyGasFragment monthFragment;
    TextView avgPrice;
    TextView sumPrice;
    private ProgressBar calcAverage;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    AutomobileMonthlyGasFragment automobileMonthlyGasFragment;


    /**
     * Inflates the layout for the Automobile and sets the the local View references to the ones in the Context View.
     * Initializes a new AutomobileDatabaseHelper and requests an SQLiteDatabase reference.
     * Initializes new ArrayLists purchaseStorage well as its respective ArrayListAdapters.
     *
     * Creates an onClickListener for the Buttons that initializes and displays a custom Dialog that allows the user
     * to add , edit, or delete.
     *
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.automobileTitle);

        addPurchase = findViewById(R.id.addPurchaseButton);
        purchaseHistory = findViewById(R.id.purchaseHistoryButton);
        monthlySummary = findViewById(R.id.monthlyGasButton);

        calcAverage = findViewById(R.id.calculateAverageProgress);
        calcAverage.setVisibility(View.VISIBLE);
        calcAverage.setMax(100);

        autoDBHelper = new AutoDatabaseHelper(this);
        db = autoDBHelper.getWritableDatabase();
        cursor = getTotalCursor();

        purchaseStorage = new ArrayList<>();
        purchaseListAdapter = new AllAutoPurchaseListAdapter(this, purchaseStorage);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            PurchaseInfo purchaseInfoCursor = new PurchaseInfo();
            purchaseInfoCursor.setCost(cursor.getString(cursor.getColumnIndex(AutoDatabaseHelper.COST)));
            purchaseInfoCursor.setDate(cursor.getString(cursor.getColumnIndex(AutoDatabaseHelper.DATE)));
            purchaseInfoCursor.setKilo(cursor.getString(cursor.getColumnIndex(AutoDatabaseHelper.KILOMETERS)));
            purchaseInfoCursor.setLitre(cursor.getString(cursor.getColumnIndex(AutoDatabaseHelper.LITRES)));
            purchaseStorage.add(purchaseInfoCursor);
            cursor.moveToNext();
        }

        new AverageCalculations().execute();

        purchaseFragment = new AllAutoPurchaseFragment().newInstance(purchaseListAdapter);
        monthFragment = new AutomobileMonthlyGasFragment().newInstance(AutomobileActivity.this);

        purchaseHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AllAutoPurchaseFragment allAutoPurchaseFragment = (AllAutoPurchaseFragment)getSupportFragmentManager().findFragmentByTag(AUTO_PURCHASE_LIST);
                if(allAutoPurchaseFragment == null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.auto_fragment_container, purchaseFragment, AUTO_PURCHASE_LIST).commit();
                }
            }
        });

        monthlySummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AutomobileMonthlyGasFragment automobileMonthlyGasFragment = (AutomobileMonthlyGasFragment)getSupportFragmentManager().findFragmentByTag(MONTHLY_GAS);
                automobileMonthlyGasFragment = (AutomobileMonthlyGasFragment)getSupportFragmentManager().findFragmentByTag(MONTHLY_GAS);
                if(automobileMonthlyGasFragment == null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.auto_fragment_container, monthFragment, MONTHLY_GAS).commit();
                }
            }
        });

        addPurchase.setOnClickListener(new View.OnClickListener(){
            Dialog dialog;
            TextView addCost, addKilo, addLitre, addDate;
            public void onClick(View view){
                dialog = new Dialog(AutomobileActivity.this);
                dialog.setContentView(R.layout.automobile_add_dialog_layout);
                dialog.setTitle("Add Purchase");
                addCost = dialog.findViewById(R.id.addPurchaseCost);
                addKilo = dialog.findViewById(R.id.addPurchaseKilometers);
                addLitre = dialog.findViewById(R.id.addPurchaseLitres);
                addDate = dialog.findViewById(R.id.addPurchaseDate);
                String date = sdf.format(new Date());
                addDate.setText(date);

                Button saveButton = dialog.findViewById(R.id.save_add);
                saveButton.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        String newDate = addDate.getText().toString();
                        double numLitres = Double.valueOf(checkUserInput(addLitre.getText().toString()));
                        double numCost = Double.valueOf(checkUserInput(addCost.getText().toString()));
                        double numKilo = Double.valueOf(checkUserInput(addKilo.getText().toString()));
                        PurchaseInfo purchaseInfo = new PurchaseInfo();
                        purchaseInfo.setLitre(String.valueOf(numLitres));
                        purchaseInfo.setKilo(String.valueOf(numKilo));
                        purchaseInfo.setDate(newDate);
                        purchaseInfo.setCost(String.valueOf(numCost));
                        purchaseStorage.add(purchaseInfo);
                        ContentValues cValues = new ContentValues();
                        cValues.put(AutoDatabaseHelper.LITRES, addLitre.getText().toString());
                        cValues.put(AutoDatabaseHelper.COST, addCost.getText().toString());
                        cValues.put(AutoDatabaseHelper.DATE, newDate);
                        cValues.put(AutoDatabaseHelper.KILOMETERS, addKilo.getText().toString());
                        db.insert(AutoDatabaseHelper.TABLE_NAME, null, cValues);

                        dialog.dismiss();
                        updateAutomotiveActivity();

                    }
                });

                Button cancelButton = dialog.findViewById(R.id.cancel_add);
                cancelButton.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
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
     *
     * @param inputValue the String input by the user
     * @return a String with the value of "0" if the inputValue argument is an empty String. Otherwise it returns the initial value of the inputValue argument.
     */
    public String checkUserInput(String inputValue){
        if(Objects.equals(inputValue, "")){
            return String.valueOf(0);
        }
        return inputValue;
    }

    /**
     * Deletes am item from the Database using the "_id" key provided as the id argument.
     * Executes an AsyncTask to refresh the calculated daily averages values one the item has been removed.
     * Notify the allFoodListAdapter that the data has been changed.
     *
     * @param id the KEY_ID for the selected item as stored in the Nutrition Database. Retrieved by AllFoodListAdapter's getItemID method.
     * @param pos the index value of the NutritionInfo object that represents the food item deleted by the user.
     */
    public void deletePurchase(long id, int pos){
        db.execSQL("DELETE FROM " + AutoDatabaseHelper.TABLE_NAME + " where " + AutoDatabaseHelper.KEY_ID + "='" + id + "'");
        //purchaseStorage.get(pos).getDate();
        purchaseStorage.remove(pos);
        updateAutomotiveActivity();
    }

    /**
     * Updates a purchase item from the Database using the "_id" key provided as the id argument.
     *
     * @param id the "_id" key for the corresponding row in the Nutrition Database.
     * @param pos the position of the selected item
     * @param date the new name date of the purchase
     * @param cost the new cost of the purchase
     * @param kilo the new number of kilometers of the purchase
     * @param litre the new number of litres of the purchase
     */
    public void updatePurchase(long id, int pos, String date, double cost, double kilo, double litre){
        db.execSQL("UPDATE " + AutoDatabaseHelper.TABLE_NAME
                + " set " + AutoDatabaseHelper.DATE + " = \"" + date
                + "\", " + AutoDatabaseHelper.LITRES + " = \"" + litre
                + "\", " + AutoDatabaseHelper.KILOMETERS + " = \"" + kilo
                + "\", " + AutoDatabaseHelper.COST + " = \"" + cost
                + "\" WHERE " + AutoDatabaseHelper.KEY_ID + " = " + id);

        PurchaseInfo updateInfo = purchaseStorage.get(pos);
        updateInfo.setLitre(String.valueOf(litre));
        updateInfo.setCost(String.valueOf(cost));
        updateInfo.setKilo(String.valueOf(kilo));
        updateInfo.setDate(date);
        updateAutomotiveActivity();
    }

    /**
     * Updates the Cursor
     * as well as run the AverageCalculations AsyncTask which calculates average values
     */
    private void updateAutomotiveActivity(){
        cursor = getTotalCursor();
        new AverageCalculations().execute();
        purchaseListAdapter.notifyDataSetChanged();
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

    /**
     * Used by multiple methods that use all the data stored in the Database.
     * @return a Cursor which captures every row in the Database.
     */
    public Cursor getTotalCursor(){
        return db.query(false,
                AutoDatabaseHelper.TABLE_NAME,
                new String[]{ AutoDatabaseHelper.KEY_ID,
                        AutoDatabaseHelper.COST,
                        AutoDatabaseHelper.KILOMETERS,
                        AutoDatabaseHelper.LITRES,
                        AutoDatabaseHelper.DATE},
                null,
                null,
                null,
                null,
                null,
                null);
    }

    /**
     * Calculates the sum of the litres of fuel purchases in the last 30 days
     * @return a string that holds the value of the Database query.
     */
    public String calculateThirtyDaySum(){
        Date today = new Date();
        String stringToday = sdf.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -30);
        String stringPast = sdf.format(cal.getTime());
        String query = "SELECT SUM(LITRES) FROM " + AutoDatabaseHelper.TABLE_NAME + " WHERE DATE >= ? and DATE  <= ?";
        Cursor cursor = db.rawQuery(query, new String[]{stringPast, stringToday});
        cursor.moveToPosition(0);
        String result = cursor.getString(cursor.getColumnIndex("SUM(LITRES)"));
        cursor.close();
        return result;
    }

    /**
     * Calculates the average cost for fuel over the last 30 days
     * @return a string that holds the value of the Database query.
     */
    public String calculateThirtyDayAvg(){
        Date today = new Date();
        String stringToday = sdf.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -30);
        String stringPast = sdf.format(cal.getTime());
        String query = "SELECT AVG(COST) FROM " + AutoDatabaseHelper.TABLE_NAME + " WHERE DATE >= ? and DATE  <= ?";
        Cursor cursor = db.rawQuery(query, new String[]{stringPast, stringToday});
        //Cursor cursor = db.rawQuery(query, new String[]{stringPast, today.toString()}); if break, use above
        cursor.moveToPosition(0);
        String result = cursor.getString(cursor.getColumnIndex("AVG(COST)"));
        cursor.close();
        return result;
    }

    /**
     * Calculates the sum of the fuel in a indicated month
     * @param month the int value for the month for the query. Eg, 1 would be january
     * @return a string that holds the value of the Database query.
     */
    public String calculateMonthlySum(int month){
        String testMonth = String.format("%02d", month);
        String query = "SELECT SUM(LITRES) FROM " + AutoDatabaseHelper.TABLE_NAME + " WHERE DATE LIKE ?";
        String monthCue = "%-" + testMonth + "-%";
        Cursor cursor = db.rawQuery(query, new String[]{monthCue});
        cursor.moveToPosition(0);
        String result = cursor.getString(cursor.getColumnIndex("SUM(LITRES)"));
        cursor.close();
        return result;
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
                //setResult(MainActivity.SWITCH_TO_AUTOMOBILE);
                //finish();
                break;
            case R.id.nutrition_tracker_activity:
                setResult(MainActivity.SWITCH_TO_NUTRITION);
                finish();
                break;
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.automobileTitle));
                builder.setMessage(R.string.automobile_help);
                // Add the buttons
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return true;
    }

    /**
     * A custom AsyncTask that runs statistical queries in the background.
     */
    private class AverageCalculations extends AsyncTask<Void, Integer, String>{

        /**
         * Creates a progress bar to demonstrate progress in updating statistical values
         *
         * {@inheritDoc}
         * @param args
         * @return
         */
        @Override
        protected String doInBackground(Void... args){

           int counter;
            for (counter=1; counter < 10; counter++ ) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(counter);
            }
            return "done";
        }

        /**
         * Updates the progress bar
         *
         * {@inheritDoc}
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            calcAverage.setProgress(values[0]);
            calcAverage.setVisibility(View.VISIBLE);
        }

        /**
         * Calculates values for all statistical queries.
         * Updates values
         * Refreshes fragment data, if required
         *
         * {@inheritDoc}
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            String s;
            NumberFormat formatter = new DecimalFormat("#.##");
            avgPrice = findViewById(R.id.averageGasPrice);
            if(calculateThirtyDayAvg() != null){
                s = formatter.format(Double.valueOf(calculateThirtyDayAvg()));
                avgPrice.setText(getString(R.string.automobileAvgPrice, s));
            }else{
                avgPrice.setText(getString(R.string.automobileZeroPrice));
            }

            sumPrice = findViewById(R.id.totalLitres);
            if(calculateThirtyDaySum() != null){
                s = formatter.format(Double.valueOf(calculateThirtyDaySum()));
            }else{
                s = getString(R.string.automobileZeroPrice);
            }
            automobileMonthlyGasFragment = (AutomobileMonthlyGasFragment)getSupportFragmentManager().findFragmentByTag(MONTHLY_GAS);
            if(automobileMonthlyGasFragment != null){
                AutomobileMonthlyGasFragment monthFragment2 = new AutomobileMonthlyGasFragment().newInstance(AutomobileActivity.this);
                getSupportFragmentManager().beginTransaction().replace(R.id.auto_fragment_container, monthFragment2, MONTHLY_GAS).commit();
            }

            sumPrice.setText(getString(R.string.automobileSumPrice, s));
            calcAverage.setVisibility(View.INVISIBLE);
            Snackbar.make(findViewById(R.id.autoActivityLayout), "Averages have been updated!", Snackbar.LENGTH_LONG).show();
        }
    }
}
