package org.example.dto;

import lombok.Data;

/**
 * ClassName:OrderYueDto
 * Package:org.example.dto
 * Description:
 *
 * @Date:2022/8/28 16:42
 * @Author:cbb
 */
@Data
public class OrderYueDto {
    private Integer completeOrder;
    private Integer refundOrder;
    private Double moneySum;

}
