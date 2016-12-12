package com.application.norgevz.myticketon;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.application.norgevz.myticketon.settings.Settings;

import java.util.ArrayList;

/**
 * Created by norgevz on 12/11/2016.
 */

public class DashboardScreen extends AppCompatActivity {


    private Toolbar toolbar;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.mipmap.icon);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = (RecyclerView) findViewById(R.id.tickets_lists);
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

        String line = "pdla_56422,pdla_56423,pdla_56441";

        String providerId = Settings.getSettings("providerId");

        String[] values = line.split(",");

        ArrayList<String> queries = new ArrayList<>();

        for (String value: values) {
            if(value.startsWith(providerId))
                queries.add(value);
        }

    }
}
