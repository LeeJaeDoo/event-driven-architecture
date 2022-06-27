package com.jd.application.port.in;

import com.jd.application.PointHistoryEvent;

/**
 * @author Jaedoo Lee
 */
public interface PointHistoryCommandUseCase {

    void save(PointHistoryEvent event);
    void deduct(PointHistoryEvent event);

}
