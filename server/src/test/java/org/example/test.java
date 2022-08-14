package org.example;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.pojo.Admin;
import org.example.pojo.Orders;
import org.example.rabbitmq.MQSender;
import org.example.service.ICustomAddressService;
import org.example.service.ICustomService;
import org.example.service.IOrdersService;
import org.example.service.IRiderService;
import org.example.utlis.RedisBeanMapUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @ClassName: test
 * @Author: MaCongYi
 * @create: 2022-08-08 19:35
 * @Description:
 * @Version: 1.0
 */

@Slf4j
@SpringBootTest
public class test {

    @Autowired
    private ICustomService iCustomService;
    @Autowired
    private IOrdersService iOrdersService;
    @Autowired
    private ICustomAddressService iCustomAddressService;
    @Autowired
    private IRiderService iRiderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired

    @Test
    public void fda(){
        //System.out.println(iCustomService.getMoney(2));
        //System.out.println(iOrdersService.newId());
      //
      //  //输出当前时间
      //  LocalDateTime dateTime = LocalDateTime.now();
      //  System.out.println(dateTime);
    }

    @Test
    public void fdasg(){
        iOrdersService.PayOrder(1220810556320001l,1);
    }

    @Test
    public void fdsafda(){
        //查询可以抢单列表
        Set killOrder = iOrdersService.getKillOrder();
        killOrder.forEach(System.out::println);
    }
    @Test
    public void fdsaf(){
        List<Map<String,Object>> killOrderDetail = iOrdersService.getKillOrderDetail(iOrdersService.getKillOrder());
        killOrderDetail.stream().forEach(System.out::println);
    }

    @Test
    public void fdsafgs(){
        Boolean rider = iRiderService.isRider(1);
        System.out.println(rider);
    }

    @Test
    public void fdsag(){
                R killOrder = iOrdersService.killOrder(1,1220810787300001l);
        System.out.println(killOrder);
    }
    @Test
    public void fdsagd(){
        iCustomAddressService.CustomdellAddresss(3);
    }


    @Test
    public void TestRedis() throws InstantiationException, IllegalAccessException {
        //拿到一个JavaBean
        Admin admin = new Admin();
        admin.setAdminId(1);
        admin.setAdminType(2);
        admin.setPassword("fdsfds231432");
        admin.setPhone("12321");
        admin.setUserName("cbb");
        //设置key
        String key="umbrella:"+"张三3";
        //String key2="umbrella:"+"李四oR83j4kkq2CyvVmuxl6znKbrWi2A";
        //String key3="umbrella:"+"王五oR83j4kkq2CyvVmuxl6znKbrWi2A";
        //拿到redis操作对象
        HashOperations<String, Object, Object> redisHash = redisTemplate.opsForHash();
        try {
            //存入redis中
            parseMap(key,redisHash,admin);
            //parseMap(key2,redisHash,umbrellaBorrow);
            //parseMap(key3,redisHash,umbrellaBorrow);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    /**
     * 将bean转成map
     */
    private void parseMap(String key,HashOperations<String, Object, Object> redisHash, Object bean) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        //1. 获得所有的get方法
        List<Method> allGetMethod = getAllGetMethod(bean);
        //2. 遍历往redis中存入值
        for(Method m : allGetMethod){
            //截取属性名
            String field = m.getName().substring(3);
            //激活方法得到值
            Object value = m.invoke(bean)+"";//加一个空串是为了将LocalDataTime转换成字符串
            //往redis里存这些字段
            redisHash.put(key,field,value);
        }

    }

    /**
     * 取出所有的get方法
     *
     * @param bean 指定实例
     * @return 返回get方法的集合
     */
    private List<Method> getAllGetMethod(Object bean) {
        List<Method> getMethods = new ArrayList<>();
        Method[] methods = bean.getClass().getMethods();
        for (Method m : methods) {
            if (m.getName().startsWith("get")) {
                getMethods.add(m);
            }
        }
        return getMethods;
    }
    @Test
    public void fd(){
        Admin admin = new Admin();
        admin.setAdminId(1);
        admin.setAdminType(2);
        admin.setPassword("fdsfds231432");
        admin.setPhone("12321");
        String key="umbrella:"+"张三1";
        Map<String, Object> map = RedisBeanMapUtil.beanToMap(admin);
        map.forEach((k,v)->redisTemplate.opsForHash().put(key,k,String.valueOf(v)));

    }

    /**
     * @param
     * @return void
     * @author 聪懿
     * @date 2022/8/13 16:43
     * @description 先将对象转为map通过putall提交到redis,redis的hash转对象，
     *              先转为map对象再转为bean对象
     **/
    @Test
    public void fdd() throws Exception {
        //admin.setAdminId(1);
        //admin.setAdminType(2);
        //admin.setPassword("fdsfds231432");
        //admin.setPhone("12321");
        //Map<String, Object> dataMap = objectToMap(admin);
        String key="umbrella:"+"张三2";
        //redisTemplate.opsForHash().putAll(key,dataMap);

        Map<Object,Object> map =redisTemplate.opsForHash().entries(key);
        //map.forEach((k,v)-> System.out.println(k+","+v));
        Admin person = mapToObj(map,Admin.class);
        System.out.println(person);
        //Admin admin = RedisBeanMapUtil
    }
    public static Map<String, Object> objectToMap(Object object){
        Map<String,Object> dataMap = new HashMap<>();
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                dataMap.put(field.getName(),field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return dataMap;
    }
    /**把Map转为对象
     * 把source转为target
     * @param source source
     * @param target target
     * @param <T> 返回值类型
     * @return 返回值Person person = JSONObject.parseObject(JSONObject.toJSONString(source), Person.class);
     * @throws Exception newInstance可能会抛出的异常
     */
    public static <T> T mapToObj(Map source,Class<T> target) throws Exception {
        Field[] fields = target.getDeclaredFields();
        T o = target.newInstance();
        for(Field field:fields){
            Object val;
            if((val=source.get(field.getName()))!=null){
                field.setAccessible(true);
                field.set(o,val);
            }
        }
        return o;
    }
    @Test
    //通过map更新Redis
    public void fdsagdas(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("adminId",2);
        map.put("userName","mcy");
        redisTemplate.opsForHash().putAll("umbrella:张三2",map);
    }

    @Autowired
    private MQSender mqSender;
    @Resource
    private RedisBeanMapUtil redisBeanMapUtil;

    @Test
    public void mq(){
        mqSender.sendSeckillMessage("520");
    }
    @Test
    //map转对象成功
    //使用最原初的方法将时间一个个手动转为localdatetime对象
    public void getRedisHash(){
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        Map<String,Object> map = redisTemplate.opsForHash().entries("order:1220814582500001");
        map.forEach((k,v)->{
            System.out.println(k+"--"+v);
        });
        //Date date = new Date();
        //String o = map.get("payDate").toString();
        //String payDate = map.get("payDate").toString().replace("T"," ").replace("\"","");
        //LocalDateTime dateTime=LocalDateTime.parse(payDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        //System.out.println(dateTime);
        Orders orders = BeanUtil.fillBeanWithMap (map,new Orders(), true);
        orders.setCreateTime(LocalDateTime.parse(map.get("payDate").toString().replace("T"," ").replace("\"",""), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        orders.setCreateTime(LocalDateTime.parse(map.get("riderAcceptDate").toString().replace("T"," ").replace("\"",""), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        System.out.println(orders);
    }

}
