package com.seungmoo.thejavatest.test;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class Study {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated
    private StudyStatus status = StudyStatus.DRAFT;

    // 원래 limit였는데 limitCount로 이름 바꾸니까 정상 DDL 정상 실행됨.
    // 예약어 때문인듯
    private Integer limitCount;

    private String name;
    private Long ownerId;
    private LocalDateTime openedDateTime;

    public Study(int limit, String name) {
        this.limitCount = limit;
        this.name = name;
    }

    public Study(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit는 0보다 커야 한다.");
        }
        this.limitCount = limit;
    }

    public void open() {
        this.openedDateTime = LocalDateTime.now();
        this.status = StudyStatus.OPENED;
    }
}
