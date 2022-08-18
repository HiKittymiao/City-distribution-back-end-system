package org.example.utlis;
/**
 * @ClassName: OrderIidUtil
 * @Author: MaCongYi
 * @create: 2022-08-18 21:51
 * @Description:
 * @Version: 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: MaCongYi
 * @Date: 2022/08/18 21:51
 * @Description:
 * @Version:1.0
 */
@Component
public class OrderIidUtil {
    @Resource
    private  RedisTemplate redisTemplate;
    Lock lock = new ReentrantLock();//创建锁

    public  String newid(){
        long count = 0;
        String Tickid = "";
        //订单类型
        do
        {
            Integer idStyle = 1;
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");//设置日期格式(年-月-日-时-分-秒)
            String createTime = dateFormat.format(now);
            String secondTime = String.format("%0" + 5 + "d", (Integer.valueOf(createTime.substring(6, 8)) * 60 * 60
                    + Integer.valueOf(createTime.substring(8, 10)) * 60
                    + Integer.valueOf(createTime.substring(10, 12))));
            Tickid = idStyle + createTime.substring(0, 6) + secondTime;
            // 2.2.自增长
            lock.lock();

            String uuid = UUID.randomUUID().toString();
            // 从Redis获取锁
            Boolean flag = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 3, TimeUnit.SECONDS);
            if (flag) {
                // flag = true:表示拿到锁！ 执行业务逻辑
                count = (Long) redisTemplate.opsForValue().increment("ID:" + Tickid);
                // 定义一个lua脚本
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

                // 创建对象
                DefaultRedisScript<Long> redisScript = new DefaultRedisScript();
                // 设置lua脚本
                redisScript.setScriptText(script);
                //设置lua脚本返回类型为Long
                redisScript.setResultType(Long.class);
                // redis调用lua脚本
                redisTemplate.execute(redisScript, Arrays.asList("lock"), uuid);
                lock.unlock();//解锁
            } else {
                continue;
            }
        }while(count >10000);
        String countStr = String.format("%0" + 4 + "d", count);
        Tickid =Tickid +countStr;
        return Tickid;
    }



}
