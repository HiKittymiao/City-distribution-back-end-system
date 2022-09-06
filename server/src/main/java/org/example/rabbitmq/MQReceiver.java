package org.example.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.Orders;
import org.example.service.IOrdersService;
import org.example.utlis.JsonUtil;
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
    @Autowired
    private IOrdersService iOrdersService;

    @RabbitListener(queues = "mysqlQueue")
    public void receive(String message) {
        log.info("updatabyid接收消息：" + message);
        Orders orders = JsonUtil.jsonStr2Object(message, Orders.class);
        if(orders.getId()==null){
            log.info("message为空");
            return;}
        if (orders.getStatue()!=0){
            try {
                boolean b = iOrdersService.updateById(orders);
                log.info("更新数据库--->"+b);
            }catch (Exception e){e.printStackTrace();}
        }else{
            try {
                iOrdersService.save(orders);
                log.info("保存数据库--->");
            }catch (Exception e){e.printStackTrace();}
        }
    }
    //@RabbitListener(queues = "mysqlSaveQueue")
    //public void orderSave(String message) {
    //    log.info("save接收消息：" + message);
    //    Orders orders = JsonUtil.jsonStr2Object(message, Orders.class);
    //    try {
    //        iOrdersService.save(orders);
    //    }catch (Exception e){
    //        e.printStackTrace();
    //    }finally {
    //        log.info("数据库更新失败--->");
    //    }
    //
    //}

}
