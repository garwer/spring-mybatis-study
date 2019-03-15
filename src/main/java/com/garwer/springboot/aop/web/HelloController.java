package com.garwer.springboot.aop.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Garwer
 * @Date: 19/3/5 下午11:30
 * @Version 1.0
 * 若每个Controller都需要记录日志 使用aop优雅完成
 * 将请求日志的功能作为一个切面
 */

@RestController
@RequestMapping("/web")
public class HelloController {
    @GetMapping(value = "/hello")
    @ResponseBody
    public String hello() {
        String hello = "hello world";
        System.out.println(hello);
        return hello;
    }

    @GetMapping(value = "/hi")
    @ResponseBody
    public String hi() {
        String hello = "hi world";
        System.out.println(hello);
        return hello;
    }
}
