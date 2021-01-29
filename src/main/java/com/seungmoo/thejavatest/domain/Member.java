package com.seungmoo.thejavatest.domain;

public class Member {
    private long id;
    private String email;

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
