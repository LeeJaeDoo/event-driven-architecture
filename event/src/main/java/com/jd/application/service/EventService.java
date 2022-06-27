package com.jd.application.service;

import com.jd.adapter.in.EventRequest;
import com.jd.application.port.in.EventUseCase;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * @author Jaedoo Lee
 */
@Service
@RequiredArgsConstructor
public class EventService implements EventUseCase {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void event(EventRequest request) {
        eventPublisher.publishEvent(request);
    }
}
