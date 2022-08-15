package org.example.config.redis;

import cn.hutool.core.bean.BeanUtil;
import org.example.pojo.Orders;
import org.example.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @ClassName: RedisKeyExpirationListener
 * @Author: MaCongYi
 * @create: 2022-08-15 20:15
 * @Description:
 * @Version: 1.0
 */


@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Autowired
    private IOrdersService iOrdersService;
    @Autowired
    private RedisTemplate redisTemplate;

    private static String order="order:";


    /**
     * 使用该方法监听 当我们的key失效的时候执行该方法
     *
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiraKey = message.toString();
        //截取:之前字符串
        String sign = expiraKey.substring(0, expiraKey.indexOf(":"));
        String data =expiraKey.substring(expiraKey.indexOf(":"));
        switch (sign){
            case  "orders:":{
                System.out.println("该key：expiraKey：" + expiraKey + "失效啦~");
                Map map = redisTemplate.opsForHash().entries(order + data);
                Orders orders = BeanUtil.fillBeanWithMap (map,new Orders(), true);
                orders.setCreateTime(LocalDateTime.parse(map.get("payDate").toString().replace("T"," ").replace("\"",""), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
                orders.setRiderAcceptDate(LocalDateTime.parse(map.get("riderAcceptDate").toString().replace("T"," ").replace("\"",""), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
                orders.setRiderGetDate(LocalDateTime.parse(map.get("payriderGetDateDate").toString().replace("T"," ").replace("\"",""), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
                orders.setRiderGetDate(LocalDateTime.parse(map.get("riderGetDate").toString().replace("T"," ").replace("\"",""), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
                orders.setRiderCompleteDate(LocalDateTime.parse(map.get("riderCompleteDate").toString().replace("T"," ").replace("\"",""), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
                orders.setCreateTime(LocalDateTime.parse(map.get("createTime").toString().replace("T"," ").replace("\"",""), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
                orders.setUpdateTime(LocalDateTime.parse(map.get("updateTime").toString().replace("T"," ").replace("\"",""), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
                orders.setEstimatedTime(LocalDateTime.parse(map.get("estimatedTime").toString().replace("T"," ").replace("\"",""), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
                iOrdersService.save(orders);
                redisTemplate.delete(order+data);
            }
        }



    }
}
