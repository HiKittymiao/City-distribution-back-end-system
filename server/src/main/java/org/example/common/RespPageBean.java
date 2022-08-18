package org.example.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ClassName:RespPageBean
 * Package:com.cbb.server.common
 * Description:
 *
 * @Date:2022/5/15 15:27
 * @Author:cbb
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespPageBean {

    //总条数
    private Long total;
    //数据list
    private List<?> allOrderList;

}
