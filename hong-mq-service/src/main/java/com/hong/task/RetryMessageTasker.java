package com.hong.task;

import com.alibaba.fastjson.JSONObject;
import com.hong.constant.MessageConstants;
import com.hong.entity.BrokerMessageLog;
import com.hong.entity.Order;
import com.hong.mapper.BrokerMessageLogMapper;
import com.hong.service.RabbitOrderSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by John on 2018/12/8.
 */
@Component
public class RetryMessageTasker {

    private static Logger logger = LoggerFactory.getLogger(RetryMessageTasker.class);

    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Scheduled(initialDelay = 5000, fixedDelay = 5000)
    public void reSend(){
        logger.info("定时重试失败消息开始。。。。");
        //pull status = 0 and timeout message
        List<BrokerMessageLog> list = brokerMessageLogMapper.query4StatusAndTimeoutMessage();
        logger.info("当前失败消息总数:{}",list.size());
        list.forEach(messageLog -> {
            if(messageLog.getTryCount() >= 3){
                //update fail message
                brokerMessageLogMapper.changeBrokerMessageLogStatus(messageLog.getMessageId(), MessageConstants.StatusConstants.MESSAGE_SEND_FAILURE, new Date());
            } else {
                logger.info("当前重试消息id:{}",messageLog.getMessageId());
                logger.info("当前重试次数:{}",messageLog.getTryCount());
                // resend
                brokerMessageLogMapper.update4ReSend(messageLog.getMessageId(),  new Date());
                Order reSendOrder = JSONObject.parseObject(messageLog.getMessage(),Order.class);
                try {
                    rabbitOrderSender.sendOrder(reSendOrder);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("-----------异常处理-----------");
                }
            }
        });
    }
}
