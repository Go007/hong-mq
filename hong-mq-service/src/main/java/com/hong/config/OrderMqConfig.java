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
        return new DirectExchange(ORDER_DIRECT_EXCHANGE);
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
    public Binding orderDirectBinding() {
        return BindingBuilder.bind(orderDirectQueue()).to(orderDirectExchange()).with(ORDER_CREATE_ROUTING_KEY);
    }

    /********************************延迟队列***********************************/
    /**
     * 延迟消费步骤
     * 生产者 ---> 缓冲队列 ---> DLX(Dead Letter Exchange) ---->过期后通过DLX转发到实际消费队列--->实际消费队列--->消费者
     */
    //TTL配置在消息上的缓冲队列
    public static final String DELAY_QUEUE_PER_MESSAGE_TTL_ORDER = "ORDER.PAY.TTL.QUEUE";

    public static final String DELAY_QUEUE_ROUTING_KEY_ORDER = "DELAY.QUEUE.ROUTING.ORDER";

    @Bean
    Queue delayQueuePerMessageTTL() {
        return QueueBuilder.durable(DELAY_QUEUE_PER_MESSAGE_TTL_ORDER)
                .withArgument("x-dead-letter-exchange", ORDER_DIRECT_EXCHANGE) // DLX，dead letter发送到的exchange
                .withArgument("x-dead-letter-routing-key", DELAY_QUEUE_ROUTING_KEY_ORDER) // dead letter携带的routing key
                .build();
    }

    @Bean
    Binding dlxBinding(Queue delayQueuePerMessageTTL, DirectExchange orderDirectExchange) {
        return BindingBuilder.bind(delayQueuePerMessageTTL).to(orderDirectExchange).with(DELAY_QUEUE_ROUTING_KEY_ORDER);
    }

    public static final String TTL_QUEUE_NOTIFY_ORDER = "TTL.QUEUE.NOTIFY.ORDER";

    @Bean
    public Queue orderPayNotifyQueue() {
        return new Queue(TTL_QUEUE_NOTIFY_ORDER);
    }

    @Bean
    Binding bindingNotifyMessage(Queue orderPayNotifyQueue, DirectExchange orderDirectExchange) {
        return BindingBuilder.bind(orderPayNotifyQueue).to(orderDirectExchange).with(DELAY_QUEUE_ROUTING_KEY_ORDER);
    }
    /********************************延迟队列***********************************/
}
