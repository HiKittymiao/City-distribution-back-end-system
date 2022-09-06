package org.example.enums;
/**
 * @ClassName: Enum_redis
 * @Author: MaCongYi
 * @create: 2022-09-03 19:21
 * @Description:
 * @Version: 1.0
 */

/**
 * @Auther: MaCongYi
 * @Date: 2022/09/03 19:21
 * @Description:
 * @Version:1.0
 */
public enum Enum_redis {
    rider("rider:"),
    order("order:"),
    kill_order("kill_order");

    private final  String s;

    Enum_redis(String s){
        this.s=s;
    }
}
