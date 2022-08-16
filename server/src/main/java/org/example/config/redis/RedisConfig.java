package org.example.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

/**
 * 自定义Redis配置类，进行序列化以及RedisTemplate设置
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Value("${spring.redis.time-to-live}")
    private Duration timeToLive = Duration.ofDays(1l);
    /**
     *  定制Redis API模板RedisTemplate
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {




        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(redisConnectionFactory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);



        jacksonSeial.setObjectMapper(om);

        // 值采用json序列化
        template.setValueSerializer(jacksonSeial);
        //使用StringRedisSerializer来序列化和反序列化redis的key值

        //om.registerModule(new Jdk8Module())
        //        .registerModule(new JavaTimeModule())
        //        .registerModule(new ParameterNamesModule());

        template.setKeySerializer(new StringRedisSerializer());
        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);
        template.afterPropertiesSet();
        // 日期序列化处理



        //RedisTemplate<String, Object> template = new RedisTemplate<>();
        ////key序列化
        //template.setKeySerializer(new StringRedisSerializer());
        ////value序列化
        //template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        ////hash类型value序列化
        //template.setHashKeySerializer(new StringRedisSerializer());
        //template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        ////注入连接工厂
        //template.setConnectionFactory(redisConnectionFactory);
        //return template;

        //开启事物
        //template.setEnableTransactionSupport(true);
        return template;
    }


    //    定义cacheManager，统一redis的属性配置
    //@Bean
    //public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    //    RedisSerializer<String> redisSerializer = new StringRedisSerializer();
    //    // 配置序列化（解决乱码的问题）
    //    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
    //            // 缓存有效期
    //            .entryTtl(timeToLive)
    //            // 使用StringRedisSerializer来序列化和反序列化redis的key值
    //            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
    //            // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
    //            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2Serializer()))
    //            // 禁用空值
    //            .disableCachingNullValues();
    //
    //      RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(config).build();
    //      return cacheManager;
    //}





    /**
     * 配置Jackson2JsonRedisSerializer序列化策略
     * */
    //private Jackson2JsonRedisSerializer<Object> jackson2Serializer() {
    //    // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
    //    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    //    ObjectMapper objectMapper = new ObjectMapper();
    //    // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
    //    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    //    // 此项必须配置，否则会报java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to XXX
    //    // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
    //    objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
    //
    //    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
    //    return jackson2JsonRedisSerializer;
    //}
}

