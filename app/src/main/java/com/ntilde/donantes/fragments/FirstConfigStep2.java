package com.ntilde.donantes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntilde.donantes.FirstConfig;
import com.ntilde.donantes.R;
import com.ntilde.donantes.utils.DefaultConfig;
import com.ntilde.donantes.utils.PicassoTransformationBlur;
import com.squareup.picasso.Picasso;

/**
 * Created by Julio on 30/09/2015.
 */
public class FirstConfigStep2 extends Fragment implements View.OnClickListener{

    ImageView aPos;
    ImageView aNeg;
    ImageView bPos;
    ImageView bNeg;
    ImageView abPos;
    ImageView abNeg;
    ImageView zeroPos;
    ImageView zeroNeg;

    TextView tvGroup;
    String grupoSanguineoSeleccionado=null;

    FirstConfig mActivity;
    ImageView ivBackground;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mActivity = (FirstConfig) getActivity();
        View view = inflater.inflate(R.layout.first_config_step2, container, false);
        loadUI(view);
        return view;
    }

    private void loadUI(View view){
        ivBackground = (ImageView) view.findViewById(R.id.first_config_step2_background);
        updateBackground();

        aPos = (ImageView) view.findViewById(R.id.first_config_step2_iv_aPos);
        aNeg = (ImageView) view.findViewById(R.id.first_config_step2_iv_aNeg);
        bPos = (ImageView) view.findViewById(R.id.first_config_step2_iv_bPos);
        bNeg = (ImageView) view.findViewById(R.id.first_config_step2_iv_bNeg);
        abPos = (ImageView) view.findViewById(R.id.first_config_step2_iv_abPos);
        abNeg = (ImageView) view.findViewById(R.id.first_config_step2_iv_abNeg);
        zeroPos = (ImageView) view.findViewById(R.id.first_config_step2_iv_zeroPos);
        zeroNeg = (ImageView) view.findViewById(R.id.first_config_step2_iv_zeroNeg);

        tvGroup = (TextView) view.findViewById(R.id.first_config_step2_group);


        aPos.setOnClickListener(this);
        aNeg.setOnClickListener(this);
        bPos.setOnClickListener(this);
        bNeg.setOnClickListener(this);
        abPos.setOnClickListener(this);
        abNeg.setOnClickListener(this);
        zeroPos.setOnClickListener(this);
        zeroNeg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Animation zoomOut = AnimationUtils.loadAnimation(v.getContext(), R.anim.zoom_out);
        v.startAnimation(zoomOut);

        zeroNeg.setImageResource(R.drawable.grupo_0_neg_off);
        zeroPos.setImageResource(R.drawable.grupo_0_pos_off);
        aNeg.setImageResource(R.drawable.grupo_a_neg_off);
        aPos.setImageResource(R.drawable.grupo_a_pos_off);
        bNeg.setImageResource(R.drawable.grupo_b_neg_off);
        bPos.setImageResource(R.drawable.grupo_b_pos_off);
        abNeg.setImageResource(R.drawable.grupo_ab_neg_off);
        abPos.setImageResource(R.drawable.grupo_ab_pos_off);

        if(v.getTag().toString().equals(grupoSanguineoSeleccionado)){
            grupoSanguineoSeleccionado = null;
            tvGroup.setText(" ");
            return;
        }

        switch(v.getTag().toString()){
            case "0-":
                ((ImageView) v).setImageResource(R.drawable.grupo_0_neg_on);
                break;
            case "0+":
                ((ImageView) v).setImageResource(R.drawable.grupo_0_pos_on);
                break;
            case "A-":
                ((ImageView) v).setImageResource(R.drawable.grupo_a_neg_on);
                break;
            case "A+":
                ((ImageView) v).setImageResource(R.drawable.grupo_a_pos_on);
                break;
            case "B-":
                ((ImageView) v).setImageResource(R.drawable.grupo_b_neg_on);
                break;
            case "B+":
                ((ImageView) v).setImageResource(R.drawable.grupo_b_pos_on);
                break;
            case "AB-":
                ((ImageView) v).setImageResource(R.drawable.grupo_ab_neg_on);
                break;
            case "AB+":
                ((ImageView) v).setImageResource(R.drawable.grupo_ab_pos_on);
                break;
        }

        grupoSanguineoSeleccionado=v.getTag().toString();
        tvGroup.setText(grupoSanguineoSeleccionado);

        Log.i("XXX", "Fijo grupo sanguíneo");
    }

    public String getGrupoSanguineoSeleccionado(){
        return grupoSanguineoSeleccionado;
    }

    public void updateBackground(){
        Log.i("XXX", "Actualizamos el background en paso 2");
        String url;
        int blur;

        if(mActivity.mSelectedCentroRegional != null && mActivity.mSelectedCentroRegional.getImagenCfg1() != null){
            Log.i("XXX", "El centro regional no es nulo");
            url = mActivity.mSelectedCentroRegional.getImagenCfg1().getUrl();
        }else{
            Log.i("XXX", "Centro regional nulo, cogemos las settings");
            url = DefaultConfig.ImgCfg1.getUrl();
            Log.i("XXX", url == null ? "Es nula la setting!" : ("No es nula la setting: " + url));
        }

        if(mActivity.mSelectedCentroRegional != null && mActivity.mSelectedCentroRegional.getImagenCfg1Radius() != null){
            blur = mActivity.mSelectedCentroRegional.getImagenCfg1Radius();
        }else{
            blur = DefaultConfig.ImgCfg1Radius;
        }

        Picasso.with(mActivity).load(url).transform(new PicassoTransformationBlur(blur)).into(ivBackground);
    }

}
