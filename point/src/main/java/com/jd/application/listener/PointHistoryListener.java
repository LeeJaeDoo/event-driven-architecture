package com.jd.application.listener;

import com.jd.application.PointHistoryEvent;
import com.jd.application.port.in.PointHistoryCommandUseCase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

/**
 * @author Jaedoo Lee
 */
@Component
@RequiredArgsConstructor
public class PointHistoryListener  {

    private final PointHistoryCommandUseCase commandUseCase;

    @TransactionalEventListener(PointHistoryEvent.class)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void subscribePointEvent(PointHistoryEvent event) {
        switch (event.getStatus()) {
            case SAVE:
                commandUseCase.save(event);
                break;
            case DEDUCTION:
                commandUseCase.deduct(event);
                break;
        }
    }

}
