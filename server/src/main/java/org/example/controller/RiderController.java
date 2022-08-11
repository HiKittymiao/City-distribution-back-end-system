package org.example.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.R;
import org.example.pojo.Rider;
import org.example.service.IOrdersService;
import org.example.service.IRiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 *  前端控制器
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
    public R getKillOrder(){
        return R.success("",iOrdersService.getKillOrder());
    }


    /**
     * @param ids
     * @return org.example.common.R
     * @author 聪懿
     * @date 2022/8/10 17:09
     * @description TODO
     **/
    @ApiOperation(value = "骑手通过id集合查询可抢单的详细内容")
    @PostMapping("/getKillOrder")
    public R getKillOrderDetail(@RequestBody Set<Long> ids){
        return R.success("",iOrdersService.getKillOrderDetail(ids));
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
    public R getAllKillOrderDetail(){
        return R.success("",iOrdersService.getAllKillOrderDetail());
    }

    @ApiOperation(value = "骑手抢单")
    @PostMapping("/killOrder/{rider_id}/{order_id}")
    public R killOrders(@PathVariable Integer rider_id, Long order_id){
        return  iOrdersService.killOrder(rider_id,order_id);

    }

}
