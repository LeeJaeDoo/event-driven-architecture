package com.jd.domain;

import com.jd.application.enums.PointActionType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "point_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "point_amount")
    private long pointAmount;

    @Column(name = "point_action_type")
    @Enumerated(EnumType.STRING)
    private PointActionType actionType;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static PointHistory create(String userId, PointActionType actionType) {
        return PointHistory.builder()
                           .userId(userId)
                           .actionType(actionType)
                           .build();
    }

    public void addPoint() {
        this.pointAmount++;
    }

    public void minusPoint() {
        this.pointAmount--;
    }

}
