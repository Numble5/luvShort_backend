package com.example.backend.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // JPA Entity 클래스들이 BaseEntity를 상속할 경우 필드들(createdDate, updatedDate)도 칼럼으로 인식하도록 함
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate // Entity가 생성되어 저장될 때의 시간이 자동저장
    private LocalDateTime createdDate;

    @LastModifiedDate // 조회한 Entity의 값을 변경할 때의 시간이 자동 저장
    private LocalDateTime updatedDate;
}
