package com.ntilde.donantes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ntilde.donantes.adapters.FirstConfigViewPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 0011361 on 16/09/2015.
 */
public class FirstConfig extends AppCompatActivity {

    private int[] steps = {R.layout.first_config_step1, R.layout.first_config_step2, R.layout.first_config_step3};
    private int step = 0;

    @InjectView(R.id.first_config_left_button) TextView leftButton;
    @InjectView(R.id.first_config_right_button) TextView rightButton;
    @InjectView(R.id.first_config_viewpager) ViewPager viewPager;
    @InjectView(R.id.first_config_viewpager_indicator) CirclePageIndicator viewPagerIndicator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_config);

        ButterKnife.inject(this);
        configureViewPager();
    }

    private void configureViewPager(){
        step = 0;

        viewPager.setAdapter(new FirstConfigViewPagerAdapter(this, steps));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                step = position;
                leftButton.setVisibility(step == 0 ? View.GONE : View.VISIBLE);
                rightButton.setText(step == steps.length - 1 ? "Finalizar" : "Siguiente");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        viewPagerIndicator.setPageColor(Color.parseColor("#777777"));
        viewPagerIndicator.setFillColor(getResources().getColor(R.color.rojo));
        viewPagerIndicator.setStrokeColor(Color.parseColor("#444444"));
        viewPagerIndicator.setCentered(true);
        viewPagerIndicator.setRadius(10);
        viewPagerIndicator.setViewPager(viewPager);

    }

    @OnClick({R.id.first_config_left_button, R.id.first_config_right_button})
    public void onClickLeftTv(View v){
        switch(v.getId()){
            case R.id.first_config_left_button:
                viewPager.setCurrentItem(--step);
                break;
            case R.id.first_config_right_button:
                if(step==steps.length-1){
                    Toast.makeText(getApplicationContext(), "Fin", Toast.LENGTH_SHORT).show();
                }else {
                    viewPager.setCurrentItem(++step);
                }
                break;
        }
    }

}
