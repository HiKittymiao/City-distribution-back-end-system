package org.example;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.dto.OrderDetial;
import org.example.pojo.Orders;
import org.example.service.ICustomService;
import org.example.service.IOrdersService;
import org.example.service.IRiderService;
import org.example.service.impl.CustomServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: test
 * @Author: MaCongYi
 * @create: 2022-08-08 19:35
 * @Description:
 * @Version: 1.0
 */

@Slf4j
@SpringBootTest
public class test {

    @Autowired
    private ICustomService iCustomService;
    @Autowired
    private IOrdersService iOrdersService;
    @Autowired
    private IRiderService iRiderService;

    @Autowired

    @Test
    public void fda(){
        //System.out.println(iCustomService.getMoney(2));
        //System.out.println(iOrdersService.newId());
      //
      //  //输出当前时间
      //  LocalDateTime dateTime = LocalDateTime.now();
      //  System.out.println(dateTime);
    }

    @Test
    public void fdasg(){
        iOrdersService.PayOrder(1220810556320001l,1);
    }

    @Test
    public void fdsafda(){
        //查询可以抢单列表
        Set killOrder = iOrdersService.getKillOrder();
        killOrder.forEach(System.out::println);
    }
    @Test
    public void fdsaf(){
        List<Orders> killOrderDetail = iOrdersService.getKillOrderDetail(iOrdersService.getKillOrder());
        killOrderDetail.stream().forEach(System.out::println);
    }

    @Test
    public void fdsafgs(){
        Boolean rider = iRiderService.isRider(1);
        System.out.println(rider);
    }

    @Test
    public void fdsag(){
                R killOrder = iOrdersService.killOrder(1,1220810787300001l);
        System.out.println(killOrder);
    }
}
