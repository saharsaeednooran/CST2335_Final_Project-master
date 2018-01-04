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
                AutomobileMonthlyGasFragment automobileMonthlyGasFragment = (AutomobileMonthlyGasFragment)getSupportFragmentManager().findFragmentByTag(MONTHLY_GAS);
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

    public String checkUserInput(String inputValue){
        if(Objects.equals(inputValue, "")){
            return String.valueOf(0);
        }
        return inputValue;
    }

    public void deletePurchase(long id, int pos){
        db.execSQL("DELETE FROM " + AutoDatabaseHelper.TABLE_NAME + " where " + AutoDatabaseHelper.KEY_ID + "='" + id + "'");
        //purchaseStorage.get(pos).getDate();
        purchaseStorage.remove(pos);
        updateAutomotiveActivity();
    }

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

    private void updateAutomotiveActivity(){
        cursor = getTotalCursor();
        new AverageCalculations().execute();
        purchaseListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy(){
        cursor.close();
        super.onDestroy();
    }

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
        return cursor.getString(cursor.getColumnIndex("SUM(LITRES)"));
    }

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
        return cursor.getString(cursor.getColumnIndex("AVG(COST)"));
    }

    public String calculateMonthlySum(int month){
        String testMonth = String.format("%02d", month);
        String query = "SELECT SUM(LITRES) FROM " + AutoDatabaseHelper.TABLE_NAME + " WHERE DATE LIKE ?";
        String monthCue = "%-" + testMonth + "-%";
        Cursor cursor = db.rawQuery(query, new String[]{monthCue});
        cursor.moveToPosition(0);
        return cursor.getString(cursor.getColumnIndex("SUM(LITRES)"));
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
                builder.setTitle("Fuel Tracker");
                builder.setMessage(R.string.automobile_help);
                // Add the buttons
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return true;
    }

    private class AverageCalculations extends AsyncTask<Void, Integer, String>{

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

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            calcAverage.setProgress(values[0]);
            calcAverage.setVisibility(View.VISIBLE);
        }

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

            sumPrice.setText(getString(R.string.automobileSumPrice, s));
            calcAverage.setVisibility(View.INVISIBLE);
            Snackbar.make(findViewById(R.id.autoActivityLayout), "Averages have been updated!", Snackbar.LENGTH_LONG).show();
        }
    }
}
