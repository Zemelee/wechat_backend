package org.example.config;

import org.mybatis.spring.annotation.MapperScan;
@MapperScan("org.example.mapper")
public class MybatisPlusConfig {
    // 确保扫描到 UserMapper

}
