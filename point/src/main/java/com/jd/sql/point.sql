create table point_history
(
    id                int auto_increment comment 'pk' primary key,
    user_id           binary(36)                         not null comment '리뷰 작성자 ID',
    point_amount      int                                not null comment '포인트',
    point_action_type char(7)                            not null comment '포인트 증감 방식',
    created_at        datetime default CURRENT_TIMESTAMP not null comment '포인트 증감 처리일'
);

create index idx_user_id_01 on point_history (user_id);

create table point_review
(
    id                binary(36)                         not null comment 'pk'primary key,
    user_id           binary(36)                         not null comment '리뷰 작성자 ID',
    content_length_yn char     default 'N'               not null comment '포인트 지급 유형',
    place_id          binary(36)                         not null comment '리뷰가 작성된 장소 ID',
    photo_yn          char     default 'N'               not null comment '사진 등록 여부',
    init_yn           char     default 'N'               not null comment '리뷰 최초 등록 여부',
    delete_yn         char     default 'N'               not null comment '리뷰 삭제 여부',
    created_at        datetime default CURRENT_TIMESTAMP not null comment '리뷰 등록일',
    updated_at        datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '리뷰 수정일'
);

create index idx_place_id_user_id_01 on point_review (place_id, user_id);
