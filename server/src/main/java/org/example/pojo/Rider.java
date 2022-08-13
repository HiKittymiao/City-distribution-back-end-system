package org.example.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author cbb
 * @since 2022-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("web_rider")
@ApiModel(value="Rider对象", description="")
public class Rider implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "骑手姓名")
    @TableField("rider_name")
    private String riderName;

    @ApiModelProperty(value = "骑手电话")
    private String phone;

    @ApiModelProperty(value = "骑手账号")
    @TableField("rider_user_name")
    private String riderUserName;

    @ApiModelProperty(value = "骑手状态")
    @TableField("rider_status")
    private Integer riderStatus;

    @ApiModelProperty(value = "注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "骑手评分")
    @TableField("rider_score")
    private Integer riderScore;

    @ApiModelProperty(value = "骑手头像")
    @TableField("rider_avatar")
    private String riderAvatar;

    @ApiModelProperty(value = "骑手接单数量总和")
    @TableField("take_orders_number")
    private Integer takeOrdersNumber;

    @ApiModelProperty(value = "赚取的钱(可提现数)")
    @TableField("get_money")
    private Double getMoney;

    @ApiModelProperty(value = "微信号")
    @TableField("wx_id")
    private String wxId;

    @ApiModelProperty(value = "到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @TableField("expire_time")

    private LocalDateTime expireTime;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "投诉次数")
    @TableField("complaint_number")
    private Integer complaintNumber;

    @ApiModelProperty(value = "好评次数")
    @TableField("praise_number")
    private Integer praiseNumber;

    @ApiModelProperty(value = "密码(审核成功后默认123456)")
    private String password;

    @ApiModelProperty(value = "男")
    private String sex;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @TableField("del_flag")
    private Boolean delFlag;

    @ApiModelProperty(value = "身份证号")
    @TableField("persion_id")
    private String persionId;

    @ApiModelProperty(value = "已提现金额")
    @TableField("already_money")
    private Double alreadyMoney;


    @ApiModelProperty(value = "公里数")
    @TableField("kilometre")
    private Double kilometre;







}
