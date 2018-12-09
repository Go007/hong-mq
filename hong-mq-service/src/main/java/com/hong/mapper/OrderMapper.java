package com.hong.mapper;

import com.hong.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Created by John on 2018/12/8.
 */
public interface OrderMapper {
    int insertOne(Order order);

    void updateOrderStatus(@Param("id")String id, @Param("status")String status);

    Order getOrderById(@Param("id")String id);
}
