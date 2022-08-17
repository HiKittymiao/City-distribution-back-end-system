package org.example.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

    @ApiModelProperty(value = "发送者电话号码")
    @TableField("s_phone")
    private String senderPhone;
    @ApiModelProperty(value = "发送者用户名")
    @TableField("s_name")
    private String senderName;
    @ApiModelProperty(value = "发件人地址")
    @TableField("sender_address")
    private String senderAddress;

    @ApiModelProperty(value = "接受用户的名字")
    @TableField("r_name")
    private String addresseeName;
    @ApiModelProperty(value = "接收者电话号码")
    @TableField("r_phone")
    private String addresseePhone;
    @ApiModelProperty(value = "收件人地址")
    @TableField("addressee_address")
    private String addresseeAddress;

    @ApiModelProperty(value = "订单内容")
    @TableField("order_content")
    private String goodsDescribe;

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

    @ApiModelProperty(value = "配送距离单位Km")
    private Double distance;

    @ApiModelProperty(value = "订单状态;0：开启订单顾客未支付，1：顾客已支付骑手未取货，2骑手已取货正在配送，3货物成功送达目的地，4订单完成,5订单被取消")
    @TableField("status")
    private Integer statue;

    //@JsonDeserialize(using = LocalDateDeserializer.class)
    //@JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "订单支付时间")
    @TableField("pay_date")
    private LocalDateTime payDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")

    @ApiModelProperty(value = "骑手接单时间")
    @TableField("rider_accept_date")
    private LocalDateTime riderAcceptDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")

    @ApiModelProperty(value = "骑手到达顾客地时间")
    @TableField("rider_get_date")

    private LocalDateTime riderGetDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "骑手开始配送")
    @TableField("rider_send_date")
    private LocalDateTime riderSendDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "骑手送达时间")
    @TableField("rider_complete_date")
    private LocalDateTime riderCompleteDate;

    //@JsonDeserialize(using = LocalDateDeserializer.class)
    //@JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")

    @TableField("update_time")
    private LocalDateTime updateTime;

    //@JsonDeserialize(using = LocalDateDeserializer.class)
    //@JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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

    @ApiModelProperty(value = "物品类型")
    @TableField("goods_type")
    private String goodsType;

    @ApiModelProperty(value = "商品重量 单位公斤")
    @TableField("goods_weight")
    private Integer goodsWeight;


}
