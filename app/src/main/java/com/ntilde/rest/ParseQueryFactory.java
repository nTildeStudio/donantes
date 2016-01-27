package com.ntilde.rest;

import com.ntilde.domain.CentroRegional;
import com.ntilde.domain.HorariosDonacion;
import com.ntilde.domain.PuntosDonacion;
import com.ntilde.domain.UltimaActualizacion;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by emanuel on 20/11/15.
 */
public class ParseQueryFactory {

    public static ParseQuery<CentroRegional> centrosRegionalesQuery(){
        ParseQuery<CentroRegional> centrosRegionalesQuery = ParseQuery.getQuery("CentrosRegionales");
        return centrosRegionalesQuery;
    }

    public static ParseQuery<CentroRegional> centroRegionalQuery(String objectId){
        ParseQuery<CentroRegional> centroRegionalQuery = ParseQuery.getQuery("CentrosRegionales");
        centroRegionalQuery.whereEqualTo("objectId", objectId);
        return centroRegionalQuery;
    }

    public static ParseQuery<PuntosDonacion> puntoDonacionQuery(String objectId){
        ParseQuery<PuntosDonacion> puntosDonacionQuery = ParseQuery.getQuery("PuntosDeDonacion");
        ParseObject pCentroRegional = ParseObject.createWithoutData(CentroRegional.class, objectId);
        puntosDonacionQuery.whereEqualTo("CentroRegional", pCentroRegional);
        return puntosDonacionQuery;
    }

    public static ParseQuery<PuntosDonacion> puntoDonacionQueryByID(String objectId){
        ParseQuery<PuntosDonacion> puntosDonacionQuery = ParseQuery.getQuery("PuntosDeDonacion");
        puntosDonacionQuery.whereEqualTo("objectId", objectId);
        return puntosDonacionQuery;
    }

    public static ParseQuery<UltimaActualizacion> ultimaActualizacionQuery(String objectId){
        ParseQuery<UltimaActualizacion> ultimaActualizacionQuery = ParseQuery.getQuery("UltimaActualizacion");
        ParseObject object = ParseObject.createWithoutData(CentroRegional.class, objectId);
        ultimaActualizacionQuery.whereEqualTo("CentroRegional", object);
        return ultimaActualizacionQuery;
    }

    public static ParseQuery<HorariosDonacion> horariosDonacionQuery(String objectId){
        ParseQuery<HorariosDonacion> horariosDonancionQuery = ParseQuery.getQuery("HorariosDeDonacion");
        ParseObject object = ParseObject.createWithoutData(PuntosDonacion.class, objectId);
        horariosDonancionQuery.whereEqualTo("PuntoDeDonacion", object);
        return horariosDonancionQuery;
    }


}
