package com.ntilde.donantes;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gc.materialdesign.views.Switch;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ntilde.percentagelayout.PLinearLayout;
import com.ntilde.percentagelayout.PTextView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class Configuracion extends ActionBarActivity {

    private String centroSeleccionado=null;
    private String grupoSanguineoSeleccionado=null;
    private String sexoSeleccionado=null;
    private Map<String,String> centrosRegionalesIdNombre;

    SupportMapFragment smfMapa;
    GoogleMap gmMapa;
    List<LatLng> otsLatLng;

    @InjectView(R.id.iconos_margen_superior) PLinearLayout ic_margen_sup;
    @InjectView(R.id.configuracion_logotipo)ImageView logotipo;
    @InjectView(R.id.configuracion_borde_rojo_superior) PLinearLayout borde_rojo_superior;
    @InjectView(R.id.configuracion_borde_rojo_inferior) LinearLayout borde_rojo_inferior;
    @InjectViews({R.id.configuracion_grupo_0n, R.id.configuracion_grupo_0p, R.id.configuracion_grupo_an, R.id.configuracion_grupo_ap,
            R.id.configuracion_grupo_bn, R.id.configuracion_grupo_bp, R.id.configuracion_grupo_abn, R.id.configuracion_grupo_abp}) ImageView[] gruposSanguineos;
    @InjectViews({R.id.configuracion_sexo_masculino, R.id.configuracion_sexo_femenino}) Button[] sexos;
    @InjectView(R.id.configuracion_msg_centro) PTextView msg_centro;
    @InjectView(R.id.configuracion_msg_grupo) PTextView msg_grupo;
    @InjectView(R.id.configuracion_switch_notifications) Switch switch_notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setContentView(R.layout.activity_configuracion);

        ButterKnife.inject(this);

        smfMapa=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.configuracion_mapa);

        try {
            gmMapa=smfMapa.getMap();
        } catch (Exception e) { }

        ic_margen_sup.post(new Runnable(){
            @Override
            public void run(){
                int valor=ic_margen_sup.getPHeight();
                logotipo.setPadding(valor,valor/2,valor,valor/2);
            }
        });

        borde_rojo_superior.post(new Runnable() {
            @Override
            public void run() {
                borde_rojo_inferior.getLayoutParams().height = borde_rojo_superior.getPHeight();
            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("CentrosRegionales");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> centrosRegionales, ParseException e) {
                if(e == null) {
                    otsLatLng = new ArrayList<>();
                    centrosRegionalesIdNombre = new HashMap<>();
                    for(ParseObject centroRegional:centrosRegionales){
                        ParseGeoPoint ubicacion=centroRegional.getParseGeoPoint("Ubicacion");
                        if(ubicacion!=null) {
                            LatLng latLng = new LatLng(ubicacion.getLatitude(), ubicacion.getLongitude());
                            otsLatLng.add(latLng);
                            gmMapa.addMarker(new MarkerOptions().position(latLng).title(centroRegional.getString("Nombre")));
                            //gmMapa.addMarker(new MarkerOptions().position(latLng).title(centroRegional.getString("Nombre")).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
                            centrosRegionalesIdNombre.put(centroRegional.getString("Nombre"), centroRegional.getObjectId());
                        }
                    }
                    gmMapa.getUiSettings().setZoomControlsEnabled(true);
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for(LatLng otLatLng:otsLatLng){
                        builder.include(otLatLng);
                    }
                    LatLngBounds bounds = builder.build();
                    gmMapa.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                    gmMapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            centroSeleccionado=centrosRegionalesIdNombre.get(marker.getTitle());
                            msg_centro.setTextColor(Color.BLACK);
                            return false;
                        }
                    });
                }
            }
        });

        cargarPreferencias();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @OnClick({R.id.configuracion_grupo_0n, R.id.configuracion_grupo_0p, R.id.configuracion_grupo_an, R.id.configuracion_grupo_ap,
            R.id.configuracion_grupo_bn, R.id.configuracion_grupo_bp, R.id.configuracion_grupo_abn, R.id.configuracion_grupo_abp})
    public void onGrupoClick(ImageView grupo){
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

//        grupo.setTextColor(getResources().getColor(R.color.rojo));
//        grupo.setTextSize(25);
//        msg_grupo.setTextColor(Color.BLACK);
        grupoSanguineoSeleccionado=grupo.getTag().toString();
    }

    @OnClick({R.id.configuracion_sexo_femenino, R.id.configuracion_sexo_masculino})
    public void onSexoClick(Button sexo){
        for(Button sexoAct:sexos){
            sexoAct.setTextColor(Color.BLACK);
            sexoAct.setTextSize(15);
        }
        sexo.setTextColor(getResources().getColor(R.color.rojo));
        sexo.setTextSize(25);
        sexoSeleccionado=sexo.getText().toString();
    }

    @OnClick(R.id.configuracion_buttonFloat)
    public void onGuardar(){
        SharedPreferences prefs = getSharedPreferences(Constantes.SP_KEY, Configuracion.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constantes.SP_CENTRO, centroSeleccionado);
        editor.putString(Constantes.SP_GRUPO, grupoSanguineoSeleccionado);
        editor.putString(Constantes.SP_SEXO, sexoSeleccionado);
        editor.putBoolean(Constantes.SP_NOTIFICACIONES, switch_notifications.isCheck());
        editor.commit();

        ParseInstallation pi = ParseInstallation.getCurrentInstallation();
        ArrayList<String> channels = new ArrayList<>();
        String channel = centroSeleccionado+"_"+grupoSanguineoSeleccionado;
        channel = channel.replace("+","POS").replace("-","NEG");
        if(switch_notifications.isCheck()) channels.add(channel);
        pi.put("channels", channels);
        pi.saveInBackground();

        finish();
    }

    private void cargarPreferencias(){
        SharedPreferences prefs = getSharedPreferences(Constantes.SP_KEY, Configuracion.MODE_PRIVATE);
        centroSeleccionado=prefs.getString(Constantes.SP_CENTRO, null);
        grupoSanguineoSeleccionado=prefs.getString(Constantes.SP_GRUPO, null);
        sexoSeleccionado=prefs.getString(Constantes.SP_SEXO, null);
        switch_notifications.setChecked(prefs.getBoolean(Constantes.SP_NOTIFICACIONES, false));
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
//                grupo.setTextColor(getResources().getColor(R.color.rojo));
//                grupo.setTextSize(25);
            }
        }
        for(Button sexo:sexos){
            if(sexo.getText().toString().equals(sexoSeleccionado)){
                sexo.setTextColor(getResources().getColor(R.color.rojo));
                sexo.setTextSize(25);
            }
        }
    }

}
