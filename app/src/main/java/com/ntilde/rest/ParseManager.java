package com.ntilde.rest;

import android.util.Log;

import com.ntilde.donantes.DonantesApplication;
import com.ntilde.donantes.R;
import com.ntilde.modelo.CentroRegional;
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
                                    Log.e(TAG, "Error almacenando los datos en local " + e.toString());
                                    mCallback.onError(DonantesApplication.getInstance().getString(R.string.error_retrieving_data));
                                    return;

                                }
                                //Almacenamiento correcto de los datos
                                Log.i(TAG, "Respuesta correcta");
                                mCallback.onSuccess();

                            }
                        }
                    });
                } else {
                    //Ocurrió un error
                    Log.e(TAG, "Error recuperando los centros regionales " + e.toString());
                    if (mCallback != null)
                        mCallback.onError(DonantesApplication.getInstance().getString(R.string.error_retrieving_data));
                }
            }
        });
    }

    /**
     * Recupera toda la información asociada a un centro regional y la actualiza dentro de la
     * base de datos
     * @param objectId
     */
    public void recuperarInfoCentroRegional(String objectId){
        ParseQuery<CentroRegional> query = ParseQuery.getQuery("CentrosRegionales");
        query.getInBackground(objectId, new GetCallback<CentroRegional>() {
            @Override
            public void done(CentroRegional centroRegional, ParseException e) {
                if(mCallback != null){
                    if(e == null) mCallback.onError(DonantesApplication.getInstance().getString(R.string.error_retrieving_centro_regional));
                }

            }
        });
    }

    /**
     * Recuperar la fecha de última modificacion de toda la información asociada a un centro regional
     * @param centroRegionalId
     * @param fechaUltActualizacion
     */
    public void recuperarUltimaActualizacion(String centroRegionalId, final Date fechaUltActualizacion){
        ParseQuery<UltimaActualizacion> query = ParseQuery.getQuery("UltimaActualizacion");

        ParseObject object = ParseObject.createWithoutData(CentroRegional.class, centroRegionalId);
        query.whereEqualTo("CentroRegional", object);

        query.findInBackground(new FindCallback<UltimaActualizacion>() {
            @Override
            public void done(List<UltimaActualizacion> list, ParseException e) {
                if(mCallback != null){
                    if (e == null){
                        if(list.isEmpty()) mCallback.onSuccess(); //No se encontró dicho valor
                        
                        Date fechaUltActualizacionServidor = list.get(0).getUltimaActualizacion();
                        if(fechaUltActualizacion == null ||
                                fechaUltActualizacion.getTime() < fechaUltActualizacionServidor.getTime()){
                            mCallback.onSaveInPreferences(fechaUltActualizacionServidor);

                        }else{
                            mCallback.onSuccess();
                        }

                    }else{
                        mCallback.onError(DonantesApplication.getInstance().getString(R.string.error_date_updated));
                    }

                }
            }
        });
    }

}
