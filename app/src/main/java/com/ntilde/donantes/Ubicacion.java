package com.ntilde.donantes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ntilde.percentagelayout.PLinearLayout;
import com.parse.FindCallback;
import com.parse.GetCallback;
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
import butterknife.OnClick;


public class Ubicacion extends ActionBarActivity {

    SupportMapFragment smfMapa;
    GoogleMap gmMapa;
    List<LatLng> otsLatLng;

    private String puntoSeleccionado=null;
    private Map<String,String> centrosRegionalesIdNombre;

    @InjectView(R.id.iconos_margen_superior) PLinearLayout ic_margen_sup;
    @InjectView(R.id.ubicacion_logotipo) ImageView logotipo;
    @InjectView(R.id.ubicacion_borde_rojo_superior) PLinearLayout borde_rojo_superior;
    @InjectView(R.id.ubicacion_borde_rojo_inferior) LinearLayout borde_rojo_inferior;
    @InjectView(R.id.ubicacion_msg_seleccionar_punto) TextView msg_seleccionar_punto;
    @InjectView(R.id.ubicacion_informacion) Button btInformacion;
    @InjectView(R.id.ubicacion_como_llegar) Button btComoLlegar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        setContentView(R.layout.activity_ubicacion);

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

        borde_rojo_superior.post(new Runnable(){
            @Override
            public void run(){
                borde_rojo_inferior.getLayoutParams().height=borde_rojo_superior.getPHeight();
            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("CentrosRegionales");
        SharedPreferences prefs = getSharedPreferences(Constantes.SP_KEY, Ubicacion.MODE_PRIVATE);
        String centroSeleccionado = prefs.getString(Constantes.SP_CENTRO, "");
        query.getInBackground(centroSeleccionado, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("PuntosDeDonacion").whereEqualTo("CentroRegional",object);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> puntosDeDonacion, ParseException e) {
                            if(e == null) {
                                otsLatLng = new ArrayList<>();
                                centrosRegionalesIdNombre = new HashMap<>();
                                boolean puntosIncluidos=false;
                                for(ParseObject puntoDeDonacion:puntosDeDonacion){
                                    ParseGeoPoint ubicacion=puntoDeDonacion.getParseGeoPoint("Ubicacion");
                                    Log.e("Donantes", "Encontrado punto");
                                    if(ubicacion!=null) {
                                        Log.e("Donantes", "Encontrado punto");
                                        puntosIncluidos=true;
                                        LatLng latLng = new LatLng(ubicacion.getLatitude(), ubicacion.getLongitude());
                                        otsLatLng.add(latLng);
                                        gmMapa.addMarker(new MarkerOptions().position(latLng).title(puntoDeDonacion.getString("Nombre")));
                                        //gmMapa.addMarker(new MarkerOptions().position(latLng).title(puntoDeDonacion.getString("Nombre")).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
                                        centrosRegionalesIdNombre.put(puntoDeDonacion.getString("Nombre"), puntoDeDonacion.getObjectId());
                                    }
                                }
                                if(!puntosIncluidos){
                                    return;
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
                                        puntoSeleccionado=centrosRegionalesIdNombre.get(marker.getTitle());
                                        msg_seleccionar_punto.setVisibility(View.GONE);
                                        btComoLlegar.setVisibility(View.VISIBLE);
                                        btInformacion.setVisibility(View.VISIBLE);
                                        return false;
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.ubicacion_informacion,R.id.ubicacion_como_llegar})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ubicacion_como_llegar:
                break;
            case R.id.ubicacion_informacion:
                startActivity(new Intent(this, PuntoDeDonacion.class));
                break;
        }
    }
}
