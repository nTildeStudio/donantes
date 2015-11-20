package com.ntilde.rest;

import com.ntilde.donantes.R;
import com.ntilde.utils.ParseConstantes;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by emanuel on 3/11/15.
 */
public class ParseManagerImpl<T extends ParseObject> implements ParseManager {

    @Override
    public void recuperar(final int type,ParseQuery query,final boolean fromLocalStorage, final ParseResponse callback) {

        //Recuperar de almacenamiento local
        if(fromLocalStorage) query.fromLocalDatastore();

        query.findInBackground(new FindCallback<T>() {
            @Override
            public void done(List<T> list, ParseException e) {

                if(e!=null){
                    callback.onError(crearMensajeError(type, false));
                    return;
                }


                callback.onSuccess(type,list);


            }
        });
    }

    @Override
    public void recuperarYAlmacenar(final int type,ParseQuery query, final ParseResponse callback) {


        query.findInBackground(new FindCallback<T>() {
            @Override
            public void done(List<T> list, ParseException e) {

                if(e!=null){
                    callback.onError(crearMensajeError(type, false));
                    return;
                }

                almacenar(type,list,callback);
            }
        });
    }

    @Override
    public void almacenar(final int type,final List objects, final ParseResponse callback) {
        ParseObject.pinAllInBackground(objects, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    callback.onError(crearMensajeError(type,true));
                    return;
                }

                callback.onSuccess(type, objects);
            }
        });
    }



    private int crearMensajeError(int type, boolean storing) {

        switch (type){
            case ParseConstantes.QUERY_CENTRO_REGIONAL:
                return storing ? R.string.error_saving_centro_regional : R.string.error_retrieving_centro_regional;

            case ParseConstantes.QUERY_CENTROS_REGIONALES:
                return storing ? R.string.error_saving_centros_regionales : R.string.error_retrieving_centros_regionales;

            case ParseConstantes.QUERY_PUNTO_DONACION:
                return storing ? R.string.error_saving_puntos_donacion : R.string.error_retrieving_puntosdonacion;

            case ParseConstantes.QUERY_ULTIMA_ACTUALIZACION:
                return storing ? R.string.error_saving_last_updated_date : R.string.error_retrieving_last_updated_date;

            default:
                return storing ? R.string.error_saving_data : R.string.error_retrieving_data;
        }

    }

}
