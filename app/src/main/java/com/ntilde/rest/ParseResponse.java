package com.ntilde.rest;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by emanuel on 3/11/15.
 */
public interface ParseResponse<T extends ParseObject> {

    void onSuccess(int type, List<T> result);
    void onError(int message);
}
