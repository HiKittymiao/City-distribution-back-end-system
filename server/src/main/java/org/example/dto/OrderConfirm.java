package org.example.dto;
/**
 * @ClassName: OrderConfirm
 * @Author: MaCongYi
 * @create: 2022-08-20 11:39
 * @Description: 前端确认收货评价评分
 * @Version: 1.0
 */

import lombok.Data;

@Data
public class OrderConfirm {
    private String orderId;
    private Integer customerId;
    //用户评分
    private Integer userScore;
    //用户评价
    private String userEvaluate;

}
