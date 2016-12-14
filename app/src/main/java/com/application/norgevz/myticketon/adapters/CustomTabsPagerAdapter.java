package com.application.norgevz.myticketon.adapters;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.application.norgevz.myticketon.R;
import com.application.norgevz.myticketon.fragments.ProfileFragment;
import com.application.norgevz.myticketon.fragments.StatisticsFragment;
import com.application.norgevz.myticketon.fragments.TicketsFragment;
import com.application.norgevz.myticketon.global.MyApplication;

/**
 * Created by norgevz on 12/13/2016.
 */
public class CustomTabsPagerAdapter extends FragmentPagerAdapter {

    String[] tabs;
    int icons[] = {R.drawable.stats, R.drawable.tickets, R.drawable.tickets};

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