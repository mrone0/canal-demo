package com.mrone.consumer;

import com.mrone.entity.CanalMessage;
import com.mrone.entity.User;
import com.mrone.service.SyncService;
import com.mrone.util.GsonUtil;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Mr.One
 * @program canal-demo
 * @description
 * @create 2024-06-20 20:36
 **/
@Component
public class MqConsumer {

    private Logger logger = LoggerFactory.getLogger(MqConsumer.class);

    @Autowired
    private SyncService<User> service;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "mysql"), exchange = @Exchange(value = "mysql")))
    public void businessQueue(@Payload byte[] message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            logger.info("消息{}",message);
            String realMessage = new String(message, StandardCharsets.UTF_8);
            logger.info("监听到canal消息{}",realMessage);
            CanalMessage<User> canalMessage = GsonUtil.gson.fromJson(realMessage, CanalMessage.class);
            service.syncDb2Es(realMessage,canalMessage);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);
            e.printStackTrace();
        }
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "dead.letter.queue"), exchange = @Exchange(value = "dead.letter.exchange")))
    public void deadLetterQueue(@Payload byte[] message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        logger.info("死信队列业务逻辑");
    }
}
