package com.jd.application.listener;

import com.jd.adapter.in.EventRequest;
import com.jd.application.broker.EventBroker;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * @author Jaedoo Lee
 */
@Service
@RequiredArgsConstructor
public class EventCustomListener {

    private final EventBroker eventBroker;

    @EventListener(EventRequest.class)
    public void subscribeEvent(EventRequest event) throws Exception {
        eventBroker.sendRequest(event);
    }

}
