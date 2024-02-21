package com.epamlearning.reportmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAspectJAutoProxy
public class ReportMicroserviceApplication {

  public static final String TRANSACTION_ID = "transactionId";
  public static void main(String[] args) {
    SpringApplication.run(ReportMicroserviceApplication.class, args);
  }

}
