package com.twitter.ms;

import com.gmail.merikbest2015.configuration.SharedConfiguration;
import com.gmail.merikbest2015.mapper.BasicMapper;
import com.gmail.merikbest2015.security.JwtProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {"com.twitter.ms", "com.gmail.merikbest2015"})
@EnableAutoConfiguration
@EnableFeignClients
@EnableDiscoveryClient
@Import({JwtProvider.class, BasicMapper.class, SharedConfiguration.class})
public class UserServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
