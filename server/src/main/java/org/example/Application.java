package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName:Application
 * Package:org.example
 * Description:
 *
 * @Date:2022/8/6 20:49
 * @Author:cbb
 */

@SpringBootApplication
@MapperScan({"org.example.mapper"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);

    }
}
