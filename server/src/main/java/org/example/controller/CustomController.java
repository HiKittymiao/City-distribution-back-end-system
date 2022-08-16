package org.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.example.common.R;
import org.example.pojo.Custom;
import org.example.service.ICustomService;
import org.example.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mcy
 * @since 2022-08-07
 */
@Api(tags = "CustomController")
@RestController
@RequestMapping("/custom")
public class CustomController {

    @Autowired
    private ICustomService iCustomService;
    @Autowired
    private IOrdersService iOrdersService;

    @ApiOperation(value = "顾客注册")
    @PostMapping("/userRegister")
    public R userRegister(@RequestBody Custom custom) {

        return iCustomService.userRegister(custom);
    }

    /**
     * @param phone
     * @return org.example.common.R
     * @author 聪懿
     * @date 2022/8/10 15:52
     * @description 通过电话好码查询顾客ID
     **/
    @ApiOperation(value = "通过电话好码查询顾客ID")
    @GetMapping("/userGetId")
    public R uerGetCustomId(String phone) {
        return iCustomService.GetCustomId(phone);
    }

    @ApiOperation(value = "点击传 头像 跟 名字  获取所有 当前客户信息")
    @GetMapping("/getCustom")
    public R getCustom(@RequestParam("avatarUrl") String avatarUrl, @RequestParam("nickName") String nickName, @RequestParam("phone") String phone) {

        Custom custom = iCustomService.getOne(new LambdaQueryWrapper<Custom>().eq(Custom::getPhone, phone));
        custom.setName(nickName);
        custom.setUserFace(avatarUrl);
        boolean b = iCustomService.updateById(custom);
        return R.success("成功", custom);
    }

    @ApiOperation(value = "根据id查询顾客信息")
    @GetMapping("/queryById")
    public R getCustomById(Integer id) {

        Custom byId = iCustomService.getById(id);
        if (byId != null) {
            return R.success("查询成功", byId);

        }
        return R.error("未知错误");

    }

    @ApiOperation(value = "根据手机号码查客户信息")
    @GetMapping("/queryByPhone")
    public R queryByPhone(@RequestParam("phone") String phone) {

        Custom one = iCustomService.getOne(new LambdaQueryWrapper<Custom>().eq(Custom::getPhone, phone));
        if (one != null) {
            return R.success("查询成功", one);
        }

        return R.error("查询失败");
    }


    @ApiOperation(value = "更新客户信息")
    @PutMapping("/update")
    public R update(@RequestBody Custom custom) {
        double money = custom.getMoney();
        BigDecimal two = new BigDecimal(money);
        money = two.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        custom.setMoney(money);
        System.out.println(money);
        boolean b = iCustomService.update(custom, new LambdaUpdateWrapper<Custom>().eq(Custom::getPhone, custom.getPhone()));
        if (b) {
            return R.success("更新成功");
        }
        return R.error("更新失败");
    }

    @ApiOperation(value = "查询用户订单")
    @GetMapping("/getOrders/{customId}")
    public R getOrdersStatue(@PathVariable Integer customId) {
        return iOrdersService.getOrders(customId);
    }


}




