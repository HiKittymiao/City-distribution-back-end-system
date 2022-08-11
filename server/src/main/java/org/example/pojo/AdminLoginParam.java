package org.example.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * ClassName:AdminLoginParam
 * Package:com.cbb.server.common
 * Description:
 *
 * @Date:2022/5/12 21:24
 * @Author:cbb
 */

/*
* 用户登录的参数  实体类
* */

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value = "AdminLogin对象")
public class AdminLoginParam implements Serializable {
        @ApiModelProperty(value = "用户名" ,required = true)
        private  String username;
        @ApiModelProperty(value = "密码",required = true)
        private  String password;

        @ApiModelProperty(value = "验证码",required = true)
        private  String code;

        @ApiModelProperty(value = "手机号码",required = true)
        private  String phone;


}
