package com.garwer.myStarter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Garwer
 * @Date: 19/3/17 下午10:22
 * @Version 1.0
 * 该pojo用于接收properties中配置信息
 *
 * prefix用于表示配置文件中配置项前缀
 *
 */

@ConfigurationProperties(prefix="hello")
public class HelloServiceProperties {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
