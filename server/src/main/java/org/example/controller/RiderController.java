package org.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.R;
import org.example.pojo.Rider;
import org.example.service.IOrdersService;
import org.example.service.IRiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 前端控制器
 *
 * @author cbb
 * @since 2022-08-06
 */
@RestController
@RequestMapping("/rider")
@Api(tags = "RiderController")
public class RiderController {

    @Autowired
    private IRiderService iRiderService;
    @Autowired
    private IOrdersService iOrdersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * @param rider
     * @return org.example.common.R
     * @author 聪懿
     * @date 2022/8/7 21:13
     * @description TODO
     **/
    //@ApiOperation(value = "骑手注册")
    //@PostMapping("/riderRegister")
    //public R riderRegister(@RequestBody Rider rider){
    //    return iRiderService.riderRegister(rider);
    //}


    /**
     * @param
     * @return org.example.common.R
     * @author 聪懿
     * @date 2022/8/10 17:02
     * @description TODO
     **/
    @ApiOperation(value = "骑手查询可以抢单id列表")
    @GetMapping("/getKillOrder")
    public R getKillOrder() {
        return R.success("", iOrdersService.getKillOrder());
    }


    /**
     * @param id
     * @return org.example.common.R
     * @author 聪懿
     * @date 2022/8/10 17:09
     * @description TODO
     **/
    @ApiOperation(value = "骑手通过id集合查询可抢单的详细内容")
    @GetMapping("/getOneOrder")
    public R getKillOrderDetail(@RequestParam("id") String id) {
        return R.success("", iOrdersService.getKillOrderDetail(id));
    }

    /**
     * @param
     * @return org.example.common.R
     * @author 聪懿
     * @date 2022/8/10 17:10
     * @description TODO
     **/
    @ApiOperation(value = "直接返回可枪订单内容")
    @GetMapping("/getAllKillOrderDetail")
    public R getAllKillOrderDetail() {
        return R.success("返回可枪订单内容", iOrdersService.getAllKillOrderDetail());
    }

    @ApiOperation(value = "骑手抢单")
    @PostMapping("/killOrder/{rider_id}/{order_id}")
    public R killOrders(@PathVariable Integer rider_id, @PathVariable Long order_id) {
        return iOrdersService.killOrder(rider_id, order_id);

    }

    @ApiOperation(value = "通过电话好码查询骑手ID")
    @GetMapping("/userGetId")
    public R uerGetCustomId(@RequestParam("phone") String phone) {
        Rider one = iRiderService.getOne(new LambdaQueryWrapper<Rider>().select(Rider::getId).eq(Rider::getPhone, phone));
        if (one != null) {
            return R.success("查询成功", one.getId());
        }

        return R.error("查询失败");
    }

    @ApiOperation(value = "点击传 头像 跟 名字  获取所有 当前骑手信息")
    @GetMapping("/getCustom")
    public R getCustom(@RequestParam("avatarUrl") String avatarUrl, @RequestParam("nickName") String nickName, @RequestParam("phone") String phone) {

        Rider custom = iRiderService.getOne(new LambdaQueryWrapper<Rider>().eq(Rider::getPhone, phone));
        custom.setRiderUserName(nickName);
        custom.setRiderAvatar(avatarUrl);
        boolean b = iRiderService.updateById(custom);
        return R.success("成功", custom);
    }


    @ApiOperation(value = "根据id查询骑手信息")
    @GetMapping("/queryById")
    public R getCustomById(@RequestParam("riderId") Integer id) {
        Rider byId = iRiderService.getById(id);
        if (byId != null) {
            return R.success("查询成功", byId);

        }
        return R.error("未知错误");

    }


    @ApiOperation(value = "根据手机号码查客户信息")
    @GetMapping("/queryByPhone")
    public R queryByPhone(@RequestParam("phone") String phone) {

        Rider one = iRiderService.getOne(new LambdaQueryWrapper<Rider>().eq(Rider::getPhone, phone));
        if (one != null) {
            return R.success("查询成功", one);
        }
        return R.error("查询失败");
    }

    @ApiOperation(value = "修改密码")
    @GetMapping("/updatePassword")
    public R updatePassword(@RequestParam("phone") String phone, @RequestParam("password") String password) {

        Rider rider = new Rider();
        Rider one = iRiderService.getOne(new LambdaQueryWrapper<Rider>().eq(Rider::getPhone, phone));
        if (one != null) {
            rider.setId(one.getId());
            password = passwordEncoder.encode(password);
            rider.setPassword(password);
            boolean b = iRiderService.updateById(rider);
            if (b) {
                return R.success("修改密码成功");
            }


        }
        return R.error("手机号码有误");

    }

    @ApiOperation(value = "全部订单状态查询")
    @GetMapping("/Goods/qurryStatus/{rider_id}")
    public R qurryAllOrdersStatus(@RequestParam("rider_id") Integer rider_id,
                                  @RequestParam(required = false, value = "status") Integer status,
                                  @RequestParam(required = false, value = "order_id") String order_id) {
        return iOrdersService.qurryAllOrdersStatus(rider_id, status,order_id);
    }

    @ApiOperation(value = "骑手到达目指定的地等候顾客到达")
    @PutMapping("/arrivePlace/{rider_id}/{order_id}/{x}/{y}")
    public R arrivePlace(@PathVariable Integer rider_id, @PathVariable String order_id,
                         @PathVariable(required = false) Double x, @PathVariable(required = false) Double y) {
        return iOrdersService.arrivePlace(rider_id, order_id, x, y);
    }

    @ApiOperation(value = "骑手已取货正在快马加鞭配送")
    @PutMapping("/confirmGoods/{rider_id}/{order_id}")
    public R confirmGoods(@PathVariable Integer rider_id, @PathVariable String order_id) {
        return iOrdersService.confirmGoods(rider_id, order_id);
    }

    @ApiOperation(value = "骑手已将物品送达指定目的地")
    @PutMapping("/deliveried/{rider_id}/{order_id}/{x}/{y}")
    public R deliveriedGoods(@PathVariable Integer rider_id, @PathVariable String order_id,
                             @PathVariable(required = false) Double x, @PathVariable(required = false) Double y) {
        return iOrdersService.deliveriedGoods(rider_id, order_id, x, y);
    }


}
