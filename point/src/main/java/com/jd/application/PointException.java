package com.jd.application;

import lombok.Getter;

/**
 * @author Jaedoo Lee
 */
@Getter
public class PointException extends RuntimeException {

    private final String message;

    public PointException(String message) {
        this.message = message;
    }

}
