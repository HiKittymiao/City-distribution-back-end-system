package org.example.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: Distance
 * @Author: MaCongYi
 * @create: 2022-08-08 12:31
 * @Description: 计算距离和价格实体类
 * @Version: 1.0
 */

@Data
@ApiModel(value = "前端距离计算实体类")
public class OrderDetial {


    @ApiModelProperty(value = "顾客ID",required = true)
    @TableField("customer_id")
    private Integer customerId;

    @ApiModelProperty(value = "接受用户的名字")
    @TableField("r_name")
    private String rName;

    @ApiModelProperty(value = "接收者电话号码")
    @TableField("r_phone")
    private String rPhone;

    //@ApiModelProperty(value = "发送者电话号码")
    //@TableField("s_phone")
    //private Integer sPhone;

    @ApiModelProperty(value = "发送者用户名")
    @TableField("s_name")
    private String sName;

    @ApiModelProperty(value = "顾客手机号码",required = true)
    private String phone;

    @ApiModelProperty(value = "关联地址表得到地址信息",required = true)
    @TableField("address_id")
    private Integer addressId;

    @ApiModelProperty(value = "订单内容",required = true)
    @TableField("order_content")
    private String orderContent;


    @ApiModelProperty(value = "起始经",required = true)
    @TableField("s_longitude")
    private double sLongitude;

    @ApiModelProperty(value = "起始纬",required = true)
    @TableField("s_latitude")
    private double sLatitude;

    @ApiModelProperty(value = "终点经纬",required = true)
    @TableField("r_longitude")
    private Double rLongitude;

    @ApiModelProperty(value = "终点纬",required = true)
    @TableField("r_latitude")
    private Double rLatitude;

    @ApiModelProperty(value = "订单类型，帮我送、帮我取、帮我买",required = true)
    @TableField("order_type")
    private String orderType;

    @ApiModelProperty(value = "商品重量 单位公斤",required = true)
    @TableField("goods_weight")
    private Integer goodsWeight;

    @ApiModelProperty(value = "发送者地址",required = true)
    @TableField("s_address")
    private String sAddress;

    @ApiModelProperty(value = "接收者目的地址",required = true)
    @TableField("r_address")
    private String rAddress;

}
