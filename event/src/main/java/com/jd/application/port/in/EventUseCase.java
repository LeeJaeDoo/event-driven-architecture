package com.jd.application.port.in;

import com.jd.adapter.in.EventRequest;

/**
 * @author Jaedoo Lee
 */
public interface EventUseCase {

    void event(EventRequest request);

}
