package com.hong.controller;

import com.hong.entity.Order;
import com.hong.entity.User;
import com.hong.service.OrderService;
import com.hong.service.RabbitOrderSender;
import com.hong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by John on 2018/12/8.
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/getUserById/{id}")
    public User getUserById(@PathVariable("id") String id){
        User user = userService.getUserById(Long.parseLong(id));
        return user;
    }

    @GetMapping("/testSender")
    public void testSender() throws Exception {
        Order order = new Order();
        order.setId("2018080400000001");
        order.setName("测试订单");
        order.setMessageId(System.currentTimeMillis() + "$" + UUID.randomUUID().toString());
        rabbitOrderSender.sendOrder(order);
    }

    @GetMapping("testCreateOrder")
    public void testCreateOrder() throws Exception {
        Order order = new Order();
        order.setId("2018080400000002");
        order.setName("测试创建订单");
        order.setMessageId(System.currentTimeMillis() + "$" + UUID.randomUUID().toString());
        order.setStatus("1");
        orderService.createOrder(order);
    }

}
