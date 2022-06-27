package com.jd.application;

import com.jd.application.enums.PointActionType;
import com.jd.application.enums.PointStatus;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Jaedoo Lee
 */
@Getter
@Builder
public class PointHistoryEvent {

    private final String userId;
    private final PointStatus status;
    private final PointActionType actionType;

    public static PointHistoryEvent of(String userId, PointStatus status, PointActionType actionType) {
        return PointHistoryEvent.builder()
                                .userId(userId)
                                .status(status)
                                .actionType(actionType)
                                .build();
    }

}
