package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.apache.logging.log4j.util.Strings;
import org.example.common.R;
import org.example.pojo.Admin;
import org.example.pojo.Custom;
import org.example.mapper.CustomMapper;
import org.example.service.ICustomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cbb
 * @since 2022-08-06
 */
@Slf4j
@Service
public class CustomServiceImpl extends ServiceImpl<CustomMapper, Custom> implements ICustomService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ICustomService iCustomService;

    @Override
    public R GetCustomId(String phone) {
        QueryWrapper<Custom> qw = new QueryWrapper<>();
        qw.select("id").eq("phone",phone);
        Custom one = getOne(qw);
        if (one==null){
            return R.error("用户不存在");
        }
        return R.success("查询成功",one.getId());
    }

    @Override
    public Admin getAdminByUserName(String username) {
        Admin admin = new Admin();
        Custom custom = this.getOne(new QueryWrapper<Custom>().eq("phone",username));
        if (custom!=null){
            admin.setAdminId(custom.getId());
            admin.setUserName(custom.getPhone());
            admin.setPassword(custom.getPassword());
            admin.setAdminType(2);
        }
        else {
            return null;
        }
        return admin;
    }

    /**
     * @param custom
     * @return R
     * @author 聪懿
     * @date 2022/8/7 17:08
     * @description 用户注册
     **/
    @Override
    public R userRegister(Custom custom) {
        custom.setEnabled(true);
        custom.setPassword(passwordEncoder.encode(custom.getPassword()));
        custom.setMoney(0.0);
        //通过电话号码判断是否已经注册
        if (Strings.isEmpty(custom.getPhone())) {
            return R.error("电话号码为空");
        }

        Custom custom1=getOne(new QueryWrapper<Custom>().eq("phone",custom.getPhone()));
        if (custom1!=null){
            return R.error("用户已被注册");
        }
        return R.success("注册成功",save(custom));
    }

    @Override
    public double getMoney(Integer id) {
        QueryWrapper<Custom> qw = new QueryWrapper<>();
        //根据电话号码查询余额
        qw.select("money","enabled").eq("id",id);
        Custom custom = getOne(qw);
        if (custom== null) {
            log.info("用户不存");
        }else if(!custom.getEnabled()){
            log.info("用户被禁用");
        }
        return custom.getMoney();
    }

    @Override
    public Boolean ruduceMoney(Integer id, Double price) {
        double money = 0;
        try {
            money = iCustomService.getMoney(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (money<price &&price>0){return false;}
        money=money-price;
        UpdateWrapper<Custom> qw = new UpdateWrapper<>();
        qw.eq("id",id).set("money",money);
        update(null,qw);
        return true;
    }
}
