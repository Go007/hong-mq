package com.hong.service;

import com.alibaba.fastjson.JSONObject;
import com.hong.config.OrderMqConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Created by John on 2019/1/19.
 * 订单分派(运单)消费服务
 */
@EnableRabbit
@Component
@Configuration
public class OrderDispatchConsumer {

    private final Logger logger = LoggerFactory.getLogger(OrderDispatchConsumer.class);

    @Autowired
    private DispatchService dispatchService;

    /**
     *  运单服务监听到消息后,开始派遣该笔订单
     *  消息堆积：消费者分组group
     * @param message
     * @param channel
     * @param tag
     * @throws Exception
     */
    @RabbitHandler
    @RabbitListener(queues = OrderMqConfig.ORDER_CREATE_QUEUE)
    public void messageConsumer(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception{
        // 将MQ中的数据转为JSON对象
        try {
            JSONObject orderInfo = JSONObject.parseObject(message);
            logger.info("收到MQ中的消息:" + orderInfo.toJSONString());
            Thread.sleep(5000L);

            // 执行业务操作，幂等，根据业务情况去重（拓展：Redis保留每一条消息的处理记录）
            String orderId = orderInfo.getString("id");
            // 这里就是分配一个外卖小哥
            dispatchService.dispatch(orderId);
            // ack - 告诉MQ，我已收到消息啦
            channel.basicAck(tag,false);
        } catch (Exception e) {
            /**
             * 第三个参数表示client告诉MQ消费失败是否需要重发
             * true:需要
             * false:不需要
             */
            channel.basicNack(tag,false,false);
            // 异常情况，涉及到关键数据，需要人工干预
        }

        // 如果不给回复，就等这个consumer断开连接后，mq-server会继续推送
    }
}
