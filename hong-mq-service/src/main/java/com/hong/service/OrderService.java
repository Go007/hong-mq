package com.hong.service;

import com.alibaba.fastjson.JSON;
import com.hong.constant.MessageConstants;
import com.hong.entity.BrokerMessageLog;
import com.hong.entity.Order;
import com.hong.mapper.BrokerMessageLogMapper;
import com.hong.mapper.OrderMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by John on 2018/12/8.
 */
@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    @Autowired
    private OrderBizService orderBizService;

    @Autowired
    private MqTransactionService mqTransactionService;

    /**
     * 延时消息示例
     *
     * @param order
     * @throws Exception
     */
    public void createOrder(Order order) throws Exception {
        // 使用当前时间当做订单创建时间（为了模拟一下简化）
        Date orderTime = new Date();
        // 插入业务数据
        orderMapper.insertOne(order);
        // 插入消息记录表数据
        BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
        // 消息唯一ID
        brokerMessageLog.setMessageId(order.getMessageId());
        // 保存消息整体 转为JSON 格式存储入库
        brokerMessageLog.setMessage(JSON.toJSONString(order));
        //  表示发送中
        brokerMessageLog.setStatus("0");
        // 设置消息未确认超时时间窗口为 一分钟
        brokerMessageLog.setNextRetry(DateUtils.addMinutes(orderTime, MessageConstants.RETRY_TIMEOUT));
        brokerMessageLog.setCreateTime(new Date());
        brokerMessageLog.setUpdateTime(new Date());
        brokerMessageLogMapper.insertOne(brokerMessageLog);
        // 发送消息
        //rabbitOrderSender.sendOrder(order);
        //rabbitOrderSender.sendOrder2(order);
        //rabbitOrderSender.sendOrder3(order);
        rabbitOrderSender.sendOrder4(order);
    }

    /**
     * 基于MQ的分布式事务
     *
     * @param order
     * @throws Exception
     */
    public void createOrder2(Order order) throws Exception {
        // 1.订单信息 -- 插入订单系统，订单数据库（事务1）
        orderBizService.saveOrder(order);
        // 2.发送消息到MQ且通过MQ的确认机制保证消息成功发送到MQ
        mqTransactionService.sendMsg(JSON.parseObject(JSON.toJSONString(order)));
    }

}
