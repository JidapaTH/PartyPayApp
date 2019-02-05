package com.example.admin.partypay.EventActivityFile;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.admin.partypay.EventActivityFile.EventOverviewFragment;
import com.example.admin.partypay.EventActivityFile.EventSummaryFragment;
import com.example.admin.partypay.R;

/**
 * This class is used to show 2 fragment; EventSummaryFragment and EventOverviewFragment
 */


public class EventCategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public EventCategoryAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==0){
            return new EventSummaryFragment();
        } else {
            return  new EventOverviewFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position){
            case 0:
                return mContext.getString(R.string.event_tab_1);
            case 1:
                return mContext.getString(R.string.event_tab_2);
        }
        return null;
    }
}
