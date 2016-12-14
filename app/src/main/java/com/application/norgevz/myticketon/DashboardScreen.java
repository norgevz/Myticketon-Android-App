package com.application.norgevz.myticketon;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.application.norgevz.myticketon.adapters.CustomTabsPagerAdapter;
import com.application.norgevz.myticketon.repos.Ticket;
import com.application.norgevz.myticketon.repos.TicketsRepository;
import com.application.norgevz.myticketon.settings.Settings;
import com.application.norgevz.myticketon.tabs.SlidingTabLayout;
import com.google.zxing.Result;

import org.json.JSONException;

import java.util.ArrayList;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by norgevz on 12/11/2016.
 */

public class DashboardScreen extends AppCompatActivity implements ZXingScannerView.ResultHandler, TicketsRepository.onTicketsRequest{


    private Toolbar toolbar;

    private ViewPager mPager;

    private SlidingTabLayout mTabs;

    private ZXingScannerView mScannerView;

    TicketsRepository ticketsRepository;

    //private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.mipmap.icon);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //recyclerView = (RecyclerView) findViewById(R.id.tickets_lists);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new CustomTabsPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setCustomTabView(R.layout.custom_tab_layout, R.id.tabText);
        mTabs.setDistributeEvenly(true);

        //TODO Replace getDrawable
        mTabs.setBackground(getResources().getDrawable(R.color.app_toolbar));
        //TODO Replace getColor
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        mTabs.setViewPager(mPager);

        ticketsRepository = new TicketsRepository();
        ticketsRepository.setListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.logout_action){
            System.out.println("Logging out");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void OnScanButtonClicked(View view) throws Exception {

        //TODO Check if scan is canceled
        /*
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera
        */
        handleStringResult("pdla_56422,pdla_56423,pdla_56441");
    }

    @Override
    public void handleResult(Result result) {
        // Do something with the result here

        Log.e("handler", result.getText()); // Prints scan results
        Log.e("handler", result.getBarcodeFormat().toString()); // Prints the scan format (qrcode)

        //String line = "pdla_56422,pdla_56423,pdla_56441";

        String line = result.getText();
        handleStringResult(line);

        /*
        // show the scanner result into dialog box.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(result.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();

        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);
        */
    }

    public void handleStringResult(String result){
        String providerId = null;
        try {
            providerId = Settings.getSettings("providerId");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] values = result.split(",");

        ArrayList<String> ordersHeadersId = new ArrayList<>();

        for (String value: values) {
            if(value.startsWith(providerId))
                ordersHeadersId.add(value);
        }

        try {
            ticketsRepository.getTickets(ordersHeadersId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }


    @Override
    public void onResponse(ArrayList<Ticket> tickets) {
        for (Ticket ticket : tickets){
            System.out.println(ticket.showName);
            System.out.println(ticket.theaterName);
            System.out.println(ticket.StartTime);
            System.out.println(ticket.reedemState);
            System.out.println();
        }
    }

    @Override
    public void onFailResponse(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(message);
        AlertDialog alert1 = builder.create();
        alert1.show();
    }
}
