package com.hong.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 利用Rabbitmq的插件x-delay-message实现延迟队列
 */
@Configuration
public class XdelayConfig {

    public static final String IMMEDIATE_QUEUE_XDELAY = "IMMEDIATE_QUEUE_XDELAY";
    public static final String DELAYED_EXCHANGE_XDELAY = "DELAYED_EXCHANGE_XDELAY";
    public static final String DELAY_ROUTING_KEY_XDELAY = "DELAY_ROUTING_KEY_XDELAY";

    // 创建一个立即消费队列
    @Bean
    public Queue immediateQueue() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(IMMEDIATE_QUEUE_XDELAY, true);
    }

    @Bean
    public CustomExchange customDelayExchange() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_XDELAY, "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding bindingNotify() {
        return BindingBuilder.bind(immediateQueue()).to(customDelayExchange()).with(DELAY_ROUTING_KEY_XDELAY).noargs();
    }
}
