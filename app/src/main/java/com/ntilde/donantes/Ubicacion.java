package com.ntilde.donantes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.ntilde.percentagelayout.PLinearLayout;
import com.parse.ParseAnalytics;
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
    private Map<String,ParseObject> centrosRegionalesIdNombre;

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

        ic_margen_sup.post(() -> {
                int valor=ic_margen_sup.getPHeight();
                logotipo.setPadding(valor,valor/2,valor,valor/2);
            });

        borde_rojo_superior.post(() -> borde_rojo_inferior.getLayoutParams().height=borde_rojo_superior.getPHeight());

        //TODO cambiar por parse manager
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("CentrosRegionales");
        SharedPreferences prefs = getSharedPreferences(Constantes.SP_KEY, Ubicacion.MODE_PRIVATE);
        String centroSeleccionado = prefs.getString(Constantes.SP_CENTRO, "");
        query1.getInBackground(centroSeleccionado, (object , e) -> {
                if (e == null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("PuntosDeDonacion").whereEqualTo("CentroRegional",object);
                    query.findInBackground((puntosDeDonacion, e2) -> {
                            if(e2 == null) {
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
                                        centrosRegionalesIdNombre.put(puntoDeDonacion.getString("Nombre"), puntoDeDonacion);
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
                                gmMapa.setOnMarkerClickListener((marker) -> {
                                        puntoSeleccionado = centrosRegionalesIdNombre.get(marker.getTitle()).getString("Nombre");
                                        msg_seleccionar_punto.setVisibility(View.GONE);
                                        btComoLlegar.setVisibility(View.VISIBLE);
                                        btInformacion.setVisibility(View.VISIBLE);
                                        Map<String, String> parameters = new HashMap<>();
                                        parameters.put("dondeDonar", "marker");
                                        parameters.put("markerName", puntoSeleccionado);
                                        ParseAnalytics.trackEventInBackground("click", parameters);
                                        return false;
                                    });
                                gmMapa.setOnMapClickListener((latLng) -> {
                                        msg_seleccionar_punto.setVisibility(View.VISIBLE);
                                        btComoLlegar.setVisibility(View.GONE);
                                        btInformacion.setVisibility(View.GONE);
                                    });
                            }
                        });
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
        Map<String, String> parameters = new HashMap<>();
        ParseObject punto=(ParseObject)centrosRegionalesIdNombre.get(puntoSeleccionado);
        switch (view.getId()){
            case R.id.ubicacion_como_llegar:
                parameters.put("dondeDonar", "comoLlegar");
                ParseGeoPoint geopunto = punto.getParseGeoPoint("Ubicacion");
                Intent comoLlegar = new Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("http://maps.google.com/maps?saddr=&daddr=" + geopunto.getLatitude() + "," + geopunto.getLongitude()
                        ));
                startActivity(comoLlegar);
                break;
            case R.id.ubicacion_informacion:
                parameters.put("dondeDonar", "verInfo");
                Intent mostrarDetalles=new Intent(this, PuntoDeDonacion.class);
                mostrarDetalles.putExtra("puntoNombre",punto.getString("Nombre"));
                mostrarDetalles.putExtra("puntoId",punto.getObjectId());
                mostrarDetalles.putExtra("puntoTelefono",punto.getString("Telefono"));
                mostrarDetalles.putExtra("puntoDireccion",punto.getString("Direccion"));
                startActivity(mostrarDetalles);
                break;
        }

        parameters.put("markerName", puntoSeleccionado);
        ParseAnalytics.trackEventInBackground("click", parameters);
    }

    @Override
    public void onBackPressed() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("dondeDonar", "onBack");
        ParseAnalytics.trackEventInBackground("click", parameters);
        super.onBackPressed();
    }
}
