package org.example.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName:RespBean
 * Package:com.cbb.server.common
 * Description:
 *
 * @Date:2022/5/12 21:16
 * @Author:cbb
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R {

    private long code;
    private  String message;
    private  Object data;

    //成功返回 结果
    public static R success(String message){

        return new R(200,message,null);
    }

    //成功返回 结果
    public static R success(String message, Object o){

        return new R(200,message,o);
    }

    //失败返回
    public  static R error(String message, Object obj){
        return  new R(500,message,obj);
    }

    public  static R error(String message){
        return  new R(500,message,null);
    }


}
