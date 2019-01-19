package com.hong.service;

import com.alibaba.fastjson.JSONObject;
import com.hong.config.OrderMqConfig;
import com.hong.mapper.DistributedMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * Created by John on 2019/1/19.
 * 基于MQ的分布式事务消息处理层
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MqTransactionService {

    private final Logger logger = LoggerFactory.getLogger(MqTransactionService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DistributedMessageMapper distributedMessageMapper;

    /**
     * 发送mq消息，更新本地消息表的状态
     * @param orderInfo
     */
    public void sendMsg(JSONObject orderInfo){
        // @PostConstruct会在初始化时设置MQ回调处理函数
        // CorrelationData 当收到消息回执时，会附带上这个参数
        rabbitTemplate.convertAndSend(OrderMqConfig.ORDER_CREATE_EXCHANGE,OrderMqConfig.ORDER_CREATE_RK,orderInfo.toJSONString(),
                new CorrelationData(orderInfo.getString("messageId")));
    }

    /**
     *  被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器调用一次，类似于Servlet的inti()方法。
     *  被@PostConstruct修饰的方法会在构造函数之后，init()方法之前运行。
     */
    @PostConstruct
    public void setUp(){
        /**
         * 消息发送完毕后，则回调此方法，ack表示发送是否成功
         */
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (!ack){
                    return;
                }

                try {
                    int count = distributedMessageMapper.updateStatus(correlationData.getId(), 1);
                    if (count != 1){
                        logger.warn("警告:本地消息表的状态更新失败");
                    }
                } catch (Exception e) {
                    logger.warn("警告:本地消息表的状态更新出现异常");
                }
            }
        });
    }

}
