package com.jd.application.port.out;

import com.jd.domain.PointReview;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Jaedoo Lee
 */
public interface PointReviewRepository extends JpaRepository<PointReview, String> {

    boolean existsByPlaceId(String placeId);

    boolean existsByPlaceIdAndUserId(String placeId, String userId);

}
