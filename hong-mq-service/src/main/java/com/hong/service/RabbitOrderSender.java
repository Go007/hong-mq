package com.hong.service;

import com.hong.config.OrderMqConfig;
import com.hong.constant.MessageConstants;
import com.hong.entity.Order;
import com.hong.mapper.BrokerMessageLogMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by John on 2018/12/8.
 */
@Component
public class RabbitOrderSender {
    //自动注入RabbitTemplate模板类
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    //回调函数: confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("correlationData: " + correlationData);
            String messageId = correlationData.getId();
            if (ack) {
                //如果confirm返回成功 则进行更新
                brokerMessageLogMapper.changeBrokerMessageLogStatus(messageId, MessageConstants.StatusConstants.MESSAGE_SEND_SUCCESS, new Date());
            } else {
                //失败则进行具体的后续操作:重试 或者补偿等手段
                System.err.println("异常处理...");
            }
        }
    };

    //发送消息方法调用: 构建自定义对象消息
    public void sendOrder(Order order) throws Exception {
        //rabbitTemplate.setConfirmCallback(confirmCallback); //等效替换 OrderMessageListener
        //消息唯一ID
        CorrelationData correlationData = new CorrelationData(order.getMessageId());
        rabbitTemplate.convertAndSend(OrderMqConfig.ORDER_DIRECT_EXCHANGE, OrderMqConfig.ORDER_CREATE_ROUTING_KEY, order, correlationData);
    }

    /**
     * 延迟队列,校验订单的支付状态
     * 如果订单超过1min后仍未支付则需要及时地关闭订单
     * @param order
     * @throws Exception
     */
    public void sendOrder2(Order order) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("订单入库,1min后校验该订单状态,当前时间:" + sdf.format(new Date()) );
        rabbitTemplate.convertAndSend("DEAD_LETTER_EXCHANGE","DELAY_ROUTING_KEY", order, message -> {
            message.getMessageProperties().setExpiration(String.valueOf(OrderMqConfig.ORDER_PAY_CANCEL_TIME));
            return message;
        });
    }
}
