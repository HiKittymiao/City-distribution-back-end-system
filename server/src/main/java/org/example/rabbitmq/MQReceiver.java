package org.example.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @ClassName: MQReceiver
 * @Author: MaCongYi
 * @create: 2022-08-13 19:13
 * @Description:
 * @Version: 1.0
 */
@Service
@Slf4j
public class MQReceiver {

    @RabbitListener(queues = "seckillQueue")
    public void receive(String message) {
        log.info("接收消息：" + message);
    }

}
