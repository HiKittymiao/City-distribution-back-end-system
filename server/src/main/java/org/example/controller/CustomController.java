package org.example.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.R;
import org.example.pojo.Custom;
import org.example.service.ICustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value="顾客注册")
    @PostMapping("/userRegister")
    public R userRegister(@RequestBody Custom custom){

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
    public R uerGetCustomId(String phone){
        return iCustomService.GetCustomId(phone);
    }

}
