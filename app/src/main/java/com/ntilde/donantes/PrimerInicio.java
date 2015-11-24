package com.ntilde.donantes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ntilde.exception.InvalidValueType;
import com.ntilde.modelo.CentroRegional;
import com.ntilde.percentagelayout.PLinearLayout;
import com.ntilde.percentagelayout.PTextView;
import com.ntilde.rest.ParseManager;
import com.ntilde.rest.response.ParseResponse;
import com.ntilde.utils.ParseConstantes;
import com.parse.ParseGeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class PrimerInicio extends ActionBarActivity implements ParseResponse {

    private String centroSeleccionado=null;
    private String grupoSanguineoSeleccionado=null;
    private Map<String,String> centrosRegionalesIdNombre;

    private ParseManager manager;

    SupportMapFragment smfMapa;
    GoogleMap gmMapa;
    List<LatLng> otsLatLng;

    private DonantesPreferences prefs = DonantesApplication.getInstance().getPrefrences();

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
        initBorders();
        initMap();
        initManager();
        executeQuery();

    }

    private void initBorders(){
        ic_margen_sup.post(() -> {
            int valor = ic_margen_sup.getPHeight();
            logotipo.setPadding(valor, valor / 2, valor, valor / 2);
        });

        borde_rojo_superior.post(() -> {
            borde_rojo_inferior.getLayoutParams().height = borde_rojo_superior.getPHeight();
        });
    }

    private void initMap(){

        try {
            smfMapa=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.primer_inicio_mapa);
            gmMapa=smfMapa.getMap();

        } catch (Exception e) { }
    }

    private void initManager(){
        manager = DonantesApplication.getInstance().getParseManager();
    }

    private void executeQuery(){
        manager.getCentrosRegionales(this);
    }



    @OnClick({R.id.primer_inicio_grupo_0n, R.id.primer_inicio_grupo_0p, R.id.primer_inicio_grupo_an, R.id.primer_inicio_grupo_ap,
            R.id.primer_inicio_grupo_bn, R.id.primer_inicio_grupo_bp, R.id.primer_inicio_grupo_abn, R.id.primer_inicio_grupo_abp})
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
        msg_grupo.setTextColor(Color.BLACK);
        grupoSanguineoSeleccionado=grupo.getTag().toString();
    }

    @OnClick(R.id.primer_inicio_boton_guardar)
    public void onGuardar(){
        boolean todoRelleno = isCentroSeleccionado() & isGrupoSeleccionado();
        if(todoRelleno) {
           recuperarYalmacenarCentro();
        }
    }

    public boolean isCentroSeleccionado(){
        if(centroSeleccionado !=null) return true;

        msg_centro.setTextColor(getResources().getColor(R.color.rojo));
        YoYo.with(Techniques.Shake).duration(1000).playOn(msg_centro);
        return false;
    }

    public boolean isGrupoSeleccionado(){
        if(grupoSanguineoSeleccionado !=null) return true;

        msg_grupo.setTextColor(getResources().getColor(R.color.rojo));
        YoYo.with(Techniques.Shake).duration(1000).playOn(msg_grupo);
        return false;
    }

    public void goToNextActivity(){
        try{
            saveInPreferences();

        }catch (InvalidValueType invalidValueType) {
            invalidValueType.printStackTrace();

        }finally {
            startActivity(new Intent(PrimerInicio.this, MenuPrincipal.class));
        }
    }

    private void saveInPreferences() throws InvalidValueType{
        prefs.put(Constantes.SP_CENTRO, centroSeleccionado);
        prefs.put(Constantes.SP_GRUPO, grupoSanguineoSeleccionado);
        prefs.commit();

    }

    private void recuperarYalmacenarCentro(){
        manager.getCentroRegional(centroSeleccionado, false, this);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onSuccess(int type, List result) {

        if(type == ParseConstantes.QUERY_CENTROS_REGIONALES){
            handleCentrosRegionales((List<CentroRegional>) result);
            return;
        }

        if(type == ParseConstantes.QUERY_CENTRO_REGIONAL){
            goToNextActivity();
            return;
        }
    }

    private void handleCentrosRegionales(List<CentroRegional> centros){
        generatePoints(centros);
        LatLngBounds bounds = buildBounds();
        gmMapa.getUiSettings().setZoomControlsEnabled(true);
        gmMapa.setOnMapLoadedCallback(()->
        {
            gmMapa.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        });
        gmMapa.setOnMarkerClickListener((marker) -> {
            centroSeleccionado = centrosRegionalesIdNombre.get(marker.getTitle());
            msg_centro.setTextColor(Color.BLACK);
            return false;
        });
    }

    private void generatePoints(List<CentroRegional> centros){
        otsLatLng = new ArrayList<>();
        centrosRegionalesIdNombre = new HashMap<>();

        for (CentroRegional centroRegional : centros) {
            ParseGeoPoint ubicacion = centroRegional.getLocalizacion();
            LatLng latLng = new LatLng(ubicacion.getLatitude(), ubicacion.getLongitude());
            otsLatLng.add(latLng);
            gmMapa.addMarker(new MarkerOptions().position(latLng).title(centroRegional.getNombre()));
            centrosRegionalesIdNombre.put(centroRegional.getNombre(), centroRegional.getObjectId());
        }

    }

    private LatLngBounds buildBounds(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng otLatLng : otsLatLng) {
            builder.include(otLatLng);
        }
        LatLngBounds bounds = builder.build();
        return bounds;
    }

    @Override
    public void onError(int type, int message) {
        //TODO manage error
    }

    @Override
    public void onLocalError(int type, int message) {
        //Do nothing here
    }

}
