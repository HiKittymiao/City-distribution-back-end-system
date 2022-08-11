package org.example.vo;

import lombok.Data;

/**
 * @ClassName: PriceAndDistance
 * @Author: MaCongYi
 * @create: 2022-08-08 17:02
 * @Description: 返回前端的距离和价格
 * @Version: 1.0
 */

@Data
public class PriceAndDistance {
    private Double price;

    private Double distance;

    private String createDate;
}
