package com.ntilde.donantes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class PrimerInicio extends ActionBarActivity {

    private String centroSeleccionado=null;
    private String grupoSanguineoSeleccionado=null;
    private Map<String,String> centrosRegionalesIdNombre;

    SupportMapFragment smfMapa;
    GoogleMap gmMapa;
    List<LatLng> otsLatLng;

    @InjectView(R.id.iconos_margen_superior) PLinearLayout ic_margen_sup;
    @InjectView(R.id.primer_inicio_logotipo)ImageView logotipo;
    @InjectView(R.id.primer_inicio_borde_rojo_superior) PLinearLayout borde_rojo_superior;
    @InjectView(R.id.primer_inicio_borde_rojo_inferior) LinearLayout borde_rojo_inferior;
    @InjectViews({R.id.primer_inicio_grupo_0n, R.id.primer_inicio_grupo_0p, R.id.primer_inicio_grupo_an, R.id.primer_inicio_grupo_ap,
                    R.id.primer_inicio_grupo_bn, R.id.primer_inicio_grupo_bp, R.id.primer_inicio_grupo_abn, R.id.primer_inicio_grupo_abp}) ImageView[] gruposSanguineos;
    @InjectView(R.id.primer_inicio_msg_centro) PTextView msg_centro;
    @InjectView(R.id.primer_inicio_msg_grupo) PTextView msg_grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer_inicio);

        ButterKnife.inject(this);

        smfMapa=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.primer_inicio_mapa);

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

        borde_rojo_superior.post(new Runnable(){
            @Override
            public void run(){
                borde_rojo_inferior.getLayoutParams().height=borde_rojo_superior.getPHeight();
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
                        LatLng latLng=new LatLng(ubicacion.getLatitude(),ubicacion.getLongitude());
                        otsLatLng.add(latLng);
                        MarkerOptions a=new MarkerOptions();
                        gmMapa.addMarker(new MarkerOptions().position(latLng).title(centroRegional.getString("Nombre")));
                        centrosRegionalesIdNombre.put(centroRegional.getString("Nombre"),centroRegional.getObjectId());
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
    }

    @OnClick({R.id.primer_inicio_grupo_0n, R.id.primer_inicio_grupo_0p, R.id.primer_inicio_grupo_an, R.id.primer_inicio_grupo_ap,
            R.id.primer_inicio_grupo_bn, R.id.primer_inicio_grupo_bp, R.id.primer_inicio_grupo_abn, R.id.primer_inicio_grupo_abp})
    public void onGrupoClick(ImageView grupo){
//        for(ImageView grupoSanguineo:gruposSanguineos){
//            grupoSanguineo.setTextColor(Color.BLACK);
//            grupoSanguineo.setTextSize(15);
//        }
//        grupo.setTextColor(getResources().getColor(R.color.rojo));
//        grupo.setTextSize(25);

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
        msg_grupo.setTextColor(Color.BLACK);
        grupoSanguineoSeleccionado=grupo.getTag().toString();
    }

    @OnClick(R.id.primer_inicio_boton_guardar)
    public void onGuardar(){
        boolean datosOk=true;
        if(centroSeleccionado==null){
            datosOk=false;
            msg_centro.setTextColor(getResources().getColor(R.color.rojo));
            YoYo.with(Techniques.Shake).duration(1000).playOn(msg_centro);
        }
        if(grupoSanguineoSeleccionado==null){
            datosOk=false;
            msg_grupo.setTextColor(getResources().getColor(R.color.rojo));
            YoYo.with(Techniques.Shake).duration(1000).playOn(msg_grupo);
        }
        if(datosOk) {
            SharedPreferences prefs = getSharedPreferences(Constantes.SP_KEY, PrimerInicio.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Constantes.SP_CENTRO, centroSeleccionado);
            editor.putString(Constantes.SP_GRUPO, grupoSanguineoSeleccionado);
            editor.commit();
            startActivity(new Intent(PrimerInicio.this, MenuPrincipal.class));
        }
    }

    @Override
    public void onBackPressed() {
    }
}
