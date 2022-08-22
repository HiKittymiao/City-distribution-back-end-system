package org.example.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.R;
import org.example.common.RespPageBean;
import org.example.dto.OrderConfirm;
import org.example.dto.OrderDetial;
import org.example.mapper.OrdersMapper;
import org.example.pojo.Custom;
import org.example.pojo.Orders;
import org.example.service.ICustomService;
import org.example.service.IOrdersService;
import org.example.service.IRiderService;
import org.example.utlis.DistanceUtil;
import org.example.utlis.OrderIidUtil;
import org.example.vo.PriceAndDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @Autowired
    private OrderIidUtil orderIidUtil;

    @Autowired
    private IOrdersService ordersService;


    @Override
    public String newOrder(OrderDetial o, PriceAndDistance pd) {
        Orders order = new Orders();
        String id = orderIidUtil.newid();
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
        order.setGoodsType(o.getGoodsType());
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


        long count = 0;
        String Tickid = "";
        //订单类型
        do {
            Integer idStyle = 1;
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");//设置日期格式(年-月-日-时-分-秒)
            String createTime = dateFormat.format(now);
            String secondTime = String.format("%0" + 5 + "d", (Integer.valueOf(createTime.substring(6, 8)) * 60 * 60
                    + Integer.valueOf(createTime.substring(8, 10)) * 60
                    + Integer.valueOf(createTime.substring(10, 12))));
            Tickid = idStyle + createTime.substring(0, 6) + secondTime;
            // 2.2.自增长

            String uuid = UUID.randomUUID().toString();
            // 从Redis获取锁
            Boolean flag = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 3, TimeUnit.SECONDS);
            if (flag) {
                // flag = true:表示拿到锁！ 执行业务逻辑
                count = (Long) stringRedisTemplate.opsForValue().increment("ID:" + Tickid);
                // 定义一个lua脚本
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

                // 创建对象
                DefaultRedisScript<Long> redisScript = new DefaultRedisScript();
                // 设置lua脚本
                redisScript.setScriptText(script);
                //设置lua脚本返回类型为Long
                redisScript.setResultType(Long.class);
                // redis调用lua脚本
                redisTemplate.execute(redisScript, Arrays.asList("lock"), uuid);
            } else {
                continue;
            }
        } while (count > 10000);
        String countStr = String.format("%0" + 4 + "d", count);
        Tickid = Tickid + countStr;
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
            redisTemplate.opsForHash().putAll("order:" + orderId, map);


            //redisTemplate.opsForHash().putAll("order:"+orderId,util.beanToMap(o));
            //订单内容redis设置过期时间2天
            redisTemplate.opsForValue().set("orders:" + orderId, null, 2, TimeUnit.DAYS);

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
    public List<Orders> getKillOrderDetail(Set ids) {
        ArrayList<Orders> orders = new ArrayList<>();
        ids.forEach((i) -> {
            orders.add((Orders) redisTemplate.opsForValue().get("no_pay:" + i));
        });
        return orders;
    }

    @Override
    public List<Orders> getAllKillOrderDetail() {
        Set kill_order = redisTemplate.opsForSet().members("kill_order");
        ArrayList<Orders> orders = new ArrayList<>();
        kill_order.forEach((i) -> {
            orders.add((Orders)BeanUtil.fillBeanWithMap(redisTemplate.opsForHash().entries("order:" + i),new Orders(),false));
        });
        return orders;
    }

    @Override
    public R killOrder(Integer rideId, Long orderId) {
        HashOperations ops = redisTemplate.opsForHash();
        if (!iRiderService.isRider(1)) {
            return R.error("骑手不存在");
        }
        Integer o = (Integer) ops.get("order:" + orderId, "rederId");
        if (o != null) {
            if (o == rideId) {
                return R.success("重复抢单");
            }
            return R.success("抢单失败");
        }
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.watch("order:" + orderId);
                operations.multi();
                //更新redis订单表
                LocalDateTime now = LocalDateTime.now();
                operations.opsForHash().put("order:" + orderId, "rederId", rideId);
                operations.opsForHash().put("order:" + orderId, "statue", 2); //ops.increment("order:" + id,"statue",1d);
                operations.opsForHash().put("order:" + orderId, "riderAcceptDate", now);
                return operations.exec();
            }
        });
        //移除kill_order集合的订单成功返回该订单值
        redisTemplate.opsForSet().remove("kill_order", orderId);
        //删除未付款订单
        redisTemplate.delete("no_pay:" + orderId);
        //将订单添加到骑手表
        ops.put("rider:" + rideId, orderId.toString(), 2);
        return R.success("抢单成功");
    }

    @Override
    public R cancelOrder(String orderId, String customer_id) {
        Custom c = (Custom) iCustomService.getById(customer_id);
        if (c == null) {
            return R.error("用户不存在");
        }
        if (c.getEnabled() == false) {
            return R.error("账号被冻结");
        }
        Orders o = (Orders) redisTemplate.opsForValue().get("no_pay:" + orderId);
        Map entries = redisTemplate.opsForHash().entries("order:" + orderId);
        if (o != null) {
            if (o.getStatue() == 0 && orderId.equals(o.getId().toString())) {
                o.setStatue(8);
                updateById(o);
                redisTemplate.delete("no_pay:" + orderId);
                return R.success("订单取消成功");
            }
            return R.success("非法操作");
        } else if (!entries.isEmpty()) {
            Orders order = (Orders) BeanUtil.fillBeanWithMap(entries, new Orders(), true);
            if (!order.getId().equals(orderId)) {
                return R.success("非法操作");
            }
            Integer statue = order.getStatue();
            if (statue == 1) {
                order.setStatue(8);
                updateById(order);
                redisTemplate.delete("no_pay:" + orderId);
                updateById(order);
                return R.success("订单取消成功");
            } else if (statue == 2) {
                order.setStatue(8);
                updateById(order);
                redisTemplate.delete("no_pay:" + orderId);
                updateById(order);
                return R.success("订单取消成功");
            } else {
                return R.success("订单正在派送，禁止取消");
            }
        }
        return R.success("禁止取消");

    }


    @Override
    public R arrivePlace(Integer rider_id, String id, Double x, Double y) {


        Integer rederId = (Integer) redisTemplate.opsForHash().get("order:" + id, "rederId");
        double ox = (double) redisTemplate.opsForHash().get("order:" + id, "sLongitude");
        double oy = (double) redisTemplate.opsForHash().get("order:" + id, "sLatitude");
        Object o = redisTemplate.opsForHash().get("order:" + id, "statue");

        //不是本骑手
        if (rederId != rider_id) {
            return R.error("非法操作");
        }
        Integer integer = Integer.valueOf(o.toString());
        //未到取件范围
        double distance2 = DistanceUtil.getDistance2(ox, oy, x, y);

        //System.out.println(distance2);
        //if (distance2 > 0.5d) {
        //    return R.success("请到取件地点再点击收货");
        //}
        //禁止重复操作
        if (integer != 2) {
            return R.success("禁止重复操作");
        }
        redisTemplate.opsForHash().put("order:" + id, "statue", 3);
        LocalDateTime dateTime = LocalDateTime.now();
        redisTemplate.opsForHash().put("order:" + id, "riderGetDate", dateTime);
        redisTemplate.opsForHash().put("rider:" + rider_id, id.toString(), 3);
        return R.success("骑手到达目指定的地等候顾客到达");
    }

    @Override
    public R confirmGoods(Integer rider_id, String order_id) {
        Object o = redisTemplate.opsForHash().get("order:" + order_id, "statue");
        Integer integer = Integer.valueOf(o.toString());
        if (integer != 3) {
            return R.success("禁止重复操作");
        }
        redisTemplate.opsForHash().put("order:" + order_id, "statue", 4);
        LocalDateTime dateTime = LocalDateTime.now();
        redisTemplate.opsForHash().put("order:" + order_id, "riderSendDate", dateTime);
        redisTemplate.opsForHash().put("rider:" + rider_id, order_id.toString(), 4);
        return R.success("成功取货");
    }

    @Override
    public R deliveriedGoods(Integer rider_id, String order_id, Double x, Double y) {
        Integer rederId = (Integer) redisTemplate.opsForHash().get("order:" + order_id, "rederId");
        //double ox = (double) redisTemplate.opsForHash().get("order:" + order_id, "rLongitude");
        //double oy = (double) redisTemplate.opsForHash().get("order:" + order_id, "rLatitude");
        Integer integer = (Integer) redisTemplate.opsForHash().get("order:" + order_id, "statue");
        //不是本骑手
        if (rederId != rider_id) {
            return R.error("非法操作");
        }

        //未到取件范围
        //double distance2 = DistanceUtil.getDistance2(ox, oy, x, y);
        //System.out.println(distance2);
        //if (distance2 > 0.5d) {
        //    return R.success("请到目的地再点击送达");
        //}


        if (integer != 4) {
            return R.success("禁止重复操作");
        }
        redisTemplate.opsForHash().put("order:" + order_id, "statue", 5);
        LocalDateTime dateTime = LocalDateTime.now();
        redisTemplate.opsForHash().put("order:" + order_id, "riderCompleteDate", dateTime);
        redisTemplate.opsForHash().put("rider:" + rider_id, order_id.toString(), 5);
        return R.success("物品已送达");
    }

    @Override
    public R qurryAllOrdersStatus(Integer riderId, Integer status, String order_id) {
        if(!order_id.isEmpty()){
            Map map = redisTemplate.opsForHash().entries("order:" + order_id);
            if (!map.isEmpty()) {
                Orders orders = BeanUtil.fillBeanWithMap(map, new Orders(), false);
                if (orders.getId().toString().equals(order_id) && orders.getRederId() == riderId) {
                    return R.success("成功", orders);
                }
                return R.success("非法操作订");
            }
            return R.success("订单不存在");
        }
        ArrayList<Orders> orders = new ArrayList<>();
        if (status == null) {
            Set keys = redisTemplate.opsForHash().keys("rider:" + riderId);
            Iterator<Long> it = keys.iterator();
            while (it.hasNext()) {
                //对myobject进行操作
                orders.add(BeanUtil.fillBeanWithMap(redisTemplate.opsForHash().entries("order:" + it.next()), new Orders(), false));
            }
            return R.success("返回一天内所以该骑手所以的订单状态", orders);
        }
        //返回一天的订单状态
        Map<String, Integer> entries = redisTemplate.opsForHash().entries("rider:" + riderId);
        entries.entrySet().removeIf(m -> m.getValue() != status);
        List<String> collect = entries.keySet().stream().collect(Collectors.toList());
        collect.forEach(k -> {
            orders.add(BeanUtil.fillBeanWithMap(redisTemplate.opsForHash().entries("order:" + k), new Orders(), false));
        });
        //System.out.println(orders);
        //entries.forEach((k, v) -> System.out.println(k + "-" + v));
        return R.success("返回一天内所以该骑手所以的订单状态", orders);
    }

    @Override
    public R getOrders(Integer customId, Integer pageNum, Integer pageSize) {
        RespPageBean respPageBean = new RespPageBean();
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id", customId);
        queryWrapper.orderByDesc("id");

        Page<Orders> page = new Page<Orders>(pageNum, pageSize);
        IPage<Orders> pageList = ordersService.page(page, queryWrapper);
        respPageBean.setTotal(pageList.getTotal());
        respPageBean.setAllOrderList(pageList.getRecords());

        if (pageList == null) {
            return R.error("未查询到订单");
        }
        return R.success("查询成功", respPageBean);
    }

    @Override
    public R getOneOrder(Integer customId, Long orderId) {
        Custom c = (Custom) iCustomService.getById(customId);
        if (c == null) {
            return R.error("用户不存在");
        }
        if (c.getEnabled() == false) {
            return R.error("账号被冻结");
        }
        Orders o = (Orders) redisTemplate.opsForValue().get("no_pay:" + orderId);
        if (o != null) {
            if (orderId.longValue()==o.getId().longValue() && o.getCustomerId() == customId) {
                return R.success("查询成功", o);
            }
            return R.success("非法操作订单或顾客id不存在");
        }
        Map map = redisTemplate.opsForHash().entries("order:" + orderId);
        if (!map.isEmpty()) {
            Orders orders = BeanUtil.fillBeanWithMap(map, new Orders(), false);
            if (orders.getId().longValue()==orderId.longValue() && orders.getCustomerId() == customId) {
                return R.success("成功", orders);
            }
            return R.success("非法操作订单或顾客id不存在");
        }
        Orders orders = this.getById(orderId);
        if (orders != null) {
            return R.success("成功", orders);
        }
        return R.success("没有该订单内容");

    }

    @Override
    public R confirmGoods(OrderConfirm orderConfirm) {
        Orders orders = new Orders();
        HashOperations ops = redisTemplate.opsForHash();
        Map entries = ops.entries("order:" + orderConfirm.getOrderId());
        orders = BeanUtil.fillBeanWithMap(entries,new Orders(),false);
        //entries.forEach((k,v)-> System.out.println(k+"     "+v));
        if (!entries.isEmpty()) {
            orders = (Orders) BeanUtil.fillBeanWithMap(entries, new Orders(), false);
            //System.out.println(orders);
            orders.setUserEvaluate(orderConfirm.getUserEvaluate());
            orders.setUserScore(orderConfirm.getUserScore());
            orders.setStatue(6);
            updateById(orders);
            redisTemplate.delete("order:" + orderConfirm.getOrderId());
            return R.success("收货成功订单完成");
        }
        Orders id = getById(orderConfirm.getOrderId());
        id.setUserEvaluate(orderConfirm.getUserEvaluate());
        id.setUserScore(orderConfirm.getUserScore());
        id.setStatue(6);

        boolean b = updateById(id);
        if (b) {
            return R.success("收货成功订单完成");
        }
        return R.error("数据库未查询到该订单");
    }
}
