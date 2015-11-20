package com.ntilde.donantes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;

import com.github.jorgecastillo.FillableLoader;
import com.github.jorgecastillo.listener.OnStateChangeListener;
import com.ntilde.exception.InvalidValueType;
import com.ntilde.modelo.CentroRegional;
import com.ntilde.modelo.HorariosDonacion;
import com.ntilde.modelo.PuntosDonacion;
import com.ntilde.modelo.UltimaActualizacion;
import com.ntilde.rest.ParseManager;
import com.ntilde.rest.ParseQueryFactory;
import com.ntilde.rest.ParseResponse;
import com.ntilde.utils.DateUtils;
import com.ntilde.utils.NetworkUtilities;
import com.ntilde.utils.ParseConstantes;
import com.ntilde.utils.VectorPath;
import com.parse.ParseQuery;

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
            recuperarUltimaActualizacion();

        }else{
            downloadFinished = true;
            goToNextActivity();

        }


    }

    public void recuperarUltimaActualizacion(){
        ParseQuery<UltimaActualizacion> query = ParseQueryFactory.ultimaActualizacionQuery(idCentroRegional);
        mManager.recuperar(ParseConstantes.QUERY_ULTIMA_ACTUALIZACION, query, false, this);
    }

    public void recuperarPuntosDonacion(){
        ParseQuery<PuntosDonacion> query = ParseQueryFactory.puntoDonacionQuery(idCentroRegional);
        mManager.recuperar(ParseConstantes.QUERY_PUNTO_DONACION, query, false, this);
    }

    public void recuperarCentroRegional(){
        ParseQuery<CentroRegional> query = ParseQueryFactory.centroRegionalQuery(idCentroRegional);
        mManager.recuperarYAlmacenar(ParseConstantes.QUERY_CENTRO_REGIONAL, query, this);
    }

    public void recuperarHorarios(List<PuntosDonacion> puntosDeDonacion){

        final int[] finishedCounter = {0};

        for(int i = 0; i < puntosDeDonacion.size(); i++){
            final PuntosDonacion puntoDeDonacion = puntosDeDonacion.get(i);

            ParseQuery<HorariosDonacion> query = ParseQueryFactory.horariosDonacionQuery(puntoDeDonacion.getObjectId());
            mManager.recuperarYAlmacenar(ParseConstantes.QUERY_CENTRO_REGIONAL, query, new ParseResponse() {
                @Override
                public void onSuccess(int type, List result) {
                    isLastValue();
                }

                @Override
                public void onError(int message) {
                    Log.e(TAG,"Error descargando horario del punto: " + puntoDeDonacion.getObjectId());
                    isLastValue();
                }

                private void isLastValue(){
                    finishedCounter[0]++;
                    boolean ultimoPuntoDonacion = finishedCounter[0] == puntosDeDonacion.size()-1;
                    if(ultimoPuntoDonacion){
                        downloadFinished = true;
                        saveNewDate(fechaUltimaActualizacion);
                        goToNextActivity();
                    }
                }
            });

        }
    }

    @Override
    public void onSuccess(int type, List result) {

        switch (type){

            case ParseConstantes.QUERY_CENTRO_REGIONAL:
                recuperarPuntosDonacion();
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

    public void onSaveInPreferences(Date newDate) {

        if(needUpdate(newDate)){
            fechaUltimaActualizacion = newDate;
            recuperarCentroRegional();

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




    @Override
    public void onError(int message) {
        //TODO mostrar dialogo error
        Log.e(TAG,"Ocurrió un error: " + message);
        cannotUpdate();
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

}
