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

public class AllAutoPurchaseListAdapter extends ArrayAdapter<PurchaseInfo> {

    private int selectedPostion = -1;

    private EditText purchaseDate;
    private EditText purchaseCost;
    private EditText purchaseKilometers;
    private EditText purchaseLitres;

    private LayoutInflater inflater;
    private AutomobileActivity ctx;
    private ArrayList<PurchaseInfo> list;
    private Dialog dialog;

    public AllAutoPurchaseListAdapter(Context ctx, ArrayList<PurchaseInfo> list){
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
            holder.cost = convertView.findViewById(R.id.costInfo);
            holder.date = convertView.findViewById(R.id.dateInfo);
            holder.kilo = convertView.findViewById(R.id.kiloInfo);
            holder.litre = convertView.findViewById(R.id.litreInfo);
            convertView.setTag(holder);
        }else{
            holder = (PurchaseListHolder) convertView.getTag();
        }

        holder.cost.setText(list.get(position).getCost());
        holder.date.setText(list.get(position).getDate());
        holder.kilo.setText(list.get(position).getKilo());
        holder.litre.setText(list.get(position).getLitre());

        if(this.selectedPostion==position){
            View selectedView = inflater.inflate(R.layout.automobile_info_view_selected_layout, parent, false);

            TextView cost = selectedView.findViewById(R.id.purchaseCost);
            TextView date = selectedView.findViewById(R.id.purchaseDate);
            TextView kilo = selectedView.findViewById(R.id.purchaseKilometers);
            TextView litre = selectedView.findViewById(R.id.purchaseLitres);
            Button edit = selectedView.findViewById(R.id.edit_purchase);
            Button delete = selectedView.findViewById(R.id.delete_purchase);

            cost.setText("$" + list.get(position).getCost());
            date.setText(list.get(position).getDate());
            kilo.setText(list.get(position).getKilo() + "kg");
            litre.setText(list.get(position).getLitre() + "L");

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
                }
            });

            delete.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){

                    dialog = new Dialog(ctx);
                    dialog.setContentView(R.layout.automobile_delete_dialog_layout);
                    dialog.setTitle(getContext().getResources().getString(R.string.automobile_delete_dialog_title));
                    purchaseCost = dialog.findViewById(R.id.deletePurchaseCost);
                    purchaseDate = dialog.findViewById(R.id.deletePurchaseDate);
                    purchaseKilometers = dialog.findViewById(R.id.deletePurchaseKilometers);
                    purchaseLitres = dialog.findViewById(R.id.deletePurchaseLitres);

                    purchaseCost.setText(getItem(position).getCost());
                    purchaseDate.setText(getItem(position).getDate());
                    purchaseKilometers.setText(getItem(position).getKilo());
                    purchaseLitres.setText(getItem(position).getLitre());

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
        TextView date, cost, kilo, litre;
    }

}