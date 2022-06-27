package com.jd.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Jaedoo Lee
 */
@Getter
@RequiredArgsConstructor
public enum Topic {

    POINT_REVIEW("point_review"),
    ;

    private final String value;

}
