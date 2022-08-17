package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.R;
import org.example.pojo.Address;
import org.example.service.ICustomAddressService;
import org.example.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: CustomAddressController
 * @Author: MaCongYi
 * @create: 2022-08-08 09:17
 * @Description: 顾客地址控制器
 * @Version: 1.0
 */

@RestController
@Api(tags = "顾客地址")
@RequestMapping("/custom")
public class CustomAddressController {
    @Autowired
    ICustomAddressService iCustomAddressService;

    @Autowired
    IOrdersService iOrdersService;

    /**
     * @param customerId
     * @return org.example.common.R
     * @author 聪懿
     * @date 2022/8/10 16:59
     * @description TODO
     **/
    @ApiOperation(value = "查询所有顾客地址")
    @GetMapping("/")
    public R getAllAddresss( Integer customerId){
        return iCustomAddressService.getAllAddresss(customerId);
    }

    /**
     * @param address
     * @return org.example.common.R
     * @author 聪懿
     * @date 2022/8/10 16:59
     * @description TODO
     **/
    @ApiOperation(value = "添加顾客地址")
    @PostMapping("/addAddresss")
    public R SaveAddresss(@RequestBody Address address){
        return iCustomAddressService.CustomSaveAddresss(address);
    }

    /**
     * @param id
     * @return org.example.common.R
     * @author 聪懿
     * @date 2022/8/10 16:59
     * @description TODO
     **/
    @ApiOperation(value = "删除用户地址")
    @DeleteMapping("/id")
    public R deletAddresss( Integer id){
        return iCustomAddressService.CustomdellAddresss(id);
    }

    //@ApiOperation(value = "查询顾客id和地址id查询单个地址内容")
    //@GetMapping("/{customerId}/{AddressId}")
    //public R getOneAddresss( Integer customerId,Integer AddressId){
    //    return iCustomAddressService.getOneAddresss(customerId,AddressId);
    //}


}
