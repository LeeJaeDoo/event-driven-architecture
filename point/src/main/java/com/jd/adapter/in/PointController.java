package com.jd.adapter.in;

import com.jd.adapter.in.model.PointTotalResponse;
import com.jd.application.port.in.PointHistoryQueryUseCase;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * @author Jaedoo Lee
 */
@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointController {

    private final PointHistoryQueryUseCase pointHistoryQueryUseCase;

    @GetMapping("/users/{userId}/total")
    public ResponseEntity<PointTotalResponse> getPointTotal(@PathVariable String userId) {

        return ResponseEntity.ok(pointHistoryQueryUseCase.getTotalPoint(userId));
    }

}
