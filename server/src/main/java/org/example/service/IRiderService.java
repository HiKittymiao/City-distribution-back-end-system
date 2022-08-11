package org.example.service;

import io.swagger.models.auth.In;
import org.example.common.R;
import org.example.pojo.Admin;
import org.example.pojo.Rider;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cbb
 * @since 2022-08-06
 */
public interface IRiderService extends IService<Rider> {

    //根据用户名 查询用户信息
    Admin getAdminByUserName(String username);

    R riderRegister(Rider rider);

    /**
     * @param id
     * @return java.lang.Boolean
     * @author 聪懿
     * @date 2022/8/10 18:34
     * @description 根据骑手id查询是否可用
     **/
    Boolean isRider(Integer id);
}
