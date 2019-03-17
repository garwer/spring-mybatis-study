package com.garwer.myStarter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Garwer
 * @Date: 19/3/17 下午10:27
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "hi")
public class HelloAndHiServiceProperties {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
