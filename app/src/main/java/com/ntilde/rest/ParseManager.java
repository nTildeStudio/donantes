package com.ntilde.rest;

import android.util.Log;

import com.ntilde.donantes.DonantesApplication;
import com.ntilde.donantes.R;
import com.ntilde.modelo.CentroRegional;
import com.ntilde.modelo.PuntosDonacion;
import com.ntilde.modelo.UltimaActualizacion;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

/**
 * Created by 3003012 on 24/09/2015.
 */
public class ParseManager {

    private static final String TAG = ParseManager.class.getName();

    public interface  FinishedCallback{
        void onSuccess();
        void onError(String cause);
        void onSaveInPreferences(Date newDate);
    }

    private FinishedCallback mCallback;

    public void setFinishedCallback(FinishedCallback callback){
        mCallback = callback;
    }


    /**
     * Recupera todos los centros regionales base de datos externa de parse
     */
    public void recuperarCentrosRegionales(boolean local){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CentrosRegionales");

        if(local) query.fromLocalDatastore();

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> centrosRegionales, ParseException e) {
                if (e == null) {
                    //Almacenamos los datos en local
                    ParseObject.pinAllInBackground(centrosRegionales, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (mCallback != null) {
                                if (e != null) {//Error
                                    mCallback.onError(DonantesApplication.getInstance().getString(R.string.error_retrieving_data));
                                    return;

                                }
                                //Almacenamiento correcto de los datos
                                mCallback.onSuccess();

                            }
                        }
                    });
                } else {
                    //Ocurrió un error
                     if (mCallback != null)
                        mCallback.onError(DonantesApplication.getInstance().getString(R.string.error_retrieving_data));
                }
            }
        });
    }


    public void recuperarCentroRegional(String objectId, boolean local){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CentrosRegionales");
        query.whereEqualTo("objectId", objectId);

        if(local) query.fromLocalDatastore();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if( mCallback != null ){
                    if(e != null || list.isEmpty()){
                        mCallback.onError(DonantesApplication.getInstance().getString(R.string.centro_regional_not_found));
                        return;

                    }

                    ParseObject.pinAllInBackground(list, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if( e != null ){
                                mCallback.onError(DonantesApplication.getInstance().getString(R.string.centroreg_error_storing));
                                return;

                            }

                            mCallback.onSuccess();
                        }
                    });

                }
            }
        });
    }

    /**
     * Recupera los puntos de donacion asociados a un centro en concreto
     * @param objectId
     */
    public void recuperarPuntosDonancionesPorCentroRegional(String objectId){
        ParseQuery<PuntosDonacion> query = ParseQuery.getQuery("PuntosDeDonacion");
        ParseObject pCentroRegional = ParseObject.createWithoutData(CentroRegional.class, objectId);
        query.whereEqualTo("CentroRegional", pCentroRegional);
        query.findInBackground(new FindCallback<PuntosDonacion>() {
            @Override
            public void done(final List<PuntosDonacion> list, ParseException e) {
                if (mCallback != null) {
                    if (e != null) {
                        mCallback.onError(DonantesApplication.getInstance().getString(R.string.error_retrieving_puntosdonacion));
                        return;
                    }

                    //Query vacía
                    if(list.isEmpty()){
                        mCallback.onError(DonantesApplication.getInstance().getString(R.string.empty_query_puntosdonacion));
                        return;
                    }

                    //Actualizar todos los datos recuperados en local
                    ParseObject.pinAllInBackground(list, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            //Ocurrió un error
                            if(e != null){
                                mCallback.onError(DonantesApplication.getInstance().getString(R.string.error_saving_puntos_donacion));
                                return;
                            }
                            mCallback.onSuccess();
                        }
                    });

                }
            }
        });
    }

    /**
     * Recuperar la fecha de última modificacion de toda la información asociada a un centro regional
     * @param centroRegionalId
     *
     */
    public void recuperarUltimaActualizacion(String centroRegionalId){
        ParseQuery<UltimaActualizacion> query = ParseQuery.getQuery("UltimaActualizacion");

        ParseObject object = ParseObject.createWithoutData(CentroRegional.class, centroRegionalId);
        query.whereEqualTo("CentroRegional", object);

        query.findInBackground(new FindCallback<UltimaActualizacion>() {
            @Override
            public void done(List<UltimaActualizacion> list, ParseException e) {
                if(mCallback != null && e == null){
                    if(list.isEmpty()) mCallback.onSuccess(); //No se encontró dicho valor

                    Date fechaUltActualizacionServidor = list.get(0).getUltimaActualizacion();
                    mCallback.onSaveInPreferences(fechaUltActualizacionServidor);

                }else{
                    mCallback.onError(DonantesApplication.getInstance().getString(R.string.error_date_updated));

                }
            }
        });
    }

}
