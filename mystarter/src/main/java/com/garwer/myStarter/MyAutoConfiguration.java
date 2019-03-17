package com.garwer.myStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Garwer
 * @Date: 19/3/15 上午12:38
 * @Version 1.0
 * @Configuration:表示该类为一个spring配置类
 * @EnableConfigurationProperties:启动配置文件 value可以有多个
 * @ConditionalOnClass classPath下存在指定类时才有效
 */

@Configuration
@EnableConfigurationProperties(value = HelloServiceProperties.class)
//@EnableConfigurationProperties(value = {HelloServiceProperties.class,HelloAndHiServiceProperties.class})
@ConditionalOnClass(HelloService.class)
@ConditionalOnProperty(prefix = "hello", value = "enable", matchIfMissing = true)

public class MyAutoConfiguration {
    static {
        System.out.println("hello world");
    }

    @Autowired
    private HelloServiceProperties helloServiceProperties;

    @Bean
    @ConditionalOnMissingBean(HelloService.class)
    public HelloService s() {
        HelloService helloService = new HelloService();
        helloService.setMsg(helloServiceProperties.getMsg());
        return helloService;
    }
}
