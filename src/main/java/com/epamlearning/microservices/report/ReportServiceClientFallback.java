package com.epamlearning.microservices.report;

import com.epamlearning.microservices.report.dtos.TrainerSummaryDTO;
import com.epamlearning.microservices.report.dtos.TrainerWorkloadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportServiceClientFallback implements ReportServiceClient{

    @Override
    public ResponseEntity<Void> manageTrainerWorkload(TrainerWorkloadDTO dto) {
        log.warn("Report Service is not available");
        throw new RuntimeException("Report Service is not available");
    }

    @Override
    public ResponseEntity<TrainerSummaryDTO> calculate(String username) {
        log.warn("Report Service is not available");
        throw new RuntimeException("Report Service is not available");
    }
}
