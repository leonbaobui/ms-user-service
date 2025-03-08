package com.twitter.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import main.java.com.leon.baobui.configuration.SharedConfiguration;
import main.java.com.leon.baobui.mapper.BasicMapper;
import main.java.com.leon.baobui.security.JwtProvider;

@SpringBootApplication(scanBasePackages = {"com.twitter.ms", "main.java.com.leon.baobui"})
@EnableAutoConfiguration
@EnableFeignClients
@EnableDiscoveryClient
@EnableJpaRepositories({"com.lib.twitter.lib_trans_outbox.repository", "com.twitter.ms.repository"})
@EntityScan({"com.lib.twitter.lib_trans_outbox.entity", "com.twitter.ms.model"})
@Import({JwtProvider.class, BasicMapper.class, SharedConfiguration.class})
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
