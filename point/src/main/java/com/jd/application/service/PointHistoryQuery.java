package com.jd.application.service;

import com.jd.adapter.in.model.PointTotalResponse;
import com.jd.application.port.in.PointHistoryQueryUseCase;
import com.jd.application.port.out.PointHistoryRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * @author Jaedoo Lee
 */
@Service
@RequiredArgsConstructor
public class PointHistoryQuery implements PointHistoryQueryUseCase {

    private final PointHistoryRepository pointHistoryRepository;

    @Override
    public PointTotalResponse getTotalPoint(String userId) {

        return PointTotalResponse.of(pointHistoryRepository.findByUserId(userId));
    }
}
