-- 表 broker_message_log 消息记录结构
CREATE TABLE IF NOT EXISTS `broker_message_log` (
  `message_id` varchar(128) NOT NULL, -- 消息唯一ID
  `message` varchar(4000) DEFAULT NULL, -- 消息内容
  `try_count` int(4) DEFAULT '0', -- 重试次数
  `status` varchar(10) DEFAULT '', -- 消息投递状态  0 投递中 1 投递成功   2 投递失败
  `next_retry` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',  -- 下一次重试时间 或 超时时间
  -- 有兼容性问题,可以改成通用的 `next_retry` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00', -- 创建时间
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00', -- 更新时间
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 表 order 订单结构
CREATE TABLE IF NOT EXISTS `t_order` (
  `id` varchar(128) NOT NULL, -- 订单ID
  `name` varchar(128), -- 订单名称 其他业务熟悉忽略
  `message_id` varchar(128) NOT NULL, -- 消息唯一ID
  `status` varchar(10) DEFAULT '', -- 订单状态 1 支付中   2 支付成功  3 取消订单
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--订单分布式事务-本地消息表(需要和订单表在一个schema下)
CREATE TABLE IF NOT EXISTS t_distributed_message(
  msg_id  varchar(128) DEFAULT NOT NULL,
  msg_content VARCHAR(1024) DEFAULT NULL COMMENT '消息内容',
  msg_status int(11) DEFAULT 0 COMMENT '是否发送到MQ,0-未发送，1-已发送',
  create_time datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--运单表，用于分派订单给外卖小哥
CREATE TABLE IF NOT EXISTS t_order_dispatch(
  dispatch_id varchar(128) DEFAULT NOT NULL COMMENT '调度流水',
  order_id varchar(128) DEFAULT NOT NULL COMMENT '订单id',
  dispatch_status int(11) DEFAULT 0 COMMENT '调度状态',
  dispatch_content VARCHAR(1024) DEFAULT NULL COMMENT '调度内容（送餐员，路线）',
  create_time datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (order_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
