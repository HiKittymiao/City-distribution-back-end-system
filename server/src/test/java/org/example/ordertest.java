package org.example;
/**
 * @ClassName: ordertest
 * @Author: MaCongYi
 * @create: 2022-08-15 21:58
 * @Description:
 * @Version: 1.0
 */

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import org.example.common.R;
import org.example.dto.OrderConfirm;
import org.example.dto.OrderDetial;
import org.example.pojo.Orders;
import org.example.service.IOrdersService;
import org.example.vo.PriceAndDistance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: MaCongYi
 * @Date: 2022/08/15 21:58
 * @Description:
 * @Version:1.0
 */
@SpringBootTest
public class ordertest {
    @Autowired
    private IOrdersService iOrdersService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void fdas(){
        //流水线
        Long start = System.currentTimeMillis();
        List<Object> List = redisTemplate.executePipelined(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                for (int i = 1000001; i < 2000000; i++) {
                    String key = "pipeline_" + i;
                    String value = "value_" + i;
                    connection.set(key.getBytes(), value.getBytes());
                }
                return null;
            }
        });

        Long end = System.currentTimeMillis();
        System.out.println("Pipeline插入1000000条记录耗时：" + (end - start) + "毫秒。");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
    }

    @Test
    void fdsa(){
        stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                for (int i = 0; i < 100; i++) {
                    connection.setNX(("key"+i).getBytes(),("value"+i).getBytes());
                }
                return null;
            }
        });
    }

    @Test
    void xiadan(){
        OrderDetial orderDetial ;
        PriceAndDistance priceAndDistance = new PriceAndDistance();


        Map<Object, Object> colorMap = MapUtil.of(new String[][] {
                {
                        "addresseeAddress", "在商学院"}
                ,{"addresseeName", "cbb"}
                ,{"addresseePhone", "520520520520"}
                ,{"customerId", "1"}
                ,{"goodsDescribe", "ikun"}
                ,{"goodsWeight", "2"}
                ,{"orderType", "CD"}
                ,{"rlatitude", "43.54654"}
                ,{"rlongitude", "43.54654"}
                ,{"senderAddress", "kun家"}
                ,{"senderName", "cbbHZ"}
                ,{"senderPhone", "250250250"}
                ,{"slatitude", "43.54644"},
                {"slongitude", "43.54644"}
        });
        orderDetial = BeanUtil.fillBeanWithMap (colorMap,new OrderDetial(), true);
        priceAndDistance.setPrice(10d);
        priceAndDistance.setDistance(1d);
        for (int i = 0; i < 5; i++) {
            String s = iOrdersService.newOrder(orderDetial, priceAndDistance);
            Boolean aBoolean = iOrdersService.PayOrder(Long.valueOf(s),1);
            if (aBoolean){
                System.out.println(i+"付款成功");
            }
        }


    }
    @Test
    void fda(OrderDetial o){
        OrderConfirm orderConfirm = new OrderConfirm();
        orderConfirm.setOrderId("1220818857540001");
        orderConfirm.setCustomerId(1);
        R r = iOrdersService.confirmGoods(orderConfirm);
        System.out.println(r);

        Orders orders = new Orders();
        orders.setCustomerId(o.getCustomerId());
        orders.setSenderPhone(o.getSenderPhone());
        orders.setSenderName(o.getSenderName());
        orders.setSenderAddress(o.getSenderAddress());
        orders.setAddresseeName(o.getAddresseeName());
        orders.setAddresseePhone(o.getAddresseePhone());
        orders.setAddresseeAddress(o.getAddresseeAddress());
        orders.setGoodsDescribe(o.getGoodsDescribe());
        orders.setSLongitude(o.getSLongitude());
        orders.setSLatitude(o.getSLatitude());
        orders.setRLongitude(o.getRLongitude());
        orders.setRLatitude(o.getRLatitude());
        orders.setOrderType(o.getOrderType());
        orders.setGoodsType(o.getGoodsType());
        orders.setGoodsWeight(o.getGoodsWeight());
        //使用这种方法容易调试不容易出问题
        //BeanUtil springCore 会出现的问题是如果更改字段名后另一个没改会导致报错
        //Mapstruct 字段映射在编译时进行映射容易报错
    }



}
