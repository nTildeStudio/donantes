package com.ntilde.donantes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;

import com.github.jorgecastillo.FillableLoader;
import com.github.jorgecastillo.listener.OnStateChangeListener;
import com.ntilde.exception.InvalidValueType;
import com.ntilde.modelo.PuntosDonacion;
import com.ntilde.modelo.UltimaActualizacion;
import com.ntilde.rest.ParseManager;
import com.ntilde.rest.response.ParseResponse;
import com.ntilde.rest.response.SplashHorariosParseResponse;
import com.ntilde.utils.DateUtils;
import com.ntilde.utils.NetworkUtilities;
import com.ntilde.utils.ParseConstantes;
import com.ntilde.utils.VectorPath;

import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashScreen extends ActionBarActivity implements ParseResponse{

    private static final String TAG = SplashScreen.class.getName();


    @InjectView(R.id.fillableLoader) FillableLoader fillableLoader;
    @InjectView(R.id.loaderRed) FillableLoader loaderRed;
    @InjectView(R.id.loaderGris) FillableLoader loaderGris;

    private boolean animationFinished = false;
    private boolean downloadFinished = false;


    private Date fechaUltimaActualizacion;
    private String idCentroRegional;
    private DonantesPreferences prefs = DonantesApplication.getInstance().getPrefrences();
    private ParseManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ButterKnife.inject(this);

        mManager = DonantesApplication.getInstance().getParseManager();

        fillableLoader.setSvgPath(VectorPath.svgPathBlack);
        loaderRed.setSvgPath(VectorPath.svgPathRed);
        loaderGris.setSvgPath(VectorPath.svgPathGris);

        fillableLoader.reset();
        fillableLoader.start();

        fillableLoader.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                if (state == 2) {
                    loaderRed.reset();
                    loaderRed.start();
                }
            }
        });

        loaderRed.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                if (state == 3) {
                    loaderGris.reset();
                    loaderGris.start();
                }
            }
        });

        loaderGris.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                if (state == 3) {
                    animationFinished = true;
                    goToNextActivity();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        downloadFinished = false;

        if(isCentroRegionalStored()){
            comprobarUltimaActualizacion();

        }else{
            downloadFinished = true;
            goToNextActivity();

        }

    }


    public boolean isCentroRegionalStored(){
        idCentroRegional = prefs.getIdCentroRegional();
        return !TextUtils.isEmpty(idCentroRegional);
    }

    public void comprobarUltimaActualizacion(){

        if(NetworkUtilities.hasNetworkConnection()){
            mManager.getUltimaActualizacion(idCentroRegional,this);

        }else{
            downloadFinished = true;
            goToNextActivity();

        }


    }

    public void recuperarHorarios(List<PuntosDonacion> puntosDeDonacion){

        final int[] finishedCounter = {0};

        for(int i = 0; i < puntosDeDonacion.size(); i++){

            final PuntosDonacion puntoDeDonacion = puntosDeDonacion.get(i);
            mManager.getHorarios(puntoDeDonacion.getObjectId(), false,
                    new SplashHorariosParseResponse(this, finishedCounter, puntoDeDonacion, puntosDeDonacion.size()));

        }
    }

    @Override
    public void onSuccess(int type, List result) {

        switch (type){

            case ParseConstantes.QUERY_CENTRO_REGIONAL:
                mManager.getPuntosDonacion(idCentroRegional,false,SplashScreen.this);
                return;

            case ParseConstantes.QUERY_PUNTO_DONACION:

                recuperarHorarios(result);
                return;

            case ParseConstantes.QUERY_ULTIMA_ACTUALIZACION:
                onSaveInPreferences(((UltimaActualizacion) result.get(0)).getUltimaActualizacion());
                return;

            default:
                downloadFinished = true;
                goToNextActivity();

        }



    }

    @Override
    public void onError(int type, int message) {
        //TODO mostrar dialogo error
        Log.e(TAG,"Ocurrió un error: " + message);
        cannotUpdate();
    }

    @Override
    public void onLocalError(int type, int message) {
        //Do nothing here
    }

    public void onSaveInPreferences(Date newDate) {

        if(needUpdate(newDate)){
            fechaUltimaActualizacion = newDate;
            mManager.getCentroRegional(idCentroRegional,false,SplashScreen.this);

        }else{
            downloadFinished = true;
            goToNextActivity();
        }

    }

    public boolean needUpdate(Date newDate){
        return fechaUltimaActualizacion == null || newDate.getTime() - fechaUltimaActualizacion.getTime() > 0;
    }



    public void saveNewDate(Date newDate){

        try {
            prefs.put(Constantes.SP_ULTIMA_ACTUALIZACION, DateUtils.convertDateToString(newDate));
            prefs.commit();

        } catch (InvalidValueType invalidValueType) {
            Log.e(TAG, invalidValueType.getMessage());
            cannotUpdate();

        }
    }

    public void cannotUpdate(){
        downloadFinished = true;
        goToMenuPrincipal();
    }



    public void goToNextActivity(){

        if(animationFinished && downloadFinished){
            selectNextActivity();

        }

    }

    public void selectNextActivity(){
        if(isCentroRegionalStored()){
            goToMenuPrincipal();

        }else{
            goToPrimerInicio();

        }
    }

    public void goToMenuPrincipal(){
        startActivity(new Intent(SplashScreen.this, MenuPrincipal.class));
    }

    public void goToPrimerInicio(){
        startActivity(new Intent(SplashScreen.this, PrimerInicio.class));
    }

    public void setDownloadFinished(boolean finished){
        downloadFinished = finished;
    }

    public Date getFechaUltimaActualizacion(){
        return fechaUltimaActualizacion;
    }

}
