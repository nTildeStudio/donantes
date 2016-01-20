package com.ntilde.domain;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by 3003012 on 24/09/2015.
 */
@ParseClassName("UltimaActualizacion")
public class UltimaActualizacion extends ParseObject {

    public Date getUltimaActualizacion(){
        return getDate("ultimaActualizacion");
    }

    public CentroRegional getCentroRegional(){
        return (CentroRegional)get("CentroRegional");
    }
}
