package com.ntilde.domain;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by 3003012 on 24/09/2015.
 */
@ParseClassName("HorariosDeDonacion")
public class HorariosDonacion extends ParseObject{

    public String getComentario(){
        return getString("Comentario");
    }

    public Date getFechaFin(){
        return getDate("FechaFin");
    }

    public Date getFechaInicio(){
        return getDate("FechaInicio");
    }

    public String getHorario(){
        return getString("Horario");
    }

    public PuntosDonacion getPuntoDonacion(){
        return (PuntosDonacion) get("PuntoDeDonacion");
    }

    public Date getFechaCreacion(){
        return getCreatedAt();
    }

    public Date getFechaModificacion(){
        return getUpdatedAt();
    }

}
