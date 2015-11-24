package com.ntilde.rest.callbacks;

import com.ntilde.rest.ParseManager;
import com.ntilde.rest.response.ParseError;
import com.ntilde.rest.response.ParseResponse;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by emanuel on 23/11/15.
 */
public class RecuperarYAlmacenarCallback implements FindCallback<ParseObject> {

    private ParseManager manager;
    private int type;
    private ParseResponse response;
    private boolean fromLocal;

    public RecuperarYAlmacenarCallback(ParseManager manager, int type,boolean fromLocal, ParseResponse response) {
        this.manager = manager;
        this.type = type;
        this.response = response;
        this.fromLocal = fromLocal;
    }

    @Override
    public void done(List<ParseObject> list, ParseException e) {
        if(e!=null){
            if(fromLocal){
                response.onLocalError(type,ParseError.crearMensajeError(type,false));

            }else{
                response.onError(type,ParseError.crearMensajeError(type, false));

            }
            return;
        }

        manager.almacenar(type,list,response);
    }
}
