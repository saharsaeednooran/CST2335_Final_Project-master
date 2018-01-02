package com.example.rwmol.cst2335_final_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AllAutoPurchaseFragment extends Fragment {

    AllAutoPurchaseListAdapter purchaseListAdapter;
    ListView purchaseList;

    public static AllAutoPurchaseFragment newInstance(ArrayAdapter<PurchaseInfo> purchaseAdapter){
        AllAutoPurchaseFragment fragment = new AllAutoPurchaseFragment();
        fragment.purchaseListAdapter = (AllAutoPurchaseListAdapter)purchaseAdapter;
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.automobile_all_purchase_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        purchaseList = view.findViewById(R.id.all_purchase_list_view);
        purchaseList.setAdapter(purchaseListAdapter);
        purchaseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                purchaseListAdapter.selectPurchase(i);
                purchaseListAdapter.notifyDataSetChanged();
            }
        });
    }
}
