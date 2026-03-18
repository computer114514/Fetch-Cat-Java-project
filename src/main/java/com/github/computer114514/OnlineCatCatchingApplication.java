package com.github.computer114514;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.github.computer114514.mapper")
public class OnlineCatCatchingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineCatCatchingApplication.class, args);
    }

}
