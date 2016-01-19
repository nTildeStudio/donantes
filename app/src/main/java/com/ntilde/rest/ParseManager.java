package com.ntilde.rest;

import com.ntilde.rest.response.ParseResponse;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by emanuel on 3/11/15.
 */
public interface ParseManager<T extends ParseObject> {

    void almacenar(int type,List<T> objects, ParseResponse callback);

    void getCentrosRegionales(ParseResponse callback);
    void getCentroRegional(String centroRegionalId,boolean fromLocal, ParseResponse callback);
    void getUltimaActualizacion(String centroRegionalId, ParseResponse callback);
    void getPuntosDonacion(String centroRegionalId, boolean fromLocal, ParseResponse callback);
    void getPuntosDonacionByID(String puntoDonacionID, boolean fromLocal, ParseResponse callback);
    void getHorarios(String puntoDonacionId, boolean fromLocal,ParseResponse callback);

}
