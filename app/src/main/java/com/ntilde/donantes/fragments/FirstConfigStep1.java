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
import com.google.android.gms.maps.model.MarkerOptions;
import com.ntilde.donantes.DonantesApplication;
import com.ntilde.donantes.FirstConfig;
import com.ntilde.donantes.R;
import com.ntilde.modelo.CentroRegional;
import com.ntilde.rest.ParseManager;
import com.ntilde.rest.response.ParseResponse;
import com.ntilde.utils.NetworkUtilities;
import com.ntilde.utils.dialogs.DialogUtils;
import com.parse.ParseAnalytics;
import com.parse.ParseGeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Julio on 30/09/2015.
 */
public class FirstConfigStep1 extends Fragment implements OnMapReadyCallback, ParseResponse {

    GoogleMap gmMapa;
    List<LatLng> otsLatLng;
    TextView tvCentro;
    FirstConfig mActivity;
    private ParseManager manager = DonantesApplication.getInstance().getParseManager();


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
        if(!NetworkUtilities.hasNetworkConnection()){
            DialogUtils.showNoNetDialog(getActivity());

        }else{
            manager.getCentrosRegionales(this);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmMapa = googleMap;
        gmMapa.getUiSettings().setZoomControlsEnabled(false);
        getMarkers();
    }

    @Override
    public void onSuccess(int type, List result) {
        List<CentroRegional> centrosRegionales =  result;

        otsLatLng = new ArrayList<>();
        for(CentroRegional centroRegional:centrosRegionales){
            ParseGeoPoint ubicacion=centroRegional.getLocalizacion();
            LatLng latLng=new LatLng(ubicacion.getLatitude(),ubicacion.getLongitude());
            otsLatLng.add(latLng);
            gmMapa.addMarker(new MarkerOptions().position(latLng).title(centroRegional.getNombre()));
            MarkerOptions a=new MarkerOptions();
            mActivity.mCentrosRegionales.add(centroRegional);
        }
        gmMapa.getUiSettings().setZoomControlsEnabled(true);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(LatLng otLatLng:otsLatLng){
            builder.include(otLatLng);
        }
        LatLngBounds bounds = builder.build();
        gmMapa.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        gmMapa.setOnMarkerClickListener(marker -> {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("configuracionInicial", "marker");
            parameters.put("markerName", marker.getTitle());
            ParseAnalytics.trackEventInBackground("click", parameters);

            mActivity.selectCentroRegional(marker.getTitle());
            tvCentro.setText(marker.getTitle());
            return false;
        });
    }

    @Override
    public void onError(int type, int message) {
        Toast.makeText(getActivity(), "Se ha producido un error al obtener los centros regionales", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocalError(int type, int message) {

    }
}
