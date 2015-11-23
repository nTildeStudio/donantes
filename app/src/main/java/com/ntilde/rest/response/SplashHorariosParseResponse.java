package com.ntilde.rest.response;

import android.util.Log;

import com.ntilde.donantes.SplashScreen;
import com.ntilde.modelo.HorariosDonacion;
import com.ntilde.modelo.PuntosDonacion;

import java.util.List;

/**
 * Created by emanuel on 23/11/15.
 */
public class SplashHorariosParseResponse implements ParseResponse<HorariosDonacion>{

    private static final String TAG = SplashHorariosParseResponse.class.getName();

    private int[] counter;
    private SplashScreen activity;
    private PuntosDonacion puntoDonacion;
    private int puntosDonacionSize;

    public SplashHorariosParseResponse(SplashScreen activity ,int[] counter, PuntosDonacion puntoDeDonacion, int size){
        this.activity = activity;
        this.counter = counter;
        this.puntoDonacion = puntoDeDonacion;
        this.puntosDonacionSize = size;
    }

    @Override
    public void onSuccess(int type, List<HorariosDonacion> result) {
        isLastValue();
    }

    @Override
    public void onError(int type, int message) {
        Log.e(TAG, "Error descargando horario del punto: " + puntoDonacion.getObjectId());
        isLastValue();
    }

    @Override
    public void onLocalError(int type, int message) {
        //Do nothing here
    }

    private void isLastValue(){
        counter[0]++;
        boolean ultimoPuntoDonacion = counter[0] == puntosDonacionSize -1;
        if(ultimoPuntoDonacion){
            activity.setDownloadFinished(true);
            activity.saveNewDate(activity.getFechaUltimaActualizacion());
            activity.goToNextActivity();
        }
    }
}
