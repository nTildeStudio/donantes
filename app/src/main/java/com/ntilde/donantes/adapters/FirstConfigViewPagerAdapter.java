package com.ntilde.donantes.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ntilde.donantes.fragments.FirstConfigStep1;
import com.ntilde.donantes.fragments.FirstConfigStep2;
import com.ntilde.donantes.fragments.FirstConfigStep3;

/**
 * Created by 0011361 on 16/09/2015.
 */
public class FirstConfigViewPagerAdapter extends FragmentPagerAdapter{

    private Fragment[] mSteps;

    public FirstConfigViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
        mSteps = new Fragment[] {new FirstConfigStep1(), new FirstConfigStep2(), new FirstConfigStep3()};
    }

    @Override
    public Fragment getItem(int position) {
        return mSteps[position];
    }

    @Override
    public int getCount() {
        return mSteps.length;
    }

}
