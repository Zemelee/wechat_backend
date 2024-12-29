package org.example;

import org.example.entity.Message;
import org.example.service.MessageService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.mapper") //将mapper层的所有mapper注入
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

    }