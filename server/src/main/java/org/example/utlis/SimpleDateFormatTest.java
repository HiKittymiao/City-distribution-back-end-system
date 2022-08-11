package org.example.utlis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: SimpleDateFormatTest
 * @Author: MaCongYi
 * @create: 2022-08-08 19:34
 * @Description: 时间类
 * @Version: 1.0
 */
public class SimpleDateFormatTest {
    // 新建 DateTimeFormatter 类
    //private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //private static String formatDate(LocalDateTime dateTime) {
    //    return FORMATTER.format(dateTime);
    //}

    //public static LocalDateTime parse(String dateNow) {
    //    return LocalDateTime.parse(dateNow, FORMATTER);
    //}

    //public static void main(String[] args) throws InterruptedException {
    //
    //    ExecutorService service = Executors.newFixedThreadPool(100);
    //    for (int i = 0; i < 20; i++) {
    //        service.execute(() -> {
    //            for (int j = 0; j < 10; j++) {
    //                try {
    //                    System.out.println(parse(formatDate(LocalDateTime.now())));
    //                } catch (Exception e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        });
    //    }
    //    // 等待上述的线程执行完后
    //    service.shutdown();
    //    service.awaitTermination(1, TimeUnit.DAYS);
    //}
}
