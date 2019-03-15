package com.garwer.springboot.basic;

import com.garwer.springboot.basic.pojo.MsgImp;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: Garwer
 * @Date: 19/3/1 上午12:15
 * @Version 1.0
 */
public class BasicTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-abc.xml");
        System.out.println("context 启动成功");

        //下面有多个构造函数 也可以通过类对象
        MsgImp msgImp = (MsgImp) context.getBean("msgImp");
        System.out.println(msgImp.sayHello());
    }
}
