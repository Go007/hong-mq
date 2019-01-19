package com.hong.service;

import com.hong.entity.OrderDispatch;
import com.hong.mapper.OrderDispatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Created by John on 2019/1/19.
 */
@Service
public class DispatchService {

    @Autowired
    private OrderDispatchMapper dispatchMapper;

    /**
     * 添加调度信息（此处仅往数据库中插入一条记录）
     * @param orderId
     */
    public void dispatch(String orderId){
        System.out.println("分配订单:" + orderId);
        OrderDispatch orderDispatch = new OrderDispatch();
        orderDispatch.setOrderId(orderId);
        orderDispatch.setDispatchId(UUID.randomUUID() + "");
        orderDispatch.setDispatchContent("派送此订单");
        orderDispatch.setDispatchStatus(0); // 准备派送
        orderDispatch.setCreateTime(new Date());
        dispatchMapper.insertOne(orderDispatch);
    }
}
