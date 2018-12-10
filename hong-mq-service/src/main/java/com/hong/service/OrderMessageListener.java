package com.hong.service;

import com.alibaba.fastjson.JSON;
import com.hong.config.DelayQueueConfig;
import com.hong.config.OrderMqConfig;
import com.hong.config.XdelayConfig;
import com.hong.constant.MessageConstants;
import com.hong.entity.Order;
import com.hong.mapper.BrokerMessageLogMapper;
import com.hong.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * <br>消息监听服务</br>
 */
@EnableRabbit
@Component
@Configuration
public class OrderMessageListener {

    private static Logger logger = LoggerFactory.getLogger(OrderMessageListener.class);

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Autowired
    private OrderMapper orderMapper;

    @RabbitHandler
    @RabbitListener(queues = OrderMqConfig.ORDER_DIRECT_QUEUE)
    public void handleMessageConfirm(Order order) {
        logger.info("异步消息[start]：出队列[{}]，消息内容：{},", OrderMqConfig.ORDER_DIRECT_QUEUE, JSON.toJSONString(order));
        String messageId = order.getMessageId();
        try {
            brokerMessageLogMapper.changeBrokerMessageLogStatus(messageId, MessageConstants.StatusConstants.MESSAGE_SEND_SUCCESS, new Date());
        } catch (Exception e) {
            //失败则进行具体的后续操作:重试 或者补偿等手段
            logger.error("订单:[{}]消息确认发生异常", JSON.toJSONString(order));
        }
    }

    @RabbitHandler
   // @RabbitListener(queues = DelayQueueConfig.IMMEDIATE_QUEUE)
    @RabbitListener(queues = XdelayConfig.IMMEDIATE_QUEUE_XDELAY)
    public void handleNoPayOrder(Order order) {
        logger.info("延时消息[start]：出队列[{}]，消息内容：{},", DelayQueueConfig.IMMEDIATE_QUEUE , order);
        String orderId = order.getId();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("订单入库1min时间到了,开始校验该订单状态,当前时间:" + sdf.format(new Date()));
            // 关闭超过1min未支付的订单
            Order o = orderMapper.getOrderById(orderId);
            if (Objects.equals("1",o.getStatus())){
                logger.info("订单id[{}],status:[{}]",orderId,o.getStatus());
                orderMapper.updateOrderStatus(orderId,"3");
            }
        } catch (Exception e) {
            //失败则进行具体的后续操作:重试 或者补偿等手段
            logger.error("订单:[{}]--消息确认发生异常", JSON.toJSONString(order));
        }
    }
}
