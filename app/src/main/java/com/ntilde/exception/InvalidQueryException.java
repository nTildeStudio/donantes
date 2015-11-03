package com.ntilde.exception;

/**
 * Created by emanuel on 3/11/15.
 */
public class InvalidQueryException extends Exception {

    public InvalidQueryException(String detailMessage) {
        super(detailMessage);
    }
}
