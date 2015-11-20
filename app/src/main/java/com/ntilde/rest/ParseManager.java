package com.ntilde.rest;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by emanuel on 3/11/15.
 */
public interface ParseManager<T extends ParseObject> {

    void recuperar(int type,ParseQuery<T> query, boolean fromLocalStorage, ParseResponse callback);
    void recuperarYAlmacenar(int type,ParseQuery<T> query, ParseResponse callback);
    void almacenar(int type,List<T> objects, ParseResponse callback);
}
