package org.example.controller;


import cn.hutool.db.sql.Order;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.R;
import org.example.dto.OrderDetial;
import org.example.service.ICustomService;
import org.example.service.IOrdersService;
import org.example.vo.PriceAndDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author mcy
 * @since 2022-08-08
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private IOrdersService iOrdersService;

    @Autowired
    private ICustomService iCustomService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/distance")
    @ApiOperation(value = "计算距离")
    public R calculateDistance(@RequestBody OrderDetial o) {
        PriceAndDistance pd = iOrdersService.calculateDistanceAndPrice(o);
        return R.success("距离和价格", pd);
    }

    /**
     * @param orderDetial
     * @return org.example.common.R
     * @author 聪懿
     * @date 2022/8/9 14:24
     * @description 用户点击下单，生成订单将订单保存到数据库和Redis，订单状态默认为
     * 0（下单未支付）返回订单号
     **/
    @PostMapping("/newOrder")
    @ApiOperation(value = "下单未支付")
    public R newOrder(@RequestBody OrderDetial orderDetial) {
        //计算价格
        PriceAndDistance pd = iOrdersService.calculateDistanceAndPrice(orderDetial);
        //生成订单返回订单号
        String s = iOrdersService.newOrder(orderDetial, pd);
        HashMap<String, String> map = new HashMap<>();
        map.put("onlytime", redisTemplate.getExpire("no_pay:" + s).toString());
        map.put("distance", pd.getDistance().toString());
        map.put("price", pd.getPrice().toString());
        map.put("sn", s);
        map.put("goodsType",orderDetial.getOrderType());
        return R.success("下单成功请付款", map);
    }


    @PostMapping("/payOrder/{OrderId}/{customer_id}")
    @ApiOperation(value = "付款")
    public R PayOrder(@PathVariable String OrderId, String customer_id) {
        Boolean aBoolean = iOrdersService.PayOrder(Long.valueOf(OrderId), Integer.valueOf(customer_id));
        if (!aBoolean) {
            return R.error("订单不存在或者顾客被冻结或余额不足");
        }
        return R.success("支付成功");
    }

    @PostMapping("/cancelOrder/{orderId}/{customer_id}")
    @ApiOperation(value = "取消订单")
    public R cancelOrder(@PathVariable String orderId, String customer_id) {
        if (orderId == "" || customer_id == "") {
            return R.error("订单密码格式错误");
        }
        return iOrdersService.cancelOrder(Long.valueOf(orderId), customer_id);
    }



}
