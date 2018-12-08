package com.hong.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by John on 2018/12/8.
 */
@Configuration
public class OrderMqConfig {
    public static final String ORDER_TOPIC_EXCHANGE = "ORDER.TOPIC.EXCHANGE";
    public static final String ORDER_TOPIC_QUEUE = "ORDER.TOPIC.QUEUE";

    @Bean
    public TopicExchange orderTopicExchange(){
        return new TopicExchange(ORDER_TOPIC_EXCHANGE);
    }

    @Bean
    public Queue orderTopicQueue(){
        return new Queue(ORDER_TOPIC_QUEUE);
    }

    @Bean
    public Binding orderTopicBinding(){
        return BindingBuilder.bind(orderTopicQueue()).to(orderTopicExchange()).with("order.*");
    }
}
