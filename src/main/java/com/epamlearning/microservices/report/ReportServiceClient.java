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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@FeignClient(value = "report-microservice", url = "${microservices.report.url}", fallback = ReportServiceClientFallback.class, configuration = FeignClientConfiguration.class)
public interface ReportServiceClient {

  Logger logger = LoggerFactory.getLogger(ReportServiceClient.class);

  @GetMapping("monthly-summary/{username}")
  @CircuitBreaker(name = "cb-get-trainer-summary", fallbackMethod = "fallbackGetTrainingSummaryFallback")
  ResponseEntity<TrainerSummaryDTO> getByUsername(@PathVariable("username") @Valid @NotNull @NotBlank String username);


  @GetMapping("monthly-summary/search")
  @CircuitBreaker(name = "cb-get-trainer-summary", fallbackMethod = "fallbackGetTrainingSummaryByFirstNameAndLastNameFallback")
  ResponseEntity<List<TrainerSummaryDTO>> getByFirstNameAndLastName(@RequestParam("firstName") @Valid @NotNull @NotBlank String firstName,
                                                                        @RequestParam("lastName") @Valid @NotNull @NotBlank String lastName);

  default ResponseEntity<TrainerSummaryDTO> fallbackGetTrainingSummaryByUsernameFallback(String username, IOException e){
    logger.error("Report Service is not available", e);
    TrainerSummaryDTO fallbackTrainingSummary = new TrainerSummaryDTO("000", username, null, null, false, null);
    return new ResponseEntity<>(fallbackTrainingSummary, HttpStatus.SERVICE_UNAVAILABLE);
  }

  default ResponseEntity<TrainerSummaryDTO> fallbackGetTrainingSummaryByFirstNameAndLastNameFallback(String firstName, String lastName, IOException e){
    logger.error("Report Service is not available", e);
    TrainerSummaryDTO fallbackTrainingSummary = new TrainerSummaryDTO("000", null, firstName, lastName, false, null);
    return new ResponseEntity<>(fallbackTrainingSummary, HttpStatus.SERVICE_UNAVAILABLE);
  }

}
