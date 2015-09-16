package com.ntilde.donantes;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ntilde.donantes.adapters.FirstConfigViewPagerAdapter;

/**
 * Created by 0011361 on 16/09/2015.
 */
public class FirstConfig extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_config);
        Log.i("XXX", "FIRST CONFIG CREADA!");
        loadUI();
    }

    private void loadUI(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.first_config_viewpager);
        viewPager.setAdapter(new FirstConfigViewPagerAdapter(this));

        TextView tvSkip = (TextView) findViewById(R.id.first_config_skip);
        TextView tvNext = (TextView) findViewById(R.id.first_config_next);
    }
}
