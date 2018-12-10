package com.hong.service;

import com.alibaba.fastjson.JSON;
import com.hong.common.utils.ByteArrayUtils;
import com.hong.config.DelayQueueConfig2;
import com.hong.entity.Order;
import com.hong.mapper.OrderMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Component
public class ProcessReceiver implements ChannelAwareMessageListener {

    private static Logger logger = LoggerFactory.getLogger(ProcessReceiver.class);

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            processMessage(message);
        }
        catch (Exception e) {
            // 如果发生了异常，则将该消息重定向到缓冲队列，会在一定延迟之后自动重做
            channel.basicPublish(DelayQueueConfig2.PER_QUEUE_TTL_EXCHANGE_NAME, DelayQueueConfig2.DELAY_QUEUE_PER_QUEUE_TTL_NAME, null,
                    "The failed message will auto retry after a certain delay".getBytes());
        }
    }

    /**
     * 如果订单超过1min后仍未支付则需要及时地关闭订单
     *
     * @param message
     * @throws Exception
     */
    public void processMessage(Message message) throws Exception {
        Optional<Order> order = ByteArrayUtils.bytesToObject(message.getBody());
        String orderId = order.get().getId();
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
