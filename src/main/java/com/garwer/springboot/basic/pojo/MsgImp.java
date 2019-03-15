package com.garwer.springboot.basic.pojo;

/**
 * @Author: Garwer
 * @Date: 19/3/1 上午12:18
 * @Version 1.0
 */
public class MsgImp implements Msg {
    @Override
    public String sayHello() {
        return "hello world";
    }
}
