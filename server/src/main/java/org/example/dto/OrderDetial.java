package org.example.dto;

import com.baomidou.mybatisplus.annotation.TableField;
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
    private Integer customerId;

    @ApiModelProperty(value = "接受用户的名字")
    private String addresseeName;
    @ApiModelProperty(value = "接收者电话号码")
    private String addresseePhone;
    @ApiModelProperty(value = "接收者目的地址",required = true)
    private String addresseeAddress;

    @ApiModelProperty(value = "发送者用户名")
    private String senderName;
    @ApiModelProperty(value = "顾客手机号码",required = true)
    private String senderPhone;
    @ApiModelProperty(value = "发送者地址",required = true)
    private String senderAddress;

    //@ApiModelProperty(value = "关联地址表得到地址信息",required = true)
    //private Integer addressId;

    @ApiModelProperty(value = "订单内容",required = true)
    private String goodsDescribe;


    @ApiModelProperty(value = "起始经",required = true)
    private double sLongitude;

    @ApiModelProperty(value = "起始纬",required = true)
    private double sLatitude;

    @ApiModelProperty(value = "终点经纬",required = true)
    private Double rLongitude;

    @ApiModelProperty(value = "终点纬",required = true)
    private Double rLatitude;

    @ApiModelProperty(value = "订单类型，帮我送、帮我取、帮我买",required = true)
    private String orderType;

    @ApiModelProperty(value = "商品重量 单位公斤",required = true)
    private Integer goodsWeight;




}
