package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Select;
import org.example.common.R;
import org.example.pojo.Admin;
import org.example.pojo.Rider;
import org.example.mapper.RiderMapper;
import org.example.service.IRiderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cbb
 * @since 2022-08-06
 */
@Service
public class RiderServiceImpl extends ServiceImpl<RiderMapper, Rider> implements IRiderService {



    @Override
    public Admin getAdminByUserName(String username) {
        Admin admin = new Admin();
        Rider rider = this.getOne(new QueryWrapper<Rider>().eq("phone",username));
        if (rider!=null){
            admin.setAdminId(rider.getId());
            admin.setUserName(rider.getPhone());
            admin.setPassword(rider.getPassword());
            admin.setAdminType(1);
        }
        else {
            return null;
        }
        return admin;
    }


    @Override
    public R riderRegister(Rider rider) {
        QueryWrapper<Rider> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",rider.getPhone());
        if(getOne(queryWrapper)!=null){
            return R.error("该电话号码已注册");
        }
        return null;
    }

    @Override
    public Boolean isRider(Integer id) {
        QueryWrapper<Rider> qw = new QueryWrapper<>();
        qw.select().eq("id",id).eq("enabled",1);
        Rider rider = getOne(qw);
        return rider!=null;
    }
}
