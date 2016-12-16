package com.application.norgevz.myticketon;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.application.norgevz.myticketon.fragments.ProfileFragment;
import com.application.norgevz.myticketon.fragments.StatisticsFragment;
import com.application.norgevz.myticketon.fragments.TicketsFragment;
import com.application.norgevz.myticketon.global.MyApplication;
import com.application.norgevz.myticketon.repos.Ticket;
import com.application.norgevz.myticketon.repos.TicketsRepository;
import com.application.norgevz.myticketon.settings.Settings;
import com.application.norgevz.myticketon.tabs.SlidingTabLayout;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by norgevz on 12/11/2016.
 */

public class DashboardScreen extends AppCompatActivity implements TicketsRepository.onTicketsRequest{


    private Toolbar toolbar;

    private ViewPager mPager;

    private SlidingTabLayout mTabs;

    TicketsRepository ticketsRepository;

    TicketsFragment dashboardTicketsFragment;

    public ArrayList<Ticket> ticketsList = new ArrayList<>();

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


    @Override
    public void onResponse(ArrayList<Ticket> tickets) {
        ticketsList = tickets;
        if(dashboardTicketsFragment != null){
            dashboardTicketsFragment.getTicketsAdapter().update(tickets);
        }
//
//        for (Ticket ticket : tickets){
//            System.out.println(ticket.showName);
//            System.out.println(ticket.theaterName);
//            System.out.println(ticket.StartTime);
//            System.out.println(ticket.reedemState);
//            System.out.println();
//        }

    }

    @Override
    public void onFailResponse(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(message);
        AlertDialog alert1 = builder.create();
        alert1.show();
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

        if(ordersHeadersId.size() == 0){
            dashboardTicketsFragment.setEmptyTextViewVisibility(View.VISIBLE);
            ticketsList.clear();
            dashboardTicketsFragment.getTicketsAdapter().update(ticketsList);
            return;
        }

        try {
            dashboardTicketsFragment.setEmptyTextViewVisibility(View.INVISIBLE);
            ticketsRepository.getTickets(ordersHeadersId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public class CustomTabsPagerAdapter extends FragmentPagerAdapter {

        String[] tabs;
        int icons[] = {R.drawable.stats, R.drawable.tickets, R.drawable.profile};

        public CustomTabsPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = MyApplication.getAppContext().getResources().getStringArray(R.array.tabs);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //TODO Replace getDrawable
            Drawable drawable = MyApplication.getAppContext().getResources().getDrawable(icons[position]);
            drawable.setBounds(0 , 0 , 64, 64);
            ImageSpan imageSpan = new ImageSpan(drawable);
            SpannableString spannableString = new SpannableString(" ");
            spannableString.setSpan(imageSpan, 0 , spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    StatisticsFragment statisticsTab = new StatisticsFragment();
                    return statisticsTab;
                case 1:

                    TicketsFragment ticketsFragment = new TicketsFragment();
                    dashboardTicketsFragment = ticketsFragment;

                    return ticketsFragment;
                case 2:
                    ProfileFragment profileFragment = new ProfileFragment();
                    return profileFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
