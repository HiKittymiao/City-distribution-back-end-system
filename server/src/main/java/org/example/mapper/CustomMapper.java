package org.example.mapper;

import org.example.pojo.Custom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.vo.PriceAndDistance;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cbb
 * @since 2022-08-06
 */
public interface CustomMapper extends BaseMapper<Custom> {

    //查询顾客表扣除费用
    Boolean deductMoneny(PriceAndDistance priceAndDistance);
}
