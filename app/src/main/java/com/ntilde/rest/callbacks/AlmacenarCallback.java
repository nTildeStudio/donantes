package com.ntilde.rest.callbacks;

import com.ntilde.rest.response.ParseError;
import com.ntilde.rest.response.ParseResponse;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by emanuel on 23/11/15.
 */
public class AlmacenarCallback implements SaveCallback{

    private ParseResponse callback;
    private int type;
    private List objects;

    public AlmacenarCallback(ParseResponse callback, int type, List objects) {
        this.callback = callback;
        this.type = type;
        this.objects = objects;
    }

    @Override
    public void done(ParseException e) {
        if(e != null){
            callback.onError(type, ParseError.crearMensajeError(type, true));
            return;
        }

        callback.onSuccess(type, objects);
    }
}
