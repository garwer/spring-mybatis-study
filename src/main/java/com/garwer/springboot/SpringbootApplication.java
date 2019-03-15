package com.garwer.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
public class SpringbootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }
}

@RestController
class RootController {
    public static final String PATH_ROOT = "/";
    @RequestMapping(PATH_ROOT)
    public String welcome() {
        return "Welcome!";
    }
}