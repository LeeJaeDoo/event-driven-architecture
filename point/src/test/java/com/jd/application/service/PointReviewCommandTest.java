package com.jd.application.service;

import com.jd.adapter.in.model.PointReviewEvent;
import com.jd.application.PointException;
import com.jd.application.PointHistoryEvent;
import com.jd.application.port.out.PointReviewRepository;
import com.jd.domain.PointReview;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Jaedoo Lee
 */
@ExtendWith(SpringExtension.class)
@Import(PointReviewCommand.class)
class PointReviewCommandTest {

    @SpyBean
    private PointReviewCommand pointReviewCommand;
    @MockBean
    private PointReviewRepository pointReviewRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Test
    @DisplayName("리뷰 등록 : 중복 예외")
    public void duplicatedReview() {
        //given
        PointReviewEvent event = mock(PointReviewEvent.class);
        given(pointReviewRepository.existsByPlaceIdAndUserId(any(), any())).willReturn(true);
        //when
        //then
        assertThatExceptionOfType(PointException.class)
            .isThrownBy(() -> pointReviewCommand.save(event)).withMessage("중복 등록 불가");
    }

    @Test
    @DisplayName("리뷰 등록 : 포인트 카운팅(INIT/CONTENT/PHOTO)")
    public void registerReviewPointCounting() {
        //given
        PointReviewEvent event = mock(PointReviewEvent.class);
        given(pointReviewRepository.existsByPlaceIdAndUserId(any(), any())).willReturn(false);
        given(pointReviewRepository.existsByPlaceId(any())).willReturn(false);
        given(event.getContent()).willReturn("좋아요!");
        given(event.getAttachedPhotoIds()).willReturn(Set.of("image1"));
        //when
        pointReviewCommand.save(event);
        //then
        verify(eventPublisher, times(3)).publishEvent(any(PointHistoryEvent.class));
        verify(pointReviewRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("리뷰 등록 : 포인트 카운팅(CONTENT/PHOTO)")
    public void registerReviewPointCounting1() {
        //given
        PointReviewEvent event = mock(PointReviewEvent.class);
        given(pointReviewRepository.existsByPlaceIdAndUserId(any(), any())).willReturn(false);
        given(pointReviewRepository.existsByPlaceId(any())).willReturn(true);
        given(event.getContent()).willReturn("좋아요!");
        given(event.getAttachedPhotoIds()).willReturn(Set.of("image1"));
        //when
        pointReviewCommand.save(event);
        //then
        verify(eventPublisher, times(2)).publishEvent(any(PointHistoryEvent.class));
        verify(pointReviewRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("리뷰 등록 : 포인트 카운팅(PHOTO)")
    public void registerReviewPointCounting2() {
        //given
        PointReviewEvent event = mock(PointReviewEvent.class);
        given(pointReviewRepository.existsByPlaceIdAndUserId(any(), any())).willReturn(false);
        given(pointReviewRepository.existsByPlaceId(any())).willReturn(true);
        given(event.getContent()).willReturn(null);
        given(event.getAttachedPhotoIds()).willReturn(Set.of("image1"));
        //when
        pointReviewCommand.save(event);
        //then
        verify(eventPublisher, times(1)).publishEvent(any(PointHistoryEvent.class));
        verify(pointReviewRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("리뷰 수정 : 존재하지 않는 리뷰 수정 예외")
    public void modifyReview() {
        //given
        PointReviewEvent event = mock(PointReviewEvent.class);
        given(event.getReviewId()).willReturn("123");
        given(pointReviewRepository.findById(any())).willReturn(Optional.empty());
        //when
        //then
        assertThatExceptionOfType(PointException.class)
            .isThrownBy(() -> pointReviewCommand.modification(event)).withMessage("존재하지 않는 리뷰");
    }

    @Test
    @DisplayName("리뷰 수정 : 기존 PHOTO/CONTENT 삭제")
    public void modifyReviewPointCounting() {
        //given
        PointReviewEvent event = mock(PointReviewEvent.class);
        PointReview pointReview = mock(PointReview.class);
        given(event.getReviewId()).willReturn("123");
        given(pointReviewRepository.findById(any())).willReturn(Optional.of(pointReview));
        given(pointReview.isHasPhoto()).willReturn(true);
        given(event.getAttachedPhotoIds()).willReturn(null);
        given(pointReview.isHasContent()).willReturn(true);
        given(event.getContent()).willReturn(null);
        //when
        pointReviewCommand.modification(event);
        //then
        verify(eventPublisher, times(2)).publishEvent(any(PointHistoryEvent.class));
        verify(pointReview, times(1)).update(any());
    }

    @Test
    @DisplayName("리뷰 수정 : 기존 PHOTO만 삭제")
    public void modifyReviewPointCounting1() {
        //given
        PointReviewEvent event = mock(PointReviewEvent.class);
        PointReview pointReview = mock(PointReview.class);
        given(event.getReviewId()).willReturn("123");
        given(pointReviewRepository.findById(any())).willReturn(Optional.of(pointReview));
        given(pointReview.isHasPhoto()).willReturn(true);
        given(event.getAttachedPhotoIds()).willReturn(null);
        given(pointReview.isHasContent()).willReturn(true);
        given(event.getContent()).willReturn("좋아요!");
        //when
        pointReviewCommand.modification(event);
        //then
        verify(eventPublisher, times(1)).publishEvent(any(PointHistoryEvent.class));
        verify(pointReview, times(1)).update(any());
    }

    @Test
    @DisplayName("리뷰 수정 : PHOTO만 추가")
    public void modifyReviewPointCounting2() {
        //given
        PointReviewEvent event = mock(PointReviewEvent.class);
        PointReview pointReview = mock(PointReview.class);
        given(event.getReviewId()).willReturn("123");
        given(pointReviewRepository.findById(any())).willReturn(Optional.of(pointReview));
        given(pointReview.isHasPhoto()).willReturn(false);
        given(event.getAttachedPhotoIds()).willReturn(Set.of("image1"));
        given(pointReview.isHasContent()).willReturn(true);
        given(event.getContent()).willReturn("좋아요!");
        //when
        pointReviewCommand.modification(event);
        //then
        verify(eventPublisher, times(1)).publishEvent(any(PointHistoryEvent.class));
        verify(pointReview, times(1)).update(any());
    }

    @Test
    @DisplayName("리뷰 수정 : CONTENT만 추가")
    public void modifyReviewPointCounting3() {
        //given
        PointReviewEvent event = mock(PointReviewEvent.class);
        PointReview pointReview = mock(PointReview.class);
        given(event.getReviewId()).willReturn("123");
        given(pointReviewRepository.findById(any())).willReturn(Optional.of(pointReview));
        given(pointReview.isHasPhoto()).willReturn(true);
        given(event.getAttachedPhotoIds()).willReturn(Set.of("image1"));
        given(pointReview.isHasContent()).willReturn(false);
        given(event.getContent()).willReturn("좋아요!");
        //when
        pointReviewCommand.modification(event);
        //then
        verify(eventPublisher, times(1)).publishEvent(any(PointHistoryEvent.class));
        verify(pointReview, times(1)).update(any());
    }

    @Test
    @DisplayName("리뷰 수정 : PHOTO/CONTENT 모두 추가")
    public void modifyReviewPointCounting4() {
        //given
        PointReviewEvent event = mock(PointReviewEvent.class);
        PointReview pointReview = mock(PointReview.class);
        given(event.getReviewId()).willReturn("123");
        given(pointReviewRepository.findById(any())).willReturn(Optional.of(pointReview));
        given(pointReview.isHasPhoto()).willReturn(false);
        given(event.getAttachedPhotoIds()).willReturn(Set.of("image1"));
        given(pointReview.isHasContent()).willReturn(false);
        given(event.getContent()).willReturn("좋아요!");
        //when
        pointReviewCommand.modification(event);
        //then
        verify(eventPublisher, times(2)).publishEvent(any(PointHistoryEvent.class));
        verify(pointReview, times(1)).update(any());
    }

    @Test
    @DisplayName("리뷰 삭제 : 존재하지 않는 리뷰 삭제 예외")
    public void deleteReview() {
        //given
        PointReviewEvent event = mock(PointReviewEvent.class);
        given(event.getReviewId()).willReturn("123");
        given(pointReviewRepository.findById(any())).willReturn(Optional.empty());
        //when
        //then
        assertThatExceptionOfType(PointException.class)
            .isThrownBy(() -> pointReviewCommand.deduction(event)).withMessage("존재하지 않는 리뷰");
    }

    @Test
    @DisplayName("리뷰 삭제 : INIT/PHOTO/CONTENT 모두 회수")
    public void deleteReviewPointCounting1() {
        //given
        PointReview pointReview = mock(PointReview.class);
        PointReviewEvent event = mock(PointReviewEvent.class);
        given(event.getReviewId()).willReturn("123");
        given(pointReviewRepository.findById(any())).willReturn(Optional.of(pointReview));
        given(pointReview.isInit()).willReturn(true);
        given(pointReview.isHasPhoto()).willReturn(true);
        given(pointReview.isHasContent()).willReturn(true);
        //when
        pointReviewCommand.deduction(event);
        //then
        verify(eventPublisher, times(3)).publishEvent(any(PointHistoryEvent.class));
        verify(pointReviewRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("리뷰 삭제 : INIT/CONTENT 회수")
    public void deleteReviewPointCounting2() {
        //given
        PointReview pointReview = mock(PointReview.class);
        PointReviewEvent event = mock(PointReviewEvent.class);
        given(event.getReviewId()).willReturn("123");
        given(pointReviewRepository.findById(any())).willReturn(Optional.of(pointReview));
        given(pointReview.isInit()).willReturn(true);
        given(pointReview.isHasPhoto()).willReturn(false);
        given(pointReview.isHasContent()).willReturn(true);
        //when
        pointReviewCommand.deduction(event);
        //then
        verify(eventPublisher, times(2)).publishEvent(any(PointHistoryEvent.class));
        verify(pointReviewRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("리뷰 삭제 : CONTENT 회수")
    public void deleteReviewPointCounting3() {
        //given
        PointReview pointReview = mock(PointReview.class);
        PointReviewEvent event = mock(PointReviewEvent.class);
        given(event.getReviewId()).willReturn("123");
        given(pointReviewRepository.findById(any())).willReturn(Optional.of(pointReview));
        given(pointReview.isInit()).willReturn(false);
        given(pointReview.isHasPhoto()).willReturn(false);
        given(pointReview.isHasContent()).willReturn(true);
        //when
        pointReviewCommand.deduction(event);
        //then
        verify(eventPublisher, times(1)).publishEvent(any(PointHistoryEvent.class));
        verify(pointReviewRepository, times(1)).deleteById(any());
    }

    @TestConfiguration
    static class MockitoPublisherConfiguration {

        @Bean
        @Primary
        ApplicationEventPublisher publisher() {
            return mock(ApplicationEventPublisher.class);
        }
    }

}
