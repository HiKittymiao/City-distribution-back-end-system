package org.example.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.R;
import org.example.dto.OrderDetial;
import org.example.mapper.OrdersMapper;
import org.example.pojo.Custom;
import org.example.pojo.Orders;
import org.example.service.ICustomService;
import org.example.service.IOrdersService;
import org.example.service.IRiderService;
import org.example.utlis.DistanceUtil;
import org.example.utlis.RedisBeanMapUtil;
import org.example.vo.PriceAndDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author mcy
 * @since 2022-08-08
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
     RedisTemplate redisTemplate;
    @Autowired
    ICustomService iCustomService;
    @Autowired
    private IRiderService iRiderService;
    @Resource
    private RedisBeanMapUtil util;



    @Override
    public String newOrder(OrderDetial o, PriceAndDistance pd) {
        Orders order = new Orders();
        String id = newId();
        order.setId(Long.valueOf(id));
        order.setAddressId(5);
        order.setSenderName(o.getSenderName());
        order.setAddresseeName(o.getAddresseeName());
        order.setAddresseePhone(o.getAddresseePhone());
        order.setCustomerId(o.getCustomerId());
        order.setGoodsDescribe(o.getGoodsDescribe());
        order.setSLongitude(o.getSLongitude());
        order.setSLatitude(o.getSLatitude());
        order.setRLongitude(o.getRLongitude());
        order.setRLatitude(o.getRLatitude());
        order.setSenderAddress(o.getSenderAddress());
        order.setSenderPhone(o.getSenderPhone());
        order.setSenderName(o.getSenderName());
        order.setAddresseeName(o.getAddresseeName());
        order.setAddresseePhone(o.getAddresseePhone());
        order.setAddresseeAddress(o.getAddresseeAddress());
        order.setPrice(pd.getPrice());
        order.setDistance(pd.getDistance());
        order.setStatue(0);
        order.setGoodsWeight(o.getGoodsWeight());
        //输出当前时间
        LocalDateTime dateTime = LocalDateTime.now();
        order.setCreateTime(dateTime);
        order.setOrderType(o.getOrderType());
        order.setEstimatedTime(dateTime.plusMinutes(30));
        redisTemplate.opsForValue().set("no_pay:" + id, order, 30, TimeUnit.MINUTES);
        save(order);
        return id;
    }

    @Override
    public PriceAndDistance calculateDistanceAndPrice(OrderDetial orderDetial) {
        PriceAndDistance priceAndDistance = DistanceUtil.getDistance(orderDetial);
        return priceAndDistance;
    }

    @Override
    public String newId() {
        long count = -1;
        String Tickid = new String();
        do {
            //订单类型
            Integer idStyle = 1;
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");//设置日期格式(年-月-日-时-分-秒)
            String createTime = dateFormat.format(now);
            String secondTime = String.format("%0" + 5 + "d", (Integer.valueOf(createTime.substring(6, 8)) * 60 * 60
                    + Integer.valueOf(createTime.substring(8, 10)) * 60
                    + Integer.valueOf(createTime.substring(10, 12))));
            Tickid = idStyle + createTime.substring(0, 6) + secondTime;
            // 2.2.自增长
            count = stringRedisTemplate.opsForValue().increment("ID:" + Tickid);
            if (count < 0) {
                continue;
            }
            String countStr = String.format("%0" + 4 + "d", count);
            Tickid = Tickid + countStr;
        } while (count > 10000);
        return Tickid;
    }


    @Override
    public Boolean PayOrder(Long orderId, Integer customer_id) {

        //通过订单号查询Redis得到订单实体类
        Orders o = (Orders) redisTemplate.opsForValue().get("no_pay:" + orderId);
        //System.out.println(o);
        if (o == null) {
            return false;
        }
        //根据顾客Id和金额进行扣费返回是否成功
        System.out.println(o.getPrice());
        Boolean aBoolean = iCustomService.ruduceMoney(customer_id, o.getPrice());
        if (aBoolean) {
            //删除未付款订单
            redisTemplate.delete("no_pay:" + orderId);
            //订单状态设置为1付款成功还没骑手接单
            o.setStatue(1);
            LocalDateTime dateTime = LocalDateTime.now();
            o.setPayDate(dateTime);
            //更新数据库
            updateById(o);
            //保存订单内容到Redis的hash
            //HashOperations<String,Object,Object> hashOperations = redisTemplate.opsForHash();
            //try {
            //    util.parseMap("order:"+orderId,hashOperations,o);
            //} catch (Exception e) {
            //    e.printStackTrace();
            //}

            Map<String, Object> map = BeanUtil.beanToMap(o);
            redisTemplate.opsForHash().putAll("order:"+orderId,map);


            //redisTemplate.opsForHash().putAll("order:"+orderId,util.beanToMap(o));
            //订单内容redis设置过期时间2天
            redisTemplate.expire("order:"+orderId,2,TimeUnit.DAYS);

            //要抢单的订单进入Redis的集合
            redisTemplate.opsForSet().add("kill_order", o.getId());
        }
        return aBoolean;
    }

    @Override
    public Set getKillOrder() {
        Set kill_order = redisTemplate.opsForSet().members("kill_order");
        return kill_order;
    }

    @Override
    public List<Map<String,Object>> getKillOrderDetail(Set ids) {
        ArrayList<Map<String,Object>> orders = new ArrayList<>();
        ids.forEach((i) -> {
            orders.add( redisTemplate.opsForHash().entries("order:" + i));
        });
        return orders;
    }

    @Override
    public List<Orders> getAllKillOrderDetail() {
        Set kill_order = redisTemplate.opsForSet().members("kill_order");
        ArrayList<Orders> orders = new ArrayList<>();
        kill_order.forEach((i) -> {
            orders.add((Orders) redisTemplate.opsForValue().get("order:" + i));
        });
        return orders;
    }

    @Override
    public R killOrder(Integer rideId, Long orderId) {
        HashOperations ops = redisTemplate.opsForHash();

        if (!iRiderService.isRider(1)) {
            return R.error("骑手不存在");
        }
        //移除kill_order集合的订单成功返回该订单值
        Long kill_order = -1l;
        kill_order = redisTemplate.opsForSet().remove("kill_order", orderId);
        System.out.println(kill_order);
        if (kill_order == 1) {
            //Orders order = (Orders)redisTemplate.opsForValue().get("payed:" +orderId);
            //redisTemplate.delete("payed:" +orderId);
            //order.setRederId(rideId);
            //LocalDateTime dateTime = LocalDateTime.now();
            //order.setRiderAcceptDate(dateTime);
            //order.setStatue(2);
            //order.setUpdateTime(dateTime);
            //redisTemplate.opsForHash().put("Rider:"+rideId,orderId.toString(), order);
            ////根据骑手id生成id为空间的散列然后删除支付过的订单
            //redisTemplate.delete("payed:" + orderId);
            //更新redis订单表
            LocalDateTime now = LocalDateTime.now();
            String id = String.valueOf(orderId);
            ops.put("order:" + id,"statue",2);
            ops.put("order:" + id,"riderAcceptDate",now);

            //将订单添加到骑手表
            ops.put("rider:"+rideId, id,2);

            Map entries = ops.entries("order" + id);
            return R.success("抢单成功", id);
        }
        return R.success("抢单不成功");
    }

    @Override
    public R cancelOrder(Long id, String customer_id) {
        Custom c = iCustomService.getById(customer_id);
        if(c==null){return R.error("用户不存在");}
        if(c.getEnabled()==false){return R.error("账号被冻结");}
        return R.success("订单取消成功");
    }

    @Override
    public R confirmGoods(Long id) {
        redisTemplate.opsForHash().put("order:"+id,"statue",2);
        LocalDateTime dateTime = LocalDateTime.now();
        redisTemplate.opsForHash().put("order:"+id,"riderGetDate",dateTime);
        return R.success("成功取货");
    }
}
