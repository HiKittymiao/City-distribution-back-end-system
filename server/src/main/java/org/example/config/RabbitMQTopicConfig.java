package org.example.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: MaCongYi
 * @Date: 2022/08/13 19:09
 * @Description:
 * @Version:1.0
 */
@Configuration
public class RabbitMQTopicConfig {
    private static final String QUEUE = "mysqlQueue";
    //private static final String QUEUE2 = "mysqlSaveQueue";
    private static final String EXCHANGE = "mysqlExchange";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }
    //@Bean
    //public Queue queue2() {
    //    return new Queue(QUEUE2);
    //}

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(topicExchange()).with("mysql.#");
    }
}
