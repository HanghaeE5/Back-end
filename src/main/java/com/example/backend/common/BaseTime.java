package com.example.backend.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass // JPA Entity 클래스들이 BaseTimeEntity를 상속할 경우 필드들(createdDate, modifiedDate)도 컬럼으로 인식하도록 합니다.
@EntityListeners(AuditingEntityListener.class) //BaseTimeEntiy 클래스에 Auditing 기능을 포함시킵니다.
@Getter
public abstract class BaseTime {

        @CreatedDate //Entity가 생성되어 저장될 때 시간이 자동 저장됩니다
        private LocalDateTime createdDate;

        @LastModifiedDate //조회한 Entity의 값을 변경할 때 시간이 자동 저장됩니다.
        private LocalDateTime modifiedDate;

}
