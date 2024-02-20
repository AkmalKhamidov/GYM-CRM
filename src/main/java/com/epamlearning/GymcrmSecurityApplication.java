package com.epamlearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableAspectJAutoProxy
public class GymcrmSecurityApplication {

    public static final String TRANSACTION_ID = "transactionId";

    public static void main(String[] args) {
        SpringApplication.run(GymcrmSecurityApplication.class, args);
    }
}
