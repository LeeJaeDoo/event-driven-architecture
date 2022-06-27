package com.jd.adapter.in.model;

import com.jd.application.enums.ActionType;
import com.jd.application.enums.EventType;

import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.Getter;

/**
 * @author Jaedoo Lee
 */
@Getter
public class PointReviewEvent {

    @NotNull
    private EventType event;
    @NotNull
    private ActionType action;
    @NotNull
    private String reviewId;
    private String content;
    private Set<String> attachedPhotoIds;
    @NotNull
    private String userId;
    @NotNull
    private String placeId;

}
