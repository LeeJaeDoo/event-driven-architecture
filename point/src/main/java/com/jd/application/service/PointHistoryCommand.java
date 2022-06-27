package com.jd.application.service;

import com.jd.application.PointHistoryEvent;
import com.jd.application.port.in.PointHistoryCommandUseCase;
import com.jd.application.port.out.PointHistoryRepository;
import com.jd.domain.PointHistory;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * @author Jaedoo Lee
 */
@Service
@RequiredArgsConstructor
public class PointHistoryCommand implements PointHistoryCommandUseCase {

    private final PointHistoryRepository pointHistoryRepository;

    @Override
    public void save(PointHistoryEvent event) {
        PointHistory pointHistory = PointHistory.create(event.getUserId(), event.getActionType());
        pointHistory.addPoint();
        pointHistoryRepository.save(pointHistory);
    }

    @Override
    public void deduct(PointHistoryEvent event) {
        PointHistory pointHistory = PointHistory.create(event.getUserId(), event.getActionType());
        pointHistory.minusPoint();
        pointHistoryRepository.save(pointHistory);
    }
}
