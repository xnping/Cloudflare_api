package com.cf.cloudflare_api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@MapperScan("com.cf.cloudflare_api.mapper")
public class CloudFlareApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudFlareApiApplication.class, args);
    }

}
