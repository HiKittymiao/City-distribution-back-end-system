package org.example.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * ClassName:Admin
 * Package:org.example.pojo
 * Description:
 *
 * @Date:2022/8/6 20:28
 * @Author:cbb
 */

@Data
@ApiModel(value="Admin对象", description="")
public class Admin implements Serializable, UserDetails {


    @ApiModelProperty(value = "用户名")
    @TableField("user_name")
    private String userName;


    @ApiModelProperty(value = "密码")
    private String password;



    @ApiModelProperty(value = "手机")
    private String phone;

    @ApiModelProperty(value = "是否启用")
    @Getter(AccessLevel.NONE)
    private Integer enabled;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getEnabled1(){
        return this.enabled;
    }
}
