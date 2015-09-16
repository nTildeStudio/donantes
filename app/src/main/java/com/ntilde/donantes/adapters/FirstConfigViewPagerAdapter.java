package com.ntilde.donantes.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntilde.donantes.R;

/**
 * Created by 0011361 on 16/09/2015.
 */
public class FirstConfigViewPagerAdapter extends PagerAdapter{

    private Context mContext;
    private int[] steps = {R.layout.first_config_step1, R.layout.first_config_step2, R.layout.first_config_step3};

    public FirstConfigViewPagerAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return steps.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view;
        view = LayoutInflater.from(mContext).inflate(steps[position], null);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }
}
