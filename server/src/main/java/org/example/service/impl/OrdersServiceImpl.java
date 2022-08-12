package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.R;
import org.example.dto.OrderDetial;
import org.example.mapper.OrdersMapper;
import org.example.pojo.Orders;
import org.example.service.ICustomService;
import org.example.service.IOrdersService;
import org.example.service.IRiderService;
import org.example.utlis.DistanceUtil;
import org.example.vo.PriceAndDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
        System.out.println(o);
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
            //更新redis设置过期时间2天
            redisTemplate.opsForValue().set("payed:" + orderId, o, 48, TimeUnit.HOURS);
            //要抢单的订单进入Redis的集合
            redisTemplate.opsForSet().add("kill_order", o.getId());
        }
        System.out.println(o.toString());
        return aBoolean;
    }

    @Override
    public Set getKillOrder() {
        Set kill_order = redisTemplate.opsForSet().members("kill_order");
        return kill_order;
    }

    @Override
    public List<Orders> getKillOrderDetail(Set ids) {
        ArrayList<Orders> orders = new ArrayList<>();
        ids.forEach((i) -> {
            orders.add((Orders) redisTemplate.opsForValue().get("payed:" + i));
        });
        return orders;
    }

    @Override
    public List<Orders> getAllKillOrderDetail() {
        Set kill_order = redisTemplate.opsForSet().members("kill_order");
        ArrayList<Orders> orders = new ArrayList<>();
        kill_order.forEach((i) -> {
            orders.add((Orders) redisTemplate.opsForValue().get("payed:" + i));
        });
        return orders;
    }

    @Override
    public R killOrder(Integer rideId, Long id) {
        if (!iRiderService.isRider(1)) {
            return R.error("骑手不存在");
        }
        //移除kill_order集合的订单成功返回该订单值
        Long kill_order = -1l;
        kill_order = redisTemplate.opsForSet().remove("kill_order", id);
        System.out.println(kill_order);
        if (kill_order == 1) {
            Orders order = (Orders)redisTemplate.opsForValue().get("payed:" +id);
            redisTemplate.delete("payed:" +id);
            order.setRederId(rideId);
            LocalDateTime dateTime = LocalDateTime.now();
            order.setRiderAcceptDate(dateTime);
            order.setStatue(2);
            order.setUpdateTime(dateTime);
            redisTemplate.opsForHash().put("Rider:"+rideId,id.toString(), order);
            //根据骑手id生成id为空间的散列然后删除支付过的订单
            redisTemplate.delete("payed:" + id);
            return R.success("抢单成功", order);
        }

        return R.success("抢单不成功");
    }
}
