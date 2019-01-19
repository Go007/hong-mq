package com.hong.service;

import com.alibaba.fastjson.JSON;
import com.hong.entity.DistributedMessage;
import com.hong.entity.Order;
import com.hong.mapper.BrokerMessageLogMapper;
import com.hong.mapper.DistributedMessageMapper;
import com.hong.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by John on 2019/1/19.
 * 聚合服务层
 */
@Service
public class OrderBizService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Autowired
    private DistributedMessageMapper distributedMessageMapper;

    public void saveOrder(Order order) {
        int count = orderMapper.insertOne(order);
        if (count != 1){
            throw new RuntimeException("出现异常,数据库操作失败");
        }
        saveLocalMessage(order);
    }

    /**
     * 保存本地消息表
     *
     * @param order
     */
    public void saveLocalMessage(Order order) {
        // 插入消息记录表数据
        DistributedMessage message = new DistributedMessage();
        message.setMsgId(order.getMessageId());
        message.setMsgContent(JSON.toJSONString(order));
        message.setMsgStatus(0);
        message.setCreateTime(new Date());
        int count = distributedMessageMapper.insertOne(message);
        if (count != 1){
            throw new RuntimeException("出现异常,数据库操作失败");
        }
    }

}
