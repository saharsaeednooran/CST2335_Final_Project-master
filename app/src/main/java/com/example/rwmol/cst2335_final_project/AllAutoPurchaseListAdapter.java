package com.example.rwmol.cst2335_final_project;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
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
 *Custom ArrayAdapter that takes in an ArrayList of PurchaseInfo. Generates Views for the ListView
 * in AllAutoPurchaseFragment using the data stored in the ArrayList.
 *
 * @author Ryan Molitor
 * @version 1.2
 */
public class AllAutoPurchaseListAdapter extends ArrayAdapter<PurchaseInfo> {

    private int selectedPostion = -1;
    private static final String ACTIVITY_NAME = "AutoPurchaseListAdapter";

    private EditText purchaseDate;
    private EditText purchaseCost;
    private EditText purchaseKilometers;
    private EditText purchaseLitres;

    private LayoutInflater inflater;
    private AutomobileActivity ctx;
    private ArrayList<PurchaseInfo> list;
    private Dialog dialog;

    /**
     * Creates a new Adapter and sets the local reference variables to the ones provided as arguments.
     * @param ctx is the Activity that is using this adapter and is required by the ArrayAdapter superclass.
     * @param list is the ArrayList containing the objects that hold the values displayed in the custom views.
     */
    AllAutoPurchaseListAdapter(Context ctx, ArrayList<PurchaseInfo> list){
        super(ctx, 0);
        inflater = LayoutInflater.from(ctx);
        this.list = list;
        this.ctx = (AutomobileActivity) ctx;
    }

    /**
     * Returns the size of the local ArrayList.
     * @return the size of the local ArrayList.
     */
    public int getCount(){
        return list.size();
    }

    /**
     *Returns the PurchaseInfo stored in the list ArrayList using the position argument as the array index.
     * @param pos the position of the View in which the method is called.
     * @return the PurchaseInfo reference stored in the list ArrayList whose index matches the position argument.
     */
    public PurchaseInfo getItem(int pos){
        return list.get(pos);
    }

    /**
     * Used by the ListView in AllAutoPurchaseFragment to inflate and populate custom layouts for each Purchase object stored in the linked ArrayList.
     *
     * If the convertView hasn't already been initialized and has a pre-existing tag, a layout is inflated for the convertView argument.
     * A new holder is then initialized to hold the references to the Views from the inflated layout and is set as the tag value for the convertView argument.
     * The values for the convertView's TextViews will then be set to the matching values stored in the ArrayList.
     *
     * If the convertView matches the position of the selected View from the food list ListView, a different layout is inflated.
     * This layout displays the information in the ArrayList but also contains
     * the two Buttons for Editing or Deleting the selected item.
     * Both Edit and Delete Buttons have onClickListeners initialized that will inflate custom dialogs when selected.
     *
     * If the "Edit" button is selected, a dialog is shown that asks the user to input new values. If the "Save" button is selected,
     * the new values are saved to both the ArrayList as well as the Database. Otherwise the dialog is dismissed.
     *
     * If the "Delete" button is selected, a dialog is shown that asks the user if they wish to delete the item. If the "Delete" button is selected,
     * the item is removed from both the ArrayList as well as the Database. Otherwise the dialog is dismissed.
     *
     *
     * @param position the location of the convertView argument within it's ListView.
     * @param convertView the reference to the View that needs to be inflated or updated.
     * @param parent the ListView that is holding the multiple convertView Views.
     * @return the custom View inflated or updated depending on if it already exists and whether or not there was a change in data.
     */
    public View getView (final int position, View convertView, ViewGroup parent){

        PurchaseListHolder holder;

        if(convertView==null || convertView.getTag() == null){
            convertView = inflater.inflate(R.layout.automobile_info_view_layout, parent, false);
            holder = new PurchaseListHolder();
            holder.date = convertView.findViewById(R.id.dateInfo);
            convertView.setTag(holder);
        }else{
            holder = (PurchaseListHolder) convertView.getTag();
        }

        holder.date.setText(list.get(position).getDate());

        if(this.selectedPostion==position){
            View selectedView = inflater.inflate(R.layout.automobile_info_view_selected_layout, parent, false);

            TextView cost = selectedView.findViewById(R.id.purchaseCost);
            TextView date = selectedView.findViewById(R.id.purchaseDate);
            TextView kilo = selectedView.findViewById(R.id.purchaseKilometers);
            TextView litre = selectedView.findViewById(R.id.purchaseLitres);
            Button edit = selectedView.findViewById(R.id.edit_purchase);
            Button delete = selectedView.findViewById(R.id.delete_purchase);
            Button cancel = selectedView.findViewById(R.id.cancel_view);

            String autoCost = getContext().getResources().getString(R.string.automobileCost, list.get(position).getCost());
            cost.setText(autoCost);
            date.setText(list.get(position).getDate());
            String autoKilo = getContext().getResources().getString(R.string.automobileKilo, list.get(position).getKilo());
            kilo.setText(autoKilo);
            String autoLitre = getContext().getResources().getString(R.string.automobileLitre, list.get(position).getLitre());
            litre.setText(autoLitre);

            edit.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){

                    dialog = new Dialog(ctx);
                    dialog.setContentView(R.layout.automobile_edit_dialog_layout);
                    dialog.setTitle(getContext().getResources().getString(R.string.automobile_edit_dialog_title));
                    purchaseCost = dialog.findViewById(R.id.editPurchaseCost);
                    purchaseDate = dialog.findViewById(R.id.editPurchaseDate);
                    purchaseKilometers = dialog.findViewById(R.id.editPurchaseKilometers);
                    purchaseLitres = dialog.findViewById(R.id.editPurchaseLitres);

                    purchaseCost.setText(getItem(position).getCost());
                    purchaseDate.setText(getItem(position).getDate());
                    purchaseKilometers.setText(getItem(position).getKilo());
                    purchaseLitres.setText(getItem(position).getLitre());

                    Button saveChange = dialog.findViewById(R.id.save_change);
                    saveChange.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View view){
                            Double newCost = Double.parseDouble(purchaseCost.getText().toString());
                            String newDate = purchaseDate.getText().toString();
                            Double newKilo = Double.parseDouble(purchaseKilometers.getText().toString());
                            Double newLitre = Double.parseDouble(purchaseLitres.getText().toString());
                            Toast toast = Toast.makeText(ctx, "Purchase was updated", Toast.LENGTH_LONG);
                            toast.show();
                            ctx.updatePurchase(getItemId(position), position, newDate, newCost, newKilo, newLitre);
                            dialog.dismiss();
                        }
                    });
                    Button cancelChange = dialog.findViewById(R.id.cancel_change);
                    cancelChange.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View view){
                            dialog.dismiss();
                            selectedPostion = -1;
                            notifyDataSetChanged();
                        }
                    });
                    dialog.show();
                }
            });

            delete.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){

                    dialog = new Dialog(ctx);
                    dialog.setContentView(R.layout.automobile_delete_confirmation);

                    Button deletePurchase = dialog.findViewById(R.id.delete_purchase);
                    deletePurchase.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View view){
                            Toast toast = Toast.makeText(ctx, "Purchase was deleted", Toast.LENGTH_LONG);
                            toast.show();
                            ctx.deletePurchase(getItemId(position), position);
                            dialog.dismiss();
                            selectedPostion = -1;
                            notifyDataSetChanged();
                        }
                    });

                    Button cancelDeletePurchase = dialog.findViewById(R.id.cancel_delete_purchase);
                    cancelDeletePurchase.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View view){
                            dialog.dismiss();
                            selectedPostion = -1;
                            notifyDataSetChanged();
                        }
                    });
                    dialog.show();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    selectedPostion = -1;
                    notifyDataSetChanged();
                }
            });
            return selectedView;
        }
        return convertView;
    }

    public long getItemId(int pos){
        Cursor cursor = ctx.getTotalCursor();
        if(cursor.moveToFirst()){
            cursor.moveToPosition(pos);
            long itemId = cursor.getLong(cursor.getColumnIndex(AutoDatabaseHelper.KEY_ID));
            cursor.close();
            return itemId;
        }else{
            cursor.close();
            return -1;
        }
    }

    /**
     *Used by the deletePurchase and updatePurchase methods to determine which row in the Database to interact with.
     * @param pos the position of the View last selected by the user.
     * @return the id value stored in the Database correlating view selected by the user.
     */
    void selectPurchase(int pos){
        this.selectedPostion=pos;
    }

    /**
     * ViewHolder class designed to hold the reference values for the views stored in the custom views displayed in AllAutoPurchase's ListView
     */
    static class PurchaseListHolder{
        TextView date;
    }
}
