参考链接:https://www.imooc.com/article/49814

#RabbitMQ实现消息可靠性投递
RabbitMQ可视化监控平台 http://localhost:15672

1. 采用微服务架构，将消息服务本身作为一个微服务注册到Eureka上，
   这样在消息服务回调通知业务方时，可以采取随机选取的负载均衡策略，
   调用服务方相应的回调接口。
   
2. 约定：如果不同的服务需要隔离各自的消息，除了可以设置不同的virtual-host进行粗粒度的隔离外，
   还可以约定将不同服务对应的exchange命名为{service-id}-exchange的格式进行细粒度的隔离。
   如果是通用的exchange，可以使用com.hong.constant.MessageConstants里的ExchangeConstants类命名。
  