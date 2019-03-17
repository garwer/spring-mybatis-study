package com.garwer.springboot;

import com.garwer.myStarter.HelloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private HelloService helloService;

    @Test
    public void myStarterTest () {
        System.out.println(helloService.sayHello());
    }

}
