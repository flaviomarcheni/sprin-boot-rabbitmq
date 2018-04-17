package com.fmarcheni.rabbitmq.user.controller.to.parser;

import java.util.function.Function;

import com.fmarcheni.rabbitmq.user.controller.to.UserTO;
import com.fmarcheni.rabbitmq.user.persistence.User;

public class UserParser implements Function<UserTO, User> {

    @Override
    public User apply(UserTO t) {
        return new User.Builder().withName(t.getName()).withAge(t.getAge()).build();
    }

}
