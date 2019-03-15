package com.garwer.springboot.myStarter;

/**
 * @Author: Garwer
 * @Date: 19/3/15 上午12:51
 * @Version 1.0
 */
public class HelloService {
    private String msg;

    public String sayHello() {
        return "Hello " + msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
