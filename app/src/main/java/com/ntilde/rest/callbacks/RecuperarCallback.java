package com.ntilde.rest.callbacks;

import com.ntilde.rest.response.ParseError;
import com.ntilde.rest.response.ParseResponse;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by emanuel on 23/11/15.
 */
public class RecuperarCallback implements FindCallback{

    private ParseResponse response;
    private int type;

    public RecuperarCallback(ParseResponse callback, int type){
        this.response = callback;
        this.type = type;
    }

    @Override
    public void done(List list, ParseException e) {

        if(e != null){
            response.onLocalError(type, ParseError.crearMensajeError(type,false));
        }

        response.onSuccess(type, list);
    }
}
