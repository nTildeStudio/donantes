package com.ntilde.rest;

import android.support.annotation.Nullable;

import com.ntilde.donantes.R;
import com.ntilde.exception.InvalidQueryException;
import com.ntilde.modelo.CentroRegional;
import com.ntilde.modelo.PuntosDonacion;
import com.ntilde.modelo.UltimaActualizacion;
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
    public void recuperar(final int type,ParseQuery query,final boolean fromLocalStorage, final ParseResponse callback) throws InvalidQueryException {

        //Lanzar error si la query es inválida
        if(query == null) throw new InvalidQueryException("Query inválida");

        //Recuperar de almacenamiento local
        if(fromLocalStorage) query.fromLocalDatastore();

        query.findInBackground(new FindCallback<T>() {
            @Override
            public void done(List<T> list, ParseException e) {

                if(e!=null){
                    callback.onError(crearMensajeError(type,false));
                    return;
                }


                if(!fromLocalStorage){
                    almacenar(type,list,callback);

                }else{
                    callback.onSuccess(list);

                }
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

                callback.onSuccess(objects);
            }
        });
    }

    @Override
    public ParseQuery crearQuery(int type, @Nullable String objectId) {
        switch (type){

            case ParseConstantes.QUERY_CENTRO_REGIONAL:
                ParseQuery<CentroRegional> centroRegionalQuery = ParseQuery.getQuery("CentrosRegionales");
                centroRegionalQuery.whereEqualTo("objectId", objectId);
                return centroRegionalQuery;

            case ParseConstantes.QUERY_CENTROS_REGIONALES:
                ParseQuery<CentroRegional> centrosRegionalesQuery = ParseQuery.getQuery("CentrosRegionales");
                return centrosRegionalesQuery;

            case ParseConstantes.QUERY_PUNTO_DONACION:
                ParseQuery<PuntosDonacion> puntosDonacionQuery = ParseQuery.getQuery("PuntosDeDonacion");
                ParseObject pCentroRegional = ParseObject.createWithoutData(CentroRegional.class, objectId);
                puntosDonacionQuery.whereEqualTo("CentroRegional", pCentroRegional);
                return puntosDonacionQuery;

            case ParseConstantes.QUERY_ULTIMA_ACTUALIZACION:
                ParseQuery<UltimaActualizacion> ultimaActualizacionQuery = ParseQuery.getQuery("UltimaActualizacion");
                ParseObject object = ParseObject.createWithoutData(CentroRegional.class, objectId);
                ultimaActualizacionQuery.whereEqualTo("CentroRegional", object);
                return ultimaActualizacionQuery;
        }

        //No debería nunca estar aquí
        return null;
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
