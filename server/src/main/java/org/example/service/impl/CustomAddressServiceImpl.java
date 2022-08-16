package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.R;
import org.example.mapper.AddressMapper;
import org.example.pojo.Address;
import org.example.service.ICustomAddressService;
import org.springframework.stereotype.Service;

/**
 * @ClassName: CustomAddressServiceImpl
 * @Author: MaCongYi
 * @create: 2022-08-08 09:29
 * @Description: 顾客地址
 * @Version: 1.0
 */
@Service
public class CustomAddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements ICustomAddressService  {
    @Override
    public R CustomdellAddresss(Integer id) {
        if (id==null) {
            R.error("id不能为空");
        }
        QueryWrapper<Object> UpdateWrapper = new QueryWrapper<>();
        Address address = new Address();
        address.setDelFlag(true);
        address.setId(id);
        if (updateById(address)!=true) {
            R.error("删除失败");
        }
        return R.success("删除成功");
    }

    @Override
    public R CustomSaveAddresss(Address address) {
        address.setDelFlag(false);
        if (save(address)!=true){
            return R.error("添加失败");
        }
        return R.success("添加成功");
    }

    @Override
    public R getAllAddresss(Integer customerId) {
        if (customerId==null) {
            return R.error("顾客id为空");
        }
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id",customerId).eq("del_flag",false);
        return R.success("全部地址查询成功",this.list(queryWrapper));

    }
}
