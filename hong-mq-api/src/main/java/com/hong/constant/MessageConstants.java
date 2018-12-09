package com.hong.constant;

/**
 * 消息常量类
 */
public class MessageConstants {

    /**
     * 息状态常量类
     */
    public static class StatusConstants{
        public static final String MESSAGE_SENDING = "0"; //发送中

        public static final String MESSAGE_SEND_SUCCESS = "1"; //成功

        public static final String MESSAGE_SEND_FAILURE = "2"; //失败，需要人工介入处理
    }

    /**
     * 超时单位：ms
     * 补偿策略：定时任务每次间隔1分钟从表中抓取状态为0的任务，
     * 也即需要重发的消息，(也就是说，在1分钟的时间窗口内，消息没有被确认，则会被定时任务拉取出来重发)
     */
    public static final int RETRY_TIMEOUT = 60*1000;

    /**
     * 交换器常量
     */
    public static class ExchangeConstants{
        public static final String MQ_DIRECT_EXCHANGE = "mq.direct.exchange";

        public static final String MQ_TOPIC_EXCHANGE = "mq.topic.exchange";

        public static final String MQ_FANOUT_EXCHANGE = "mq.fanout.exchange";
    }

    /**
     * 队列常量
     */
    public static class QueueConstants{
        /**
         * 异常重试
         */
        public static final String ASYNC_QUEUE = "async.queue";

        /**
         * 延时消息（死信队列）
         */
        public static final String TTL_QUEUE = "ttl.queue";
        public static final String TTL_NOTIFY_QUEUE = "ttl.notify.queue";
    }

    /**
     * 路由键常量
     */
    public static class RoutingKeyConstant{
        /**
         * 异步消息
         */
        public static final String ASYNC_ROUTING_KEY = "async.routing.key";

        /**
         * 延时消息（死信队列）
         */
        public static final String TTL_ROUTING_KEY = "ttl.routing.key";
        public static final String TTL_NOTIFY_ROUTING_KEY = "ttl.notify.routing.key";
    }

}
