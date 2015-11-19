package com.ntilde.exception;

/**
 * Created by Emanuel on 19/11/2015.
 */
public class NonStoredValue extends Exception {

    public NonStoredValue(String detailMessage) {
        super(detailMessage);
    }
}
