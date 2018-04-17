package com.fmarcheni.rabbitmq.user.persistence;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer age;
    private LocalDateTime request;
    private LocalDateTime created;

    public User() {
        // JPA
    }

    private User(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.request = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        created = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDateTime getRequest() {
        return request;
    }

    public void setRequest(LocalDateTime request) {
        this.request = request;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public static class Builder {
        private String name;
        private Integer age;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withAge(Integer age) {
            this.age = age;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }
}
