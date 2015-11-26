package com.ntilde.donantes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ntilde.modelo.PuntosDonacion;
import com.ntilde.percentagelayout.PLinearLayout;
import com.ntilde.rest.ParseManager;
import com.ntilde.rest.response.ParseResponse;
import com.ntilde.utils.ParseConstantes;
import com.parse.ParseAnalytics;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class Ubicacion extends ActionBarActivity implements ParseResponse{

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


    private ParseManager manager = DonantesApplication.getInstance().getParseManager();
    private DonantesPreferences preferences = DonantesApplication.getInstance().getPrefrences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setContentView(R.layout.activity_ubicacion);

        ButterKnife.inject(this);

        smfMapa=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.configuracion_mapa);

        try {
            gmMapa=smfMapa.getMap();
        } catch (Exception e) { }


        setMargins();
        manager.getPuntosDonacion(preferences.getIdCentroRegional(),true,this);

    }

    private void setMargins(){
        ic_margen_sup.post(() -> {
            int valor = ic_margen_sup.getPHeight();
            logotipo.setPadding(valor, valor / 2, valor, valor / 2);
        });

        borde_rojo_superior.post(() -> borde_rojo_inferior.getLayoutParams().height = borde_rojo_superior.getPHeight());
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.ubicacion_informacion,R.id.ubicacion_como_llegar})
    public void onClick(View view){
        Map<String, String> parameters = new HashMap<>();
        PuntosDonacion punto= (PuntosDonacion) centrosRegionalesIdNombre.get(puntoSeleccionado);
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
                startActivity(createIntent(punto));
                break;
        }

        parameters.put("markerName", puntoSeleccionado);
        ParseAnalytics.trackEventInBackground("click", parameters);
    }

    private Intent createIntent(PuntosDonacion punto){
        Intent mostrarDetalles=new Intent(this, PuntoDeDonacion.class);
        mostrarDetalles.putExtra("puntoNombre",punto.getNombre());
        mostrarDetalles.putExtra("puntoId",punto.getObjectId());
        mostrarDetalles.putExtra("puntoTelefono", punto.getTelefono());
        mostrarDetalles.putExtra("puntoDireccion", punto.getDireccion());
        return mostrarDetalles;
    }

    @Override
    public void onBackPressed() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("dondeDonar", "onBack");
        ParseAnalytics.trackEventInBackground("click", parameters);
        super.onBackPressed();
    }

    @Override
    public void onSuccess(int type, List result) {
        generatePoints((List<PuntosDonacion>)result);
        if(noPointsGenerated()) return;

        LatLngBounds bounds = buildBounds();

        gmMapa.getUiSettings().setZoomControlsEnabled(true);
        gmMapa.setOnMapLoadedCallback(()->
        {
            gmMapa.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        });
        gmMapa.setOnMarkerClickListener((marker) -> {
            manageMarkerClickListener(marker);
            return false;
        });
        gmMapa.setOnMapClickListener((latLng) -> {
            msg_seleccionar_punto.setVisibility(View.VISIBLE);
            btComoLlegar.setVisibility(View.GONE);
            btInformacion.setVisibility(View.GONE);
        });
    }


    private void generatePoints(List<PuntosDonacion> puntosDeDonacion){
        otsLatLng = new ArrayList<>();
        centrosRegionalesIdNombre = new HashMap<>();

        for(PuntosDonacion puntoDeDonacion : puntosDeDonacion){
            ParseGeoPoint ubicacion = puntoDeDonacion.getLocalizacion();

            if(ubicacion!=null) {
                LatLng latLng = new LatLng(ubicacion.getLatitude(), ubicacion.getLongitude());
                otsLatLng.add(latLng);
                gmMapa.addMarker(new MarkerOptions().position(latLng).title(puntoDeDonacion.getString("Nombre")));
                centrosRegionalesIdNombre.put(puntoDeDonacion.getString("Nombre"), puntoDeDonacion);
            }
        }

    }

    private boolean noPointsGenerated(){
      return otsLatLng.isEmpty();
    }

    private LatLngBounds buildBounds(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng otLatLng : otsLatLng) {
            builder.include(otLatLng);
        }
        LatLngBounds bounds = builder.build();
        return bounds;
    }

    private void manageMarkerClickListener(Marker marker){
        puntoSeleccionado = centrosRegionalesIdNombre.get(marker.getTitle()).getString("Nombre");
        msg_seleccionar_punto.setVisibility(View.GONE);
        btComoLlegar.setVisibility(View.VISIBLE);
        btInformacion.setVisibility(View.VISIBLE);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("dondeDonar", "marker");
        parameters.put("markerName", puntoSeleccionado);
        ParseAnalytics.trackEventInBackground("click", parameters);
    }

    @Override
    public void onError(int type, int message) {

        if(type == ParseConstantes.QUERY_PUNTO_DONACION){
            //TODO sustituir por snackbar
            Toast.makeText(this,"No se pudieron recuperar los puntos asociados a su centro",Toast.LENGTH_LONG).show();
            return;
        }
    }


    @Override
    public void onLocalError(int type, int message) {
        manager.getPuntosDonacion(preferences.getIdCentroRegional(),false,this);
    }
}
