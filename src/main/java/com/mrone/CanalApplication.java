package com.mrone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Mr.One
 * @program canal-demo
 * @description
 * @create 2024-06-19 19:45
 **/
@SpringBootApplication
@MapperScan("com.mrone.mapper")
public class CanalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CanalApplication.class,args);
    }
}
