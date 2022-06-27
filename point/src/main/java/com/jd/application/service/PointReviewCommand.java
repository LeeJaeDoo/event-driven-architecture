package com.jd.application.service;

import com.jd.adapter.in.model.PointReviewEvent;
import com.jd.application.PointException;
import com.jd.application.PointHistoryEvent;
import com.jd.application.enums.PointActionType;
import com.jd.application.enums.PointStatus;
import com.jd.application.port.in.PointReviewUseCase;
import com.jd.application.port.out.PointReviewRepository;
import com.jd.domain.PointReview;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;

/**
 * @author Jaedoo Lee
 */
@Service
@RequiredArgsConstructor
public class PointReviewCommand implements PointReviewUseCase {

    private final PointReviewRepository pointReviewRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void save(PointReviewEvent event) {

        if (pointReviewRepository.existsByPlaceIdAndUserId(event.getPlaceId(), event.getUserId())) {
            throw new PointException("중복 등록 불가");
        }

        PointReview pointReview = PointReview.create(event);
        if (!pointReviewRepository.existsByPlaceId(event.getPlaceId())) {
            pointReview.initCreate();
            eventPublisher.publishEvent(PointHistoryEvent.of(event.getUserId(), PointStatus.SAVE, PointActionType.INIT));
        }

        if (!ObjectUtils.isEmpty(event.getContent())) {
            eventPublisher.publishEvent(PointHistoryEvent.of(event.getUserId(), PointStatus.SAVE, PointActionType.CONTENT));
        }

        if (!ObjectUtils.isEmpty(event.getAttachedPhotoIds())) {
            eventPublisher.publishEvent(PointHistoryEvent.of(event.getUserId(), PointStatus.SAVE, PointActionType.PHOTO));
        }

        pointReviewRepository.save(pointReview);
    }

    @Override
    @Transactional
    public void modification(PointReviewEvent event) {
        PointReview pointReview = pointReviewRepository.findById(event.getReviewId()).orElseThrow(() -> new PointException("존재하지 않는 리뷰"));

        if (pointReview.isHasPhoto() && ObjectUtils.isEmpty(event.getAttachedPhotoIds())) {
            eventPublisher.publishEvent(PointHistoryEvent.of(event.getUserId(), PointStatus.DEDUCTION, PointActionType.PHOTO));
        }
        if (pointReview.isHasContent() && ObjectUtils.isEmpty(event.getContent())) {
            eventPublisher.publishEvent(PointHistoryEvent.of(event.getUserId(), PointStatus.DEDUCTION, PointActionType.CONTENT));
        }
        if (!pointReview.isHasPhoto() && !ObjectUtils.isEmpty(event.getAttachedPhotoIds())) {
            eventPublisher.publishEvent(PointHistoryEvent.of(event.getUserId(), PointStatus.SAVE, PointActionType.PHOTO));
        }
        if (!pointReview.isHasContent() && !ObjectUtils.isEmpty(event.getContent())) {
            eventPublisher.publishEvent(PointHistoryEvent.of(event.getUserId(), PointStatus.SAVE, PointActionType.CONTENT));
        }
        pointReview.update(event);
    }

    @Override
    @Transactional
    public void deduction(PointReviewEvent event) {
        PointReview pointReview = pointReviewRepository.findById(event.getReviewId()).orElseThrow(() -> new PointException("존재하지 않는 리뷰"));

        if (pointReview.isInit()) {
            eventPublisher.publishEvent(PointHistoryEvent.of(event.getUserId(), PointStatus.DEDUCTION, PointActionType.INIT));
        }
        if (pointReview.isHasContent()) {
            eventPublisher.publishEvent(PointHistoryEvent.of(event.getUserId(), PointStatus.DEDUCTION, PointActionType.CONTENT));
        }
        if (pointReview.isHasPhoto()) {
            eventPublisher.publishEvent(PointHistoryEvent.of(event.getUserId(), PointStatus.DEDUCTION, PointActionType.PHOTO));
        }

        pointReviewRepository.deleteById(pointReview.getId());
    }
}
