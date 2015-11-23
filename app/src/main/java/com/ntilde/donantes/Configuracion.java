package com.ntilde.donantes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ntilde.listaexpandible.ExpandCollapseAnimation;
import com.ntilde.percentagelayout.PLinearLayout;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class Configuracion extends ActionBarActivity {

    private String centroSeleccionado=null;
    private String grupoSanguineoSeleccionado=null;
    private String sexoSeleccionado=null;
    private Map<String,String> centrosRegionalesIdNombre;

    SupportMapFragment smfMapa;
    GoogleMap gmMapa;
    List<LatLng> otsLatLng;

    @InjectView(R.id.configuracion_logotipo)ImageView logotipo;
    @InjectViews({R.id.configuracion_grupo_0n, R.id.configuracion_grupo_0p, R.id.configuracion_grupo_an, R.id.configuracion_grupo_ap,
            R.id.configuracion_grupo_bn, R.id.configuracion_grupo_bp, R.id.configuracion_grupo_abn, R.id.configuracion_grupo_abp}) ImageView[] gruposSanguineos;
    @InjectView(R.id.configuracion_et_numero_donante) EditText numerodonante;
    @InjectView(R.id.enable_notifications)
    CheckBox checkNotificationes;
    @InjectView(R.id.mostrar_grupos) CheckBox mostrarGrupos;
    @InjectView(R.id.container_grupos) PLinearLayout contenedorGrupos;
    @InjectView(R.id.configuracion_cabecera) PLinearLayout cabecera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setContentView(R.layout.activity_configuracion_v2);

        ButterKnife.inject(this);

        smfMapa=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.configuracion_mapa);

        try {
            gmMapa=smfMapa.getMap();
            gmMapa.getUiSettings().setZoomControlsEnabled(false);
        } catch (Exception e) { }


        //TODO cambiar por parse manager
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CentrosRegionales");
        query.findInBackground((centrosRegionales, e) -> {
                if (e == null) {
                    otsLatLng = new ArrayList<>();
                    centrosRegionalesIdNombre = new HashMap<>();
                    for (ParseObject centroRegional : centrosRegionales) {
                        ParseGeoPoint ubicacion = centroRegional.getParseGeoPoint("Ubicacion");
                        if (ubicacion != null) {
                            LatLng latLng = new LatLng(ubicacion.getLatitude(), ubicacion.getLongitude());
                            otsLatLng.add(latLng);
                            gmMapa.addMarker(new MarkerOptions().position(latLng).title(centroRegional.getString("Nombre")));
                            centrosRegionalesIdNombre.put(centroRegional.getString("Nombre"), centroRegional.getObjectId());
                        }
                    }
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (LatLng otLatLng : otsLatLng) {
                        builder.include(otLatLng);
                    }
                    LatLngBounds bounds = builder.build();
                    gmMapa.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                    gmMapa.setOnMarkerClickListener(marker -> {
                            centroSeleccionado = centrosRegionalesIdNombre.get(marker.getTitle());
                            return false;
                        });
                }
            });

        gmMapa.setOnMapClickListener(latLng -> {
                numerodonante.clearFocus();
            });

        cabecera.setOnTouchListener((v, event) -> {
                numerodonante.clearFocus();
                return false;
            });

        checkNotificationes.setOnCheckedChangeListener((buttonView, isChecked) -> numerodonante.clearFocus());

        numerodonante.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            });

        cargarPreferencias();


    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @OnClick({R.id.configuracion_grupo_0n, R.id.configuracion_grupo_0p, R.id.configuracion_grupo_an, R.id.configuracion_grupo_ap,
            R.id.configuracion_grupo_bn, R.id.configuracion_grupo_bp, R.id.configuracion_grupo_abn, R.id.configuracion_grupo_abp})
    public void onGrupoClick(ImageView grupo){

        numerodonante.clearFocus();

        gruposSanguineos[0].setImageResource(R.drawable.grupo_0_neg_off);
        gruposSanguineos[1].setImageResource(R.drawable.grupo_0_pos_off);
        gruposSanguineos[2].setImageResource(R.drawable.grupo_a_neg_off);
        gruposSanguineos[3].setImageResource(R.drawable.grupo_a_pos_off);
        gruposSanguineos[4].setImageResource(R.drawable.grupo_b_neg_off);
        gruposSanguineos[5].setImageResource(R.drawable.grupo_b_pos_off);
        gruposSanguineos[6].setImageResource(R.drawable.grupo_ab_neg_off);
        gruposSanguineos[7].setImageResource(R.drawable.grupo_ab_pos_off);


        switch(grupo.getTag().toString()){
            case "0-":
                grupo.setImageResource(R.drawable.grupo_0_neg_on);
                break;
            case "0+":
                grupo.setImageResource(R.drawable.grupo_0_pos_on);
                break;
            case "A-":
                grupo.setImageResource(R.drawable.grupo_a_neg_on);
                break;
            case "A+":
                grupo.setImageResource(R.drawable.grupo_a_pos_on);
                break;
            case "B-":
                grupo.setImageResource(R.drawable.grupo_b_neg_on);
                break;
            case "B+":
                grupo.setImageResource(R.drawable.grupo_b_pos_on);
                break;
            case "AB-":
                grupo.setImageResource(R.drawable.grupo_ab_neg_on);
                break;
            case "AB+":
                grupo.setImageResource(R.drawable.grupo_ab_pos_on);
                break;
        }

        grupoSanguineoSeleccionado=grupo.getTag().toString();
    }



    @OnCheckedChanged(R.id.mostrar_grupos)
    public void mostrarGrupos(boolean checked){

        numerodonante.clearFocus();

        final int type = checked? ExpandCollapseAnimation.EXPAND : ExpandCollapseAnimation.COLLAPSE;

        Animation anim = new ExpandCollapseAnimation(
                contenedorGrupos,
                type
        );
        anim.setDuration(500);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (type == ExpandCollapseAnimation.EXPAND) {
                    contenedorGrupos.setVisibility(View.VISIBLE);
                }else{
                    contenedorGrupos.setVisibility(View.GONE);
                }

            }
        });
        contenedorGrupos.startAnimation(anim);
    }

    @OnClick(R.id.configuracion_buttonFloat)
    public void onGuardar(){

        numerodonante.clearFocus();

        SharedPreferences prefs = getSharedPreferences(Constantes.SP_KEY, Configuracion.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constantes.SP_CENTRO, centroSeleccionado);
        editor.putString(Constantes.SP_GRUPO, grupoSanguineoSeleccionado);
        editor.putString(Constantes.SP_SEXO, sexoSeleccionado);
        editor.putBoolean(Constantes.SP_NOTIFICACIONES, checkNotificationes.isChecked());
        editor.putString(Constantes.SP_NUMERO_DONANTE, numerodonante.getText().toString());
        editor.commit();

        ParseInstallation pi = ParseInstallation.getCurrentInstallation();
        ArrayList<String> channels = new ArrayList<>();
        String channel = centroSeleccionado+"_"+grupoSanguineoSeleccionado;
        channel = channel.replace("+","POS").replace("-","NEG");
        if(checkNotificationes.isChecked()) channels.add(channel);
        pi.put("channels", channels);
        pi.put("numeroDonante", numerodonante.getText().toString());
        pi.saveInBackground();

        finish();
    }

    private void cargarPreferencias(){
        SharedPreferences prefs = getSharedPreferences(Constantes.SP_KEY, Configuracion.MODE_PRIVATE);
        centroSeleccionado=prefs.getString(Constantes.SP_CENTRO, null);
        grupoSanguineoSeleccionado=prefs.getString(Constantes.SP_GRUPO, null);
        sexoSeleccionado=prefs.getString(Constantes.SP_SEXO, null);
        checkNotificationes.setChecked(prefs.getBoolean(Constantes.SP_NOTIFICACIONES, false));
        numerodonante.setText(prefs.getString(Constantes.SP_NUMERO_DONANTE, ""));
        for(ImageView grupo:gruposSanguineos){
            if(grupo.getTag().toString().equals(grupoSanguineoSeleccionado)){
                switch(grupo.getTag().toString()){
                    case "0-":
                        grupo.setImageResource(R.drawable.grupo_0_neg_on);
                        break;
                    case "0+":
                        grupo.setImageResource(R.drawable.grupo_0_pos_on);
                        break;
                    case "A-":
                        grupo.setImageResource(R.drawable.grupo_a_neg_on);
                        break;
                    case "A+":
                        grupo.setImageResource(R.drawable.grupo_a_pos_on);
                        break;
                    case "B-":
                        grupo.setImageResource(R.drawable.grupo_b_neg_on);
                        break;
                    case "B+":
                        grupo.setImageResource(R.drawable.grupo_b_pos_on);
                        break;
                    case "AB-":
                        grupo.setImageResource(R.drawable.grupo_ab_neg_on);
                        break;
                    case "AB+":
                        grupo.setImageResource(R.drawable.grupo_ab_pos_on);
                        break;
                }
            }
        }
    }

}
