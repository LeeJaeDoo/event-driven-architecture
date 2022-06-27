package com.jd.adapter.in.model;

import com.jd.domain.PointHistory;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Jaedoo Lee
 */
@Builder
@Getter
public class PointTotalResponse {

    private final long pointTotal;

    public static PointTotalResponse of(List<PointHistory> pointHistoryList) {
        return PointTotalResponse.builder()
                                 .pointTotal(pointHistoryList.stream().mapToLong(PointHistory::getPointAmount).sum())
                                 .build();
    }

}
