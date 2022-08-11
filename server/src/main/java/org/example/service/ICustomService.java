package org.example.service;

import io.swagger.models.auth.In;
import org.example.common.R;
import org.example.pojo.Admin;
import org.example.pojo.Custom;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cbb
 * @since 2022-08-06
 */
public interface ICustomService extends IService<Custom> {

     R  GetCustomId(String phone);

    Admin getAdminByUserName(String username);

    R userRegister(Custom custom);

    //根据顾客Id获取顾客钱包余额
    double getMoney(Integer customId);


    //根据顾客Id和金额进行扣费返回是否成功
    Boolean ruduceMoney(Integer customId , Double price);
}
