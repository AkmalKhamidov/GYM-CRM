package com.epamlearning.microservices.report;


import com.epamlearning.microservices.FeignClientConfiguration;
import com.epamlearning.microservices.report.dtos.TrainerSummaryDTO;
import com.epamlearning.microservices.report.dtos.TrainerWorkloadDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "report-microservice", url = "${microservices.report.url}", fallback = ReportServiceClientFallback.class, configuration = FeignClientConfiguration.class)
public interface ReportServiceClient {

  @PostMapping("trainer-workload")
  ResponseEntity<Void> manageTrainerWorkload(@RequestBody @Validated TrainerWorkloadDTO dto);

  @GetMapping("monthly-summary")
  ResponseEntity<TrainerSummaryDTO> calculate(@RequestParam @Valid @NotNull @NotBlank String username);

}
