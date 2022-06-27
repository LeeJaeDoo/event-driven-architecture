package com.jd.application.port.out;

import com.jd.application.Topic;

/**
 * @author Jaedoo Lee
 */
public interface MessageBroker {

    void sendMessage(Topic topic, String message);

}
