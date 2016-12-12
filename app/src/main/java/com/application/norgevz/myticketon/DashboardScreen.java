package com.application.norgevz.myticketon;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.application.norgevz.myticketon.fragments.ProfileFragment;
import com.application.norgevz.myticketon.fragments.StatisticsFragment;
import com.application.norgevz.myticketon.fragments.TicketsFragment;
import com.application.norgevz.myticketon.settings.Settings;
import com.application.norgevz.myticketon.tabs.SlidingTabLayout;

import java.util.ArrayList;

/**
 * Created by norgevz on 12/11/2016.
 */

public class DashboardScreen extends AppCompatActivity {


    private Toolbar toolbar;

    private ViewPager mPager;

    private SlidingTabLayout mTabs;

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

        Settings.addSetting("providerId", "pdla");
        String providerId = Settings.getSettings("providerId");

        String[] values = line.split(",");

        ArrayList<String> queries = new ArrayList<>();

        for (String value: values) {
            if(value.startsWith(providerId))
                queries.add(value);
        }

    }

    class CustomTabsPagerAdapter extends FragmentPagerAdapter {

        String[] tabs;
        int icons[] = {R.drawable.stats, R.drawable.tickets, R.drawable.tickets};

        public CustomTabsPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //TODO Replace getDrawable
            Drawable drawable = getResources().getDrawable(icons[position]);
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
