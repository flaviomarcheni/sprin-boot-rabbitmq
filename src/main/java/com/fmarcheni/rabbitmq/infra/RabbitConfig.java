package com.fmarcheni.rabbitmq.infra;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@EnableRabbit
public class RabbitConfig implements RabbitListenerConfigurer {

    public static final String EXCHANGE_APP_NAME = "app_exchange";
    public static final String NEW_USER_QUEUE = "new_user_queue";
    public static final String NEW_USER_ROUTING_KEY = "new_user_route";
    public static final String NEW_USER_QUEUE_DLQ = "new_user_queue_dlq";
    public static final String NEW_USER_QUEUE_DLQ_ROUTE = "new_user_dlq_route";

    public TopicExchange appExchange() {
        return new TopicExchange(EXCHANGE_APP_NAME);
    }

    @Bean
    public Queue appQueueSpecific() {
        return QueueBuilder.durable(NEW_USER_QUEUE).withArgument("x-dead-letter-exchange", EXCHANGE_APP_NAME).withArgument("x-dead-letter-routing-key", NEW_USER_QUEUE_DLQ_ROUTE).build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(NEW_USER_QUEUE_DLQ).build();
    }


    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

}
