package com.ntilde.donantes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ntilde.donantes.FirstConfig;
import com.ntilde.donantes.R;
import com.ntilde.donantes.models.CentroRegional;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julio on 30/09/2015.
 */
public class FirstConfigStep1 extends Fragment implements OnMapReadyCallback {

    GoogleMap gmMapa;
    List<LatLng> otsLatLng;
    TextView tvCentro;
    FirstConfig mActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mActivity = (FirstConfig) getActivity();
        View view = inflater.inflate(R.layout.first_config_step1, container, false);
        loadUI(view);
        return view;
    }

    private void loadUI(View view){

        tvCentro = (TextView) view.findViewById(R.id.first_config_step1_centro);
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.first_config_step1_map)).getMapAsync(this);

    }

    private void getMarkers(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CentrosRegionales");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> centrosRegionales, ParseException e) {
                if(e == null) {
                    otsLatLng = new ArrayList<>();
                    for(ParseObject centroRegional:centrosRegionales){
                        ParseGeoPoint ubicacion=centroRegional.getParseGeoPoint("Ubicacion");
                        LatLng latLng=new LatLng(ubicacion.getLatitude(),ubicacion.getLongitude());
                        otsLatLng.add(latLng);
                        gmMapa.addMarker(new MarkerOptions().position(latLng).title(centroRegional.getString("Nombre")));
                        MarkerOptions a=new MarkerOptions();
                        mActivity.mCentrosRegionales.add(new CentroRegional(centroRegional.getObjectId(),
                                                                            centroRegional.getString("Nombre"),
                                                                            centroRegional.getParseFile("ImagenCfg1"),
                                                                            centroRegional.getParseFile("ImagenCfg2"),
                                                                            centroRegional.getInt("ImagenCfg1Radio"),
                                                                            centroRegional.getInt("ImagenCfg2Radio")));
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
                            mActivity.selectCentroRegional(marker.getTitle());
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmMapa = googleMap;
        gmMapa.getUiSettings().setZoomControlsEnabled(false);
        getMarkers();
    }
}
