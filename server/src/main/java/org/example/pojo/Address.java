package org.example.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
 * @author mcy
 * @since 2022-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("web_address")
@ApiModel(value="Address对象", description="")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增定位id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    @TableField("customer_id")
    private Integer customerId;

    @ApiModelProperty(value = "发送者用户名")
    @TableField("s_name")
    private String senderName;

    @ApiModelProperty(value = "发送者电话号码")
    @TableField("s_phone")
    private String senderPhone;

    @ApiModelProperty(value = "发送者地址")
    @TableField("s_address")
    private String senderAddress;

    @ApiModelProperty(value = "发送者经度地址")
    @TableField("s_longitude")
    private BigDecimal sLongitude;

    @ApiModelProperty(value = "发送者维度地址")
    @TableField("s_latitude")
    private BigDecimal sLatitude;

    @ApiModelProperty(value = "需要送达的接受用户名字")
    @TableField("r_name")
    private String addresseeName;

    @ApiModelProperty(value = "接收者电话号码")
    @TableField("r_phone")
    private String addresseePhone;

    @ApiModelProperty(value = "接收者目的地址")
    @TableField("r_address")
    private String rAddress;

    @ApiModelProperty(value = "接收者目的地经度")
    @TableField("r_longitude")
    private BigDecimal rLongitude;

    @ApiModelProperty(value = "接收者目的地纬度")
    @TableField("r_latitude")
    private BigDecimal rLatitude;

    @ApiModelProperty(value = "0为正常使用1为已删除")
    @TableField("del_flag")
    private Boolean delFlag;


}
