package com.seungmoo.thejavatest.test;

import com.seungmoo.thejavatest.member.Member;

import java.time.LocalDateTime;

public class Study {

    private StudyStatus status;

    private int limit;

    private String name;
    private Member owner;
    private LocalDateTime openedDateTime;

    public Study(int limit, String name) {
        this.limit = limit;
        this.name = name;
    }

    public Study(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit는 0보다 커야 한다.");
        }
        this.limit = limit;
    }

    public StudyStatus getStatus() {
        return this.status;
    }

    public void setStatus(StudyStatus status) {
        this.status = status;
    }

    public int getLimit() {
        return limit;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Study{" +
                "status=" + status +
                ", limit=" + limit +
                ", name='" + name + '\'' +
                '}';
    }

    public void setOwner(Member member) {
        this.owner = member;
    }

    public Member getOwner() {
        return owner;
    }

    public void open() {
        this.openedDateTime = LocalDateTime.now();
        this.status = StudyStatus.OPENED;
    }

    public LocalDateTime getOpenedDateTime() {
        return openedDateTime;
    }
}
