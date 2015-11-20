package com.ntilde.modelo;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by 3003012 on 24/09/2015.
 */
@ParseClassName("PuntosDeDonacion")
public class PuntosDonacion extends ParseObject {

    public String getDireccion(){
        return getString("Direccion");
    }

    public String getNombre(){
        return getString("Nombre");
    }

    public String getTelefono(){
        return getString("Telefono");
    }

    public ParseGeoPoint getLocalizacion(){
        return getParseGeoPoint("Ubicacion");
    }

    public Date getFechaCreacion(){
        return getCreatedAt();
    }

    public Date getFechaModificacion(){
        return getUpdatedAt();
    }

    public CentroRegional getCentroRegional(){
        return (CentroRegional) get("CentroRegional");
    }

}
