package com.hong.service;

import com.hong.bean.BizMessageBean;
import com.hong.bean.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *  消息投递处理类
 */
@RestController
public class MessageServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Resource
    private AmqpTemplate rabbitTemplate;

/*    public Result sendMessage(@RequestBody BizMessageBean messageBean) {
        log.info("异步消息[start]：入队列[{}]，消息内容：{},", MqRoutingKeyConstant.ASYNC_ROUTING_KEY, asyncMessageBean);
        this.rabbitTemplate.convertAndSend(MqExchangeConstant.MQ_TOPIC_EXCHANGE, MqRoutingKeyConstant.ASYNC_ROUTING_KEY,
                asyncMessageBean);
        log.info("异步消息[end]：入队列[{}]，消息内容：{},", MqRoutingKeyConstant.ASYNC_ROUTING_KEY, asyncMessageBean);
        return new Result();
    }*/
}
