package com.nexta1.common.utils.jackson;

/**
 * @author valarchie
 */
public class JacksonException extends RuntimeException {

    public JacksonException(String message, Exception e) {
        super(message, e);
    }

}
