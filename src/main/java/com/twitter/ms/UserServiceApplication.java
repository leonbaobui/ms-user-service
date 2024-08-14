package com.twitter.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import main.java.com.leon.baobui.configuration.SharedConfiguration;
import main.java.com.leon.baobui.mapper.BasicMapper;
import main.java.com.leon.baobui.security.JwtProvider;

@SpringBootApplication(scanBasePackages = {"com.twitter.ms"})
@EnableAutoConfiguration
@EnableFeignClients
@EnableDiscoveryClient
@Import({JwtProvider.class, BasicMapper.class, SharedConfiguration.class})
public class UserServiceApplication {
    int a = 10;

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
