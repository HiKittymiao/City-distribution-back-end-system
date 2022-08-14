package org.example.utlis;

import org.example.dto.OrderDetial;
import org.example.vo.PriceAndDistance;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: DistanceUtil
 * @Author: MaCongYi
 * @create: 2022-08-08 12:56
 * @Description: 距离计算工具类
 * @Version: 1.0
 */

@Component
public class DistanceUtil {


    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 距离米
     */
    public static double getDistance2(double lat1, double lng1, double lat2,
                                     double lng2 ) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;

        return s;
    }
    /**
     `* @param order`
     * @return PriceAndDistance
     * @author 聪懿
     * @date 2022/8/8 17:33
     * @description 计算价格和距离输出公里
     **/
    public static PriceAndDistance getDistance(OrderDetial order) {
        PriceAndDistance priceAndDistance = new PriceAndDistance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        priceAndDistance.setCreateDate(df.format(new Date()));
        //经纬计算得到距离米
        double radLat1 = rad(order.getSLatitude());
        double radLat2 = rad(order.getRLatitude());
        double a = radLat1 - radLat2;
        double b = rad(order.getSLongitude()) - rad(order.getRLongitude());
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000d) / 10000d;
        distance = distance * 1000*1000;

        priceAndDistance.setDistance(distance);

        double price =0.00;
        if(distance<=3)
        {
            price += 5;
        }
        else if (distance>3) {
            price+=5;
            //三公里后每公里加3元
            for (int i = 0; i < (int)distance-3; i++) {
                price+=2;
            }
        }
        int weight = order.getGoodsWeight();
        //根据重量计算价格
        if(weight<3)
        {
            price+=0;
        }else if(weight>3){
            price=price*1.5;
        }
        priceAndDistance.setPrice(price);
        return priceAndDistance;
    }


}
