package com.ntilde.donantes.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ntilde.donantes.R;
import com.squareup.picasso.Picasso;

/**
 * Created by 0011361 on 16/09/2015.
 */
public class FirstConfigViewPagerAdapter extends PagerAdapter{

    private Context mContext;
    private int[] mSteps;

    public FirstConfigViewPagerAdapter(Context context, int[] steps){
        mContext = context;
        mSteps = steps;
    }

    @Override
    public int getCount() {
        return mSteps.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = generateViewFor(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    private View generateViewFor(int position){
        View v = LayoutInflater.from(mContext).inflate(mSteps[position], null);
        int background = R.drawable.donantes;
        switch(position){
            case 0:
                background = R.drawable.donantes;
                break;
            case 1:
                background = R.drawable.donantes2;
                break;
            case 2:
                background = R.drawable.donantes3;
               break;
        }


        Picasso.with(mContext).load(background).into((ImageView) v.findViewById(R.id.background));
        return v;
    }

}
