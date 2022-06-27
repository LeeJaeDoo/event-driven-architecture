package com.jd.application.port.out;

import com.jd.domain.PointHistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Jaedoo Lee
 */
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findByUserId(String userId);

}
