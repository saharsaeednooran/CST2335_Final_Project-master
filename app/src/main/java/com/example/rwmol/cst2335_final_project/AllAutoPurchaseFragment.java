package com.example.rwmol.cst2335_final_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A custom Fragment that holds and displays information related to a purchase.
 *
 * @author Ryan Molitor
 * @version 1.2
 */
public class AllAutoPurchaseFragment extends Fragment {

    AllAutoPurchaseListAdapter purchaseListAdapter;
    ListView purchaseList;

    /**
     * Creates a new Instance of AllAutoPurchaseFragment that accepts a that uses an existing PurchaseAdapter
     *
     * @param purchaseAdapter the PurchaseAdapter that is linked to a PurchaseInfo.
     * @return fragment.
     */
    public static AllAutoPurchaseFragment newInstance(ArrayAdapter<PurchaseInfo> purchaseAdapter){
        AllAutoPurchaseFragment fragment = new AllAutoPurchaseFragment();
        fragment.purchaseListAdapter = (AllAutoPurchaseListAdapter)purchaseAdapter;
        return fragment;
    }

    /**
     * Overriden version of Fragment's onCreate method that also retains the instance of the fragment,
     * allowing it to persist after an Orientation change.
     * {@inheritDoc}
     * @param bundle
     */
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    /**
     * Inflates the "automobile_all_purchase_fragment" layout for the View. Layout consists of a single ListView
     *
     * {@inheritDoc}
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.automobile_all_purchase_fragment, container, false);
    }

    /**
     * Links the local ListView reference to the one inflated by onCreateView and adds an onItemClickListener.
     * The onItemClickListener notifies the adapter of the View selected as well as a data change.
     * {@inheritDoc}
     * @param view
     * @param savedInstanceState
     */
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
