package org.example.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author mcy
 * @since 2022-08-08
 */
@Data
//@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
@TableName("web_orders")
@ApiModel(value="Orders对象", description="订单表")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id") // type = IdType.AUTO
    private Long id;

    @ApiModelProperty(value = "顾客ID")
    @TableField("customer_id")
    private Integer customerId;

    @ApiModelProperty(value = "关联地址表得到地址信息")
    @TableField("address_id")
    private Integer addressId;

    @ApiModelProperty(value = "骑手ID")
    @TableField("reder_id")
    private Integer rederId;

    @ApiModelProperty(value = "订单内容")
    @TableField("order_content")
    private String orderContent;

    @ApiModelProperty(value = "起始经")
    @TableField("s_longitude")
    private Double sLongitude;

    @ApiModelProperty(value = "起始纬")
    @TableField("s_latitude")
    private Double sLatitude;

    @ApiModelProperty(value = "终点经纬")
    @TableField("r_longitude")
    private Double rLongitude;

    @ApiModelProperty(value = "终点纬")
    @TableField("r_latitude")
    private Double rLatitude;

    @ApiModelProperty(value = "金额")
    private Double price;

    @ApiModelProperty(value = "配送距离")
    private Double distance;

    @ApiModelProperty(value = "订单状态;0：开启订单顾客未支付，1：顾客已支付骑手未取货，2骑手已取货正在配送，3货物成功送达目的地，4订单完成,5订单被取消")
    private Integer status;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "订单支付时间")
    @TableField("pay_date")
    private LocalDateTime payDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "骑手接单时间")
    @TableField("rider_accept_date")
    private LocalDateTime riderAcceptDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "骑手到达顾客地时间")
    @TableField("rider_get_date")
    private LocalDateTime riderGetDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "骑手开始配送")
    @TableField("rider_send_date")
    private LocalDateTime riderSendDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "骑手送达时间")
    @TableField("rider_complete_date")
    private LocalDateTime riderCompleteDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "预计送达时间")
    @TableField("estimated_time")
    private LocalDateTime estimatedTime;

    @ApiModelProperty(value = "用户评价")
    @TableField("user_evaluate")
    private String userEvaluate;

    @ApiModelProperty(value = "用户对此次订单评分")
    @TableField("user_score")
    private Integer userScore;

    @ApiModelProperty(value = "订单类型，帮我送、帮我取、帮我买")
    @TableField("order_type")
    private String orderType;

    @ApiModelProperty(value = "商品重量 单位公斤")
    @TableField("goods_weight")
    private Integer goodsWeight;


}
