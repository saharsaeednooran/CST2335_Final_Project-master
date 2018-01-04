package com.example.rwmol.cst2335_final_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class AutomobileMonthlyGasFragment extends Fragment {

    ListView view;
    AutomobileActivity activity;

    public static AutomobileMonthlyGasFragment newInstance(AutomobileActivity activity) {
        AutomobileMonthlyGasFragment fragment = new AutomobileMonthlyGasFragment();
        fragment.activity = activity;
        return fragment;
    }

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.automobile_month_gas_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        TextView janView, febView, marView, aprView, mayView, junView, julView, augView, sepView, octView, novView, decView;

        janView = view.findViewById(R.id.janAvg);
        febView = view.findViewById(R.id.febAvg);
        marView = view.findViewById(R.id.marAvg);
        aprView = view.findViewById(R.id.aprAvg);
        mayView = view.findViewById(R.id.mayAvg);
        junView = view.findViewById(R.id.junAvg);
        julView = view.findViewById(R.id.julAvg);
        augView = view.findViewById(R.id.augAvg);
        sepView = view.findViewById(R.id.sepAvg);
        octView = view.findViewById(R.id.octAvg);
        novView = view.findViewById(R.id.novAvg);
        decView = view.findViewById(R.id.decAvg);

        for (int i = 1; i <= 12; i++) {
            if (i == 1) {
                if(activity.calculateMonthlySum(i) != null){
                    janView.setText(activity.calculateMonthlySum(i));
                }
            } else if (i == 2) {
                if(activity.calculateMonthlySum(i) != null){
                    febView.setText(activity.calculateMonthlySum(i));
                }
            } else if (i == 3) {
                if(activity.calculateMonthlySum(i) != null){
                    marView.setText(activity.calculateMonthlySum(i));
                }
            } else if (i == 4) {
                if(activity.calculateMonthlySum(i) != null){
                    aprView.setText(activity.calculateMonthlySum(i));
                }
            } else if (i == 5) {
                if(activity.calculateMonthlySum(i) != null){
                    mayView.setText(activity.calculateMonthlySum(i));
                }
            } else if (i == 6) {
                if(activity.calculateMonthlySum(i) != null){
                    junView.setText(activity.calculateMonthlySum(i));
                }
            } else if (i == 7) {
                if(activity.calculateMonthlySum(i) != null){
                    julView.setText(activity.calculateMonthlySum(i));
                }
            } else if (i == 8) {
                if(activity.calculateMonthlySum(i) != null){
                    augView.setText(activity.calculateMonthlySum(i));
                }
            } else if (i == 9) {
                if(activity.calculateMonthlySum(i) != null){
                    sepView.setText(activity.calculateMonthlySum(i));
                }
            } else if (i == 10) {
                if(activity.calculateMonthlySum(i) != null){
                    octView.setText(activity.calculateMonthlySum(i));
                }
            } else if (i == 11) {
                if(activity.calculateMonthlySum(i) != null){
                    novView.setText(activity.calculateMonthlySum(i));
                }
            } else if (i == 12) {
                if(activity.calculateMonthlySum(i) != null){
                    decView.setText(activity.calculateMonthlySum(i));
                }
            } else {
                break;
            }
        }
    }
}
