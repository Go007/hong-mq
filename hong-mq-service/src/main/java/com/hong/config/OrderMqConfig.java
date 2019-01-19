package com.hong.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by John on 2018/12/8.
 */
@Configuration
public class OrderMqConfig {

    public static final String ORDER_TOPIC_EXCHANGE = "ORDER.TOPIC.EXCHANGE";

    public static final String ORDER_DIRECT_EXCHANGE = "ORDER.DIRECT.EXCHANGE";

    public static final String ORDER_TOPIC_QUEUE = "ORDER.TOPIC.QUEUE";

    public static final String ORDER_DIRECT_QUEUE = "ORDER.DIRECT.QUEUE";

    public static final String ORDER_CREATE_ROUTING_KEY = "ORDER.CREATE";

    public static long ORDER_PAY_CANCEL_TIME = 60 * 1000;

    @Bean
    public TopicExchange orderTopicExchange() {
        return new TopicExchange(ORDER_TOPIC_EXCHANGE);
    }

    @Bean
    public DirectExchange orderDirectExchange() {
        //return new DirectExchange(ORDER_DIRECT_EXCHANGE);
        return (DirectExchange) ExchangeBuilder.directExchange(ORDER_DIRECT_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue orderTopicQueue() {
        return new Queue(ORDER_TOPIC_QUEUE);
    }

    @Bean
    public Queue orderDirectQueue() {
        return new Queue(ORDER_DIRECT_QUEUE);
    }

    @Bean
    public Binding orderTopicBinding() {
        return BindingBuilder.bind(orderTopicQueue()).to(orderTopicExchange()).with("ORDER.*");
    }

    @Bean
    public Binding orderDirectBinding() { // point-to-point
        return BindingBuilder.bind(orderDirectQueue()).to(orderDirectExchange()).with(ORDER_CREATE_ROUTING_KEY);
    }

    public static final String ORDER_CREATE_EXCHANGE = "ORDER.CREATE.EXCHANGE";
    public static final String ORDER_CREATE_RK = "ORDER.CREATE.RK";
    public static final String ORDER_CREATE_QUEUE = "ORDER.CREATE.QUEUE";

    @Bean
    public DirectExchange orderCreateExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(ORDER_CREATE_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue orderCreateQueue() {
        return new Queue(ORDER_CREATE_QUEUE);
    }

    @Bean
    public Binding orderCreateBinding() { // point-to-point
        return BindingBuilder.bind(orderCreateQueue()).to(orderCreateExchange()).with(ORDER_CREATE_RK);
    }
}
