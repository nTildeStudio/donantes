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

import com.ntilde.donantes.R;
import com.ntilde.donantes.utils.PicassoTransformationBlur;
import com.squareup.picasso.Picasso;

/**
 * Created by 0011361 on 30/09/2015.
 */
public class FirstConfigStep3 extends Fragment{

    private boolean notifications = false;
    EditText etNumDonante;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_config_step3, container, false);
        loadUI(view);
        return view;
    }

    private void loadUI(View view){
        ImageView ivBackground = (ImageView) view.findViewById(R.id.first_config_step3_background);
        Picasso.with(getActivity()).load(R.drawable.donantes3).transform(new PicassoTransformationBlur()).into(ivBackground);

        etNumDonante = (EditText) view.findViewById(R.id.first_config_step3_num_donante);
        etNumDonante.setHintTextColor(Color.parseColor("#dddddd"));

        ImageView ivNotifications = (ImageView) view.findViewById(R.id.first_config_step3_notifications);
        ivNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation zoomOut = AnimationUtils.loadAnimation(v.getContext(), R.anim.zoom_out);
                v.startAnimation(zoomOut);

                ((ImageView) v).setImageResource(notifications ? R.drawable.ic_notifications_off_white_24dp : R.drawable.ic_notifications_active_white_24px);
                notifications = !notifications;
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
}
