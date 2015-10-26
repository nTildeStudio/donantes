package com.ntilde.donantes.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.ntilde.donantes.FirstConfig;
import com.ntilde.donantes.R;
import com.ntilde.donantes.utils.DefaultConfig;
import com.ntilde.donantes.utils.PicassoTransformationBlur;
import com.parse.ParseAnalytics;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julio on 30/09/2015.
 */
public class FirstConfigStep3 extends Fragment{

    private boolean notifications = true;
    EditText etNumDonante;
    FirstConfig mActivity;
    ImageView ivBackground;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (FirstConfig) getActivity();
        View view = inflater.inflate(R.layout.first_config_step3, container, false);
        loadUI(view);
        return view;
    }

    private void loadUI(View view){
        ivBackground = (ImageView) view.findViewById(R.id.first_config_step3_background);
        updateBackground();

        etNumDonante = (EditText) view.findViewById(R.id.first_config_step3_num_donante);
        etNumDonante.setHintTextColor(Color.parseColor("#dddddd"));
        etNumDonante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("configuracionInicial", "numeroDeDonante");
                ParseAnalytics.trackEventInBackground("click", parameters);
            }
        });

        final ImageView ivNotifications = (ImageView) view.findViewById(R.id.first_config_step3_notifications);
        ivNotifications.setImageResource(R.drawable.ic_notifications_active_white_24px);

        ivNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation zoomOut = AnimationUtils.loadAnimation(v.getContext(), R.anim.zoom_out);
                v.startAnimation(zoomOut);

                ivNotifications.setImageResource(notifications ? R.drawable.ic_notifications_off_white_24dp : R.drawable.ic_notifications_active_white_24px);
                notifications = !notifications;

                Map<String, String> parameters = new HashMap<>();
                parameters.put("configuracionInicial", "alertas");
                parameters.put("estado", notifications ? "on" : "off");
                ParseAnalytics.trackEventInBackground("click", parameters);
            }
        });
    }

    public boolean isNotificationsEnabled() {
        return notifications;
    }

    public String getNumDonante() {
        if(etNumDonante.getText().toString().length() == 0){
            return null;
        }
        return etNumDonante.getText().toString();
    }

    public void updateBackground(){
        String url;
        int blur;

        if(mActivity.mSelectedCentroRegional != null && mActivity.mSelectedCentroRegional.getImagenCfg2() != null){
            url = mActivity.mSelectedCentroRegional.getImagenCfg2().getUrl();
        }else{
            url = DefaultConfig.ImgCfg2.getUrl();
        }

        if(mActivity.mSelectedCentroRegional != null && mActivity.mSelectedCentroRegional.getImagenCfg2Radius() != null){
            blur = mActivity.mSelectedCentroRegional.getImagenCfg2Radius();
        }else{
            blur = DefaultConfig.ImgCfg2Radius;
        }

        Picasso.with(mActivity).invalidate(url);
        Picasso.with(mActivity).load(url).transform(new PicassoTransformationBlur(blur)).into(ivBackground);
    }
}
