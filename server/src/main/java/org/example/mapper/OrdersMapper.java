package org.example.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.pojo.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author mcy
 * @since 2022-08-08
 */
public interface OrdersMapper extends BaseMapper<Orders> {


    List<Orders> getYueOrdersNumber(@Param("rid") String rider_id, @Param("dateChu") String dateChu, @Param("dateMo") String dateMo);
}
