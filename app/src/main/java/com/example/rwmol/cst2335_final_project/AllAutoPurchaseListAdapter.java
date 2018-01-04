package com.example.rwmol.cst2335_final_project;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

    AllAutoPurchaseListAdapter(Context ctx, ArrayList<PurchaseInfo> list){
        super(ctx, 0);
        inflater = LayoutInflater.from(ctx);
        this.list = list;
        this.ctx = (AutomobileActivity) ctx;
    }

    public int getCount(){
        return list.size();
    }

    public PurchaseInfo getItem(int pos){
        return list.get(pos);
    }

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

    void selectPurchase(int pos){
        this.selectedPostion=pos;
    }

    static class PurchaseListHolder{
        TextView date;
    }
}
