package com.jd.domain;

import com.jd.adapter.in.model.PointReviewEvent;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Jaedoo Lee
 */
@Entity
@Table(name = "point_review")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "delete_yn='N'")
@SQLDelete(sql = "UPDATE point_review SET delete_yn='Y' WHERE id = ?")
public class PointReview {

    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "place_id")
    private String placeId;

    @Type(type = "yes_no")
    @Column(name = "content_length_yn")
    private boolean hasContent;

    @Type(type = "yes_no")
    @Column(name = "photo_yn")
    private boolean hasPhoto;

    @Type(type = "yes_no")
    @Column(name = "init_yn")
    private boolean isInit;

    @Type(type = "yes_no")
    @Column(name = "delete_yn")
    private boolean isDeleted;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updateAt;

    public static PointReview create(PointReviewEvent event) {
        return PointReview.builder()
                          .id(event.getReviewId())
                          .userId(event.getUserId())
                          .placeId(event.getPlaceId())
                          .hasContent(!ObjectUtils.isEmpty(event.getContent()))
                          .hasPhoto(!event.getAttachedPhotoIds().isEmpty())
                          .isInit(false)
                          .isDeleted(false)
                          .build();
    }

    public void initCreate() {
        this.isInit = true;
    }

    public void update(PointReviewEvent event) {
        this.hasContent = !ObjectUtils.isEmpty(event.getContent());
        this.hasPhoto = !ObjectUtils.isEmpty(event.getAttachedPhotoIds());
    }

}
