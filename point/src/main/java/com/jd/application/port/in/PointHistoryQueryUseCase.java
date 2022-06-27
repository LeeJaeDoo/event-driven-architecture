package com.jd.application.port.in;

import com.jd.adapter.in.model.PointTotalResponse;

/**
 * @author Jaedoo Lee
 */
public interface PointHistoryQueryUseCase {

    PointTotalResponse getTotalPoint(String userId);

}
