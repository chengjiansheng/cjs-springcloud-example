package com.cjs.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author chengjiansheng
 * @date 2019-06-26
 */
@EnableEurekaServer
@SpringBootApplication
public class CjsEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CjsEurekaServerApplication.class, args);
    }

}
