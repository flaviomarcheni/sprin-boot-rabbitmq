package com.fmarcheni.rabbitmq.user.controller.to;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final Integer age;
    private final String name;
    private final LocalDateTime request;

    public UserTO(@JsonProperty("name") String name, @JsonProperty("age") Integer age, @JsonProperty("request") LocalDateTime request) {
        this.name = name;
        this.age = age;
        this.request = request;

    }

    public Integer getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return "UserTO [age=" + age + ", name=" + name + ", request=" + request + "]";
    }

}
