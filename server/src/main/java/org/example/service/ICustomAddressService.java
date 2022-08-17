package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.R;
import org.example.pojo.Address;

/**
 * @ClassName: ICustomAddressService
 * @Author: MaCongYi
 * @create: 2022-08-08 09:19
 * @Description: 顾客地址接口
 * @Version: 1.0
 */
public interface ICustomAddressService extends IService<Address> {
    //通过用户电话号码查询地址
    R CustomdellAddresss(Integer id);
    //地址保存
    R CustomSaveAddresss(Address address);
    //通过顾客id获取全部地址
    R getAllAddresss(Integer customerId);

    R getOneAddresss(Integer customerId, Integer addressId);
}
