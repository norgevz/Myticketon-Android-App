package com.application.norgevz.myticketon.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.norgevz.myticketon.DashboardScreen;
import com.application.norgevz.myticketon.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

/**
 * Created by norgevz on 12/11/2016.
 */

public class TicketsFragment extends Fragment {

    private String toast;
    private DashboardScreen activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.tickets_list_layout, container, false);
        FloatingActionButton scanButton = (FloatingActionButton) layout.findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    scanFromFragment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (DashboardScreen) context;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void scanFromFragment() {
        activity.handleStringResult("pdla_56422,pdla_56423,pdla_56441");
        /*
        IntentIntegrator intentIntegrator =IntentIntegrator.forSupportFragment(this);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.initiateScan();
        */

    }

    private void displayToast() {
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                toast = "Cancelled from fragment";
                displayToast();
            } else {

                //toast = "Scanned from fragment: " + result.getContents();
                String line = result.getContents();
                activity.handleStringResult(line);
            }
        }
    }

}
