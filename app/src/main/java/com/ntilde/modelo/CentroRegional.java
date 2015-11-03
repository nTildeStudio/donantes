package com.ntilde.modelo;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by 3003012 on 24/09/2015.
 */
@ParseClassName("CentrosRegionales")
public class CentroRegional extends ParseObject {

    public String getDescripcion(){
        return getString("Descripcion");
    }

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

    public String getWebUrl(){
        return getString("Web");
    }

    public Date getFechaCreacion(){
        return getCreatedAt();
    }

    public Date getFechaModificacion(){
        return getUpdatedAt();
    }

}
