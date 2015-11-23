package com.ntilde.rest;

import com.ntilde.modelo.CentroRegional;
import com.ntilde.modelo.HorariosDonacion;
import com.ntilde.modelo.PuntosDonacion;
import com.ntilde.modelo.UltimaActualizacion;
import com.ntilde.rest.callbacks.AlmacenarCallback;
import com.ntilde.rest.callbacks.RecuperarCallback;
import com.ntilde.rest.callbacks.RecuperarYAlmacenarCallback;
import com.ntilde.rest.response.ParseResponse;
import com.ntilde.utils.ParseConstantes;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by emanuel on 3/11/15.
 */
public class ParseManagerImpl<T extends ParseObject> implements ParseManager {


    @Override
    public void getCentrosRegionales(ParseResponse callback){
        ParseQuery<CentroRegional> centrosRegionales = ParseQueryFactory.centrosRegionalesQuery();
        recuperar(ParseConstantes.QUERY_CENTROS_REGIONALES,centrosRegionales,false,callback);
    }

    @Override
    public void getCentroRegional(String centroRegionalId, boolean fromLocal, ParseResponse callback) {
        ParseQuery<CentroRegional> centroRegional = ParseQueryFactory.centroRegionalQuery(centroRegionalId);

        if(fromLocal){
            recuperar(ParseConstantes.QUERY_CENTROS_REGIONALES,centroRegional,fromLocal,callback);

        }else{
            recuperarYAlmacenar(ParseConstantes.QUERY_CENTROS_REGIONALES,centroRegional,callback);

        }
    }

    @Override
    public void getUltimaActualizacion(String centroRegionalId, ParseResponse callback) {
        ParseQuery<UltimaActualizacion> ultimaActualizacion = ParseQueryFactory.ultimaActualizacionQuery(centroRegionalId);
        recuperar(ParseConstantes.QUERY_ULTIMA_ACTUALIZACION,ultimaActualizacion,false,callback);
    }

    @Override
    public void getPuntosDonacion(String centroRegionalId, boolean fromLocal, ParseResponse callback) {
        ParseQuery<PuntosDonacion> puntosDonacion = ParseQueryFactory.puntoDonacionQuery(centroRegionalId);

        if(fromLocal){
            recuperar(ParseConstantes.QUERY_PUNTO_DONACION,puntosDonacion,fromLocal,callback);

        }else{
            recuperarYAlmacenar(ParseConstantes.QUERY_PUNTO_DONACION,puntosDonacion,callback);

        }
    }

    @Override
    public void getHorarios(String puntoDonacionId, boolean fromLocal, ParseResponse callback) {
        ParseQuery<HorariosDonacion> horarios = ParseQueryFactory.horariosDonacionQuery(puntoDonacionId);

        if(fromLocal){
            recuperar(ParseConstantes.QUERY_HORARIOS_DONACION,horarios,fromLocal,callback);

        }else{
            recuperarYAlmacenar(ParseConstantes.QUERY_HORARIOS_DONACION,horarios,callback);

        }
    }


    private void recuperar(final int type,ParseQuery query,final boolean fromLocalStorage, final ParseResponse callback) {
        //Recuperar de almacenamiento locals
        if(fromLocalStorage) query.fromLocalDatastore();
        query.findInBackground(new RecuperarCallback(callback, type));
    }


    private void recuperarYAlmacenar(final int type,ParseQuery query, final ParseResponse callback) {
        query.findInBackground(new RecuperarYAlmacenarCallback(this,type,false,callback));
    }

    @Override
    public void almacenar(final int type,final List objects, final ParseResponse callback) {
        ParseObject.pinAllInBackground(objects, new AlmacenarCallback(callback, type, objects));
    }

}
