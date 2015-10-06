package com.ntilde.donantes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ntilde.donantes.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 0011361 on 30/09/2015.
 */
public class FirstConfigStep1 extends Fragment {

    private String centroSeleccionado=null;
    private Map<String,String> centrosRegionalesIdNombre;

    GoogleMap gmMapa;
    List<LatLng> otsLatLng;
    TextView tvCentro;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.first_config_step1, container, false);
        loadUI(view);
        return view;
    }

    private void loadUI(View view){

        tvCentro = (TextView) view.findViewById(R.id.first_config_step1_centro);
        //ImageView background = (ImageView) view.findViewById(R.id.first_config_step1_background);
        //Picasso.with(getActivity()).load(R.drawable.donantes).transform(new PicassoTransformationBlur()).into(background);

        try {
            gmMapa = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.first_config_step1_map)).getMap();
            gmMapa.getUiSettings().setZoomControlsEnabled(false);
            getMarkers();
        } catch (Exception e) {
            Log.e("XXX", "Error al inicializar el mapa: " + e.getMessage());
        }
    }

    private void getMarkers(){
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
                            tvCentro.setText(marker.getTitle());
                            return false;
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "Se ha producido un error al obtener los centros regionales", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getCentroSeleccionado(){
        return centroSeleccionado;
    }
}
