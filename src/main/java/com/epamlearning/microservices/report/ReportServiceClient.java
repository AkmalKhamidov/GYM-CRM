package com.epamlearning.microservices.report;


import com.epamlearning.microservices.FeignClientConfiguration;
import com.epamlearning.microservices.report.dtos.TrainerSummaryDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;


@FeignClient(value = "report-microservice", url = "${microservices.report.url}", fallback = ReportServiceClientFallback.class, configuration = FeignClientConfiguration.class)
public interface ReportServiceClient {

  Logger logger = LoggerFactory.getLogger(ReportServiceClient.class);

  @GetMapping("monthly-summary")
  @CircuitBreaker(name = "cb-get-trainer-summary", fallbackMethod = "fallbackGetTrainingSummaryFallback")
  ResponseEntity<TrainerSummaryDTO> getByUsername(@RequestParam @Valid @NotNull @NotBlank String username);
  default ResponseEntity<TrainerSummaryDTO> fallbackGetTrainingSummaryFallback(String username, IOException e){
    logger.error("Report Service is not available", e);
    TrainerSummaryDTO fallbackTrainingSummary = new TrainerSummaryDTO(0L, username, null, null, false, null, null);
    return new ResponseEntity<>(fallbackTrainingSummary, HttpStatus.SERVICE_UNAVAILABLE);
  };

}
