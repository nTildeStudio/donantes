package com.ntilde.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ntilde.app.Constantes;
import com.ntilde.app.DonantesApplication;
import com.ntilde.app.DonantesPreferences;
import com.ntilde.app.R;
import com.ntilde.app.adapters.FirstConfigViewPagerAdapter;
import com.ntilde.app.fragments.FirstConfigStep2;
import com.ntilde.app.fragments.FirstConfigStep3;
import com.ntilde.exception.InvalidValueType;
import com.ntilde.domain.CentroRegional;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Julio on 16/09/2015.
 */
public class FirstConfig extends AppCompatActivity {

    private static final String TAG = FirstConfig.class.getName();

    public List<CentroRegional> mCentrosRegionales;
    public CentroRegional mSelectedCentroRegional;

    private int step = 0;

    @InjectView(R.id.first_config_left_button) TextView leftButton;
    @InjectView(R.id.first_config_right_button) TextView rightButton;
    @InjectView(R.id.first_config_viewpager) ViewPager viewPager;
    @InjectView(R.id.first_config_viewpager_indicator) CirclePageIndicator viewPagerIndicator;

    private DonantesPreferences preferences = DonantesApplication.getInstance().getPrefrences();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_config);

        ButterKnife.inject(this);
        mCentrosRegionales = new ArrayList<>();
        configureViewPager();
    }

    private void configureViewPager(){
        updateBottomButtons(step);

        viewPager.setAdapter(new FirstConfigViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateBottomButtons(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        viewPagerIndicator.setPageColor(Color.parseColor("#ffffff"));
        viewPagerIndicator.setFillColor(getResources().getColor(R.color.rojo));
        viewPagerIndicator.setStrokeColor(Color.parseColor("#000000"));
        viewPagerIndicator.setCentered(true);
        viewPagerIndicator.setRadius(10);
        viewPagerIndicator.setViewPager(viewPager);

    }

    @OnClick({R.id.first_config_left_button, R.id.first_config_right_button})
    public void onClickLeftTv(View v){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("paso", "" + viewPager.getCurrentItem());

        switch(v.getId()){
            case R.id.first_config_left_button:
                parameters.put("configuracionInicial", "anterior");
                viewPager.setCurrentItem(--step);
                break;
            case R.id.first_config_right_button:
                parameters.put("configuracionInicial", "siguiente");
                if(step==viewPager.getAdapter().getCount()-1){
                    saveInfoGoToMain();
                }else {
                    viewPager.setCurrentItem(++step);
                }
                break;
        }

        ParseAnalytics.trackEventInBackground("click", parameters);
    }

    private void saveInfoGoToMain(){

        boolean datosOk = true;

        String grupoSanguineoSeleccionado = ((FirstConfigStep2) ((FirstConfigViewPagerAdapter) viewPager.getAdapter()).getItem(1)).getGrupoSanguineoSeleccionado();
        Boolean notificationsEnabled = ((FirstConfigStep3) ((FirstConfigViewPagerAdapter) viewPager.getAdapter()).getItem(2)).isNotificationsEnabled();
        String numDonante = ((FirstConfigStep3) ((FirstConfigViewPagerAdapter) viewPager.getAdapter()).getItem(2)).getNumDonante();


        if(mSelectedCentroRegional==null){
            datosOk=false;
            viewPager.setCurrentItem(0);
            updateBottomButtons(viewPager.getCurrentItem());
        }

        if(grupoSanguineoSeleccionado==null){
            datosOk=false;
            updateBottomButtons(viewPager.getCurrentItem());
        }


        if(datosOk) {
            storeInPreferences(grupoSanguineoSeleccionado,notificationsEnabled,numDonante);

            ParseInstallation pi = ParseInstallation.getCurrentInstallation();
            ArrayList<String> channels = new ArrayList<>();
            String channel = mSelectedCentroRegional.getObjectId()+"_"+grupoSanguineoSeleccionado;
            channel = channel.replace("+","POS").replace("-","NEG");
            if(notificationsEnabled) channels.add(channel);
            pi.put("channels", channels);
            if(numDonante != null) pi.put("numeroDonante", numDonante);
            pi.saveInBackground();
            startActivity(new Intent(FirstConfig.this, MenuPrincipal.class));
        }
    }

    private void storeInPreferences(String grupoSanguineo, boolean notificationsEnabled, String numDonante){
        try{
            preferences.put(Constantes.SP_CENTRO, mSelectedCentroRegional.getObjectId());
            preferences.put(Constantes.SP_GRUPO, grupoSanguineo);
            preferences.put(Constantes.SP_NOTIFICACIONES, notificationsEnabled);
            preferences.put(Constantes.SP_NUMERO_DONANTE, numDonante);
            preferences.commit();

        }catch (InvalidValueType e){
            Log.e(TAG,"Error almacenando en preferencias");
        }
    }

    @Override
    public void onBackPressed() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("configuracionInicial", "onBack");
        parameters.put("paso", ""+viewPager.getCurrentItem());
        ParseAnalytics.trackEventInBackground("click", parameters);

        if(viewPager.getCurrentItem() > 0){
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            updateBottomButtons(viewPager.getCurrentItem());
        }
    }

    private void updateBottomButtons(int position){
        step = position;
        leftButton.setVisibility(step == 0 ? View.GONE : View.VISIBLE);
        rightButton.setText(viewPager !=null && viewPager.getAdapter() != null && step == viewPager.getAdapter().getCount() - 1 ? "Finalizar" : "Siguiente");
    }

    public void selectCentroRegional(String title) {
        for(CentroRegional centro : mCentrosRegionales){
            if(centro.getNombre().equals(title)){
                mSelectedCentroRegional = centro;
                break;
            }
        }

        ((FirstConfigStep2) ((FirstConfigViewPagerAdapter) viewPager.getAdapter()).getItem(1)).updateBackground();
        ((FirstConfigStep3) ((FirstConfigViewPagerAdapter) viewPager.getAdapter()).getItem(2)).updateBackground();
    }



}
