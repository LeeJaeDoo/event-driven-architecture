package com.jd.application.broker;

import com.jd.adapter.in.EventRequest;
import com.jd.application.Topic;
import com.jd.application.enums.EventType;
import com.jd.application.port.out.MessageBroker;
import com.jd.util.JacksonUtils;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;

/**
 * @author Jaedoo Lee
 */
@Service
@RequiredArgsConstructor
@ExtensionMethod(JacksonUtils.class)
public class EventBroker {

    private final MessageBroker messageBroker;

    public void sendRequest(EventRequest event) throws Exception {
        messageBroker.sendMessage(getTopic(event.getType()), event.toJson());
    }

    private Topic getTopic(EventType type) throws Exception {
        switch (type) {
            case REVIEW:
                return Topic.POINT_REVIEW;
        }

        throw new Exception("error");
    }

}
