package com.ntilde.rest;

import android.support.annotation.Nullable;

import com.ntilde.exception.InvalidQueryException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by emanuel on 3/11/15.
 */
public interface ParseManager<T extends ParseObject> {

    void recuperar(int type,ParseQuery<T> query, boolean fromLocalStorage, ParseResponse callback) throws InvalidQueryException;
    void almacenar(int type,List<T> objects, ParseResponse callback);
    ParseQuery<T> crearQuery(int type, @Nullable String objectId);
}
