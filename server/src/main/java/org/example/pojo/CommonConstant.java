package org.example.pojo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @ClassName: CommonConstant
 * @Author: MaCongYi
 * @create: 2022-08-14 10:54
 * @Description:
 * @Version: 1.0
 */
public interface CommonConstant {
    ObjectMapper JacksonMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
}
