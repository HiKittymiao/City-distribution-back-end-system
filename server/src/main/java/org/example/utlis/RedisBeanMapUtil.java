package org.example.utlis;

import cn.hutool.db.sql.Order;
import org.example.pojo.Orders;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: RedisBeanMapUtil
 * @Author: MaCongYi
 * @create: 2022-08-13 15:46
 * @Description:
 * @Version: 1.0
 */

@Component
public class RedisBeanMapUtil {
    //对象转Map
    public static <T> Map<String, Object> beanToMap(T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        Map<String, Object> map = new HashMap<>();

        beanMap.forEach((key, value) -> {
            map.put(String.valueOf(key), value);
        });
        return map;
    }

    public static <T> T mapToBean(Map<String, ?> map, Class<T> clazz)
            throws IllegalAccessException, InstantiationException {
        T bean = clazz.newInstance();
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (objList != null && objList.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0, size = objList.size(); i < size; i++) {
                bean = objList.get(i);
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

    public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps, Class<T> clazz)
            throws InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        if (maps != null && maps.size() > 0) {
            Map<String, ?> map = null;
            for (int i = 0, size = maps.size(); i < size; i++) {
                map = maps.get(i);
                T bean = mapToBean(map, clazz);
                list.add(bean);
            }
        }
        return list;
    }




    /**
     * 将bean转成map
     */
    public void parseMap(String key, HashOperations<String, Object, Object> redisHash, Object bean) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        //1. 获得所有的get方法
        List<Method> allGetMethod = getAllGetMethod(bean);
        //2. 遍历往redis中存入值
        for (Method m : allGetMethod) {
            //截取属性名
            String field = m.getName().substring(3);
            //激活方法得到值
            Object value = m.invoke(bean) + "";//加一个空串是为了将LocalDataTime转换成字符串
            //往redis里存这些字段
            redisHash.put(key, field, value);
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


    /**把Map转为对象
     * 把source转为target
     * @param source source
     * @param target target
     * @param <T> 返回值类型
     * @return 返回值Person person = JSONObject.parseObject(JSONObject.toJSONString(source), Person.class);
     * @throws Exception newInstance可能会抛出的异常
     */
    public static <T> T mapToObject(Map source,Class<T> target) throws Exception {
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


    public static Map mapToOrderse(Map<String,Object> map){

        Object o = map.get("payDate");

        return null;
    }
}
