package com.ntilde.rest.response;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by emanuel on 3/11/15.
 */
public interface ParseResponse<T extends ParseObject> {

    void onSuccess(int type, List<T> result);
    void onError(int type, int message);
    void onLocalError(int type, int message);
}
