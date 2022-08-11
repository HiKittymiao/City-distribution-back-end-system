package org.example.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.R;
import org.example.pojo.AdminLoginParam;
import org.example.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * ClassName:LoginController
 * Package:com.ikun.controller
 * Description:
 *
 * @Date:2022/7/7 14:49
 * @Author:cbb
 */
@RestController
@Api(tags = "LoginController")
public class LoginController {

    @Autowired
    private ILoginService loginService;

    
    /**
     * 方法名 
     *
     * @param adminLoginParam
     * @param request
 * @return com.ikun.common.R
     * @author test
     * @date 2022/7/7 14:50
     */
    @ApiOperation(value = "登陆之后返回token")
    @PostMapping("/login")
    public R login(@RequestBody AdminLoginParam adminLoginParam){

        return loginService.login(adminLoginParam.getUsername(),adminLoginParam.getPassword());
    }

    @ApiOperation(value = "测试")
    @GetMapping("/hello")
    public String hello(){
        return "cbb黑子";
    }






    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public R logout() {
        return R.success("注销成功！");
    }



}
