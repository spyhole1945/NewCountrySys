package com.ruralsync.sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ruralsync.sys.mapper")
public class RuralSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuralSyncApplication.class, args);
    }

}
