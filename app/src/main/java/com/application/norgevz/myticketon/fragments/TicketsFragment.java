package com.application.norgevz.myticketon.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.norgevz.myticketon.DashboardScreen;
import com.application.norgevz.myticketon.R;
import com.application.norgevz.myticketon.adapters.TicketsAdapter;
import com.application.norgevz.myticketon.models.Ticket;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by norgevz on 12/11/2016.
 */

public class TicketsFragment extends Fragment {

    private String toast;

    private DashboardScreen activity;

    private RecyclerView recyclerView;

    private TicketsAdapter ticketsAdapter;


    private TextView emptyListTextView;

    public void setEmptyTextViewVisibility(int visibility){
        emptyListTextView.setVisibility(visibility);
    }

    public TicketsAdapter getTicketsAdapter() {
        return ticketsAdapter;
    }

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

        recyclerView = (RecyclerView) layout.findViewById(R.id.tickets_lists);
        ticketsAdapter = new TicketsAdapter(getActivity(), activity.ticketsList, new TicketsAdapter.MyAdapterListener() {
            @Override
            public void iconImageViewOnClick(View v, Ticket ticket, int position) {
                activity.updateOrder(ticket.order, position);
            }
        });

        recyclerView.setAdapter(ticketsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        emptyListTextView = (TextView) layout.findViewById(R.id.empty_list_text_view);

        if(activity.ticketsList.size() == 0)
            emptyListTextView.setVisibility(View.VISIBLE);

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
        //activity.handleStringResult("pdla_56422,pdla_56423,pdla_56441");

        IntentIntegrator intentIntegrator =IntentIntegrator.forSupportFragment(this);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.initiateScan();

    }

    private void displayToast() {
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    public void updateList(ArrayList<Ticket> tickets){
        ticketsAdapter.update(tickets);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                toast = "Cancelled by user";
                displayToast();
            } else {

                //toast = "Scanned from fragment: " + result.getContents();
                String line = result.getContents();
                activity.handleStringResult(line);
            }
        }
    }

}
