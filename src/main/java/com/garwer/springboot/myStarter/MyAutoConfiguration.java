package com.garwer.springboot.myStarter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Garwer
 * @Date: 19/3/15 上午12:38
 * @Version 1.0
 */

@Configuration
@ConditionalOnProperty
public class MyAutoConfiguration {
    static {
        System.out.println("hello world");
    }

    @Bean
    public SimpleBean s() {
        return null;
    }
}
