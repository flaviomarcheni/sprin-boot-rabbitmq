package com.fmarcheni.rabbitmq.user.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fmarcheni.rabbitmq.infra.RabbitConfig;
import com.fmarcheni.rabbitmq.user.controller.to.UserTO;
import com.fmarcheni.rabbitmq.user.controller.to.parser.UserParser;
import com.fmarcheni.rabbitmq.user.persistence.User;
import com.fmarcheni.rabbitmq.user.persistence.UserRepository;

@RestController("/user")
public class UserController {
    Logger log = LoggerFactory.getLogger(UserController.class);
    private UserRepository usuarioRepository;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public UserController(UserRepository usuarioRepository, RabbitTemplate rabbitTemplate) {
        this.usuarioRepository = usuarioRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public void saveAsync() {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_APP_NAME, RabbitConfig.NEW_USER_ROUTING_KEY,
                new UserTO(RandomStringUtils.randomAlphabetic(10), RandomUtils.nextInt(0, 100), LocalDateTime.now()));

    }

    @RabbitListener(queues = RabbitConfig.NEW_USER_QUEUE)
    public void receiveMessage(final UserTO userMessage, Message message) throws Exception {
        log.info("message received: {}", userMessage);
        if (RandomUtils.nextBoolean()) {
            throw new Exception("Random error");
        }
        log.info("message processed: {}", userMessage);
        usuarioRepository.save(new UserParser().apply(userMessage));
    }

    @RabbitListener(queues = RabbitConfig.NEW_USER_QUEUE_DLQ)
    public void dlq(final UserTO customMessage, Message message) throws Exception {
        log.info("Message DLQ: {}", customMessage);
    }

    @GetMapping
    public List<User> listarAll() {
        return usuarioRepository.findAll();

    }
}
