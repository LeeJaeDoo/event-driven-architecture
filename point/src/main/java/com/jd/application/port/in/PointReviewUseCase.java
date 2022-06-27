package com.jd.application.port.in;

import com.jd.adapter.in.model.PointReviewEvent;

/**
 * @author Jaedoo Lee
 */
public interface PointReviewUseCase {

    void save(PointReviewEvent request);
    void modification(PointReviewEvent request);
    void deduction(PointReviewEvent request);

}
