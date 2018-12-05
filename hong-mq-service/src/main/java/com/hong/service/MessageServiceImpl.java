package com.hong.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *  消息投递处理类
 */
@RestController
public class MessageServiceImpl {

    @Resource
    private AmqpTemplate rabbitTemplate;

  /*  @Override
    public Result<Void> sendMessage(@RequestBody AsyncMessageBean asyncMessageBean) {
        log.info("异步消息[start]：入队列[{}]，消息内容：{},", MqRoutingKeyConstant.ASYNC_ROUTING_KEY, asyncMessageBean);
        this.rabbitTemplate.convertAndSend(MqExchangeConstant.MQ_TOPIC_EXCHANGE, MqRoutingKeyConstant.ASYNC_ROUTING_KEY,
                asyncMessageBean);
        log.info("异步消息[end]：入队列[{}]，消息内容：{},", MqRoutingKeyConstant.ASYNC_ROUTING_KEY, asyncMessageBean);
        return new Result<>();
    }*/
}
