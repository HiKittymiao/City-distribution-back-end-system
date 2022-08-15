package org.example.service;

import io.swagger.models.auth.In;
import org.example.common.R;
import org.example.dto.OrderDetial;
import org.example.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.vo.PriceAndDistance;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author mcy
 * @since 2022-08-08
 */
public interface IOrdersService extends IService<Orders> {

    /*
    根据订单内容计算价格和距离
     */
    PriceAndDistance calculateDistanceAndPrice(OrderDetial distance);

    /**
     * @param orderDetial
     * @return org.example.common.R
     * @author 聪懿
     * @date 2022/8/8 21:10
     * @description 扣费成功后将dto订单信息转换为Order实体类保存到redis
     **/
    String newOrder(OrderDetial orderDetial, PriceAndDistance pd);

    /**
     * @return java.lang.String
     * @author 聪懿
     * @date 2022/8/3 19:17
     * @description 返回一个订单号
     **/
    String newId();
    /**
     * @param orderId
     * @param customer_id
     * @return java.lang.Boolean
     * @author 聪懿
     * @date 2022/8/10 18:59
     * @description 判断是否付款成功
     **/
    Boolean PayOrder(Long orderId, Integer customer_id);
    /**
     * @param
     * @return java.util.Set
     * @author 聪懿
     * @date 2022/8/10 18:59
     * @description 查询可以抢单列表
     **/
    Set getKillOrder();
    /**
     * @param id
     * @return java.util.List<org.example.pojo.Orders>
     * @author 聪懿
     * @date 2022/8/10 18:59
     * @description 根据订单号返回可抢订单内容
     **/
    List<Orders> getKillOrderDetail(Set id);
    /**
     * @param
     * @return java.util.List<org.example.pojo.Orders>
     * @author 聪懿
     * @date 2022/8/10 17:07
     * @description 直接返回可枪订单内容
     **/
    List<Orders> getAllKillOrderDetail();

    /**
     * @param 骑手id，要枪的订单id
     * @return org.example.common.R
     * @author 聪懿
     * @date 2022/8/10 18:29
     * @description 抢单成功后发送该订单的最新状态
     **/
    R killOrder(Integer RiderId, Long id);

    /**
     * @param id
     * @param customer_id
     * @return void
     * @author 聪懿
     * @date 2022/8/13 19:32
     * @description 未付款取消订单
     **/
    R cancelOrder(Long id, String customer_id);

    /**
     * @param 订单号
     * @return org.example.common.R
     * @author 聪懿
     * @date 2022/8/14 21:00
     * @description 骑手确定取货
     **/
    R confirmGoods(Long orderId);

}
