package com.epamlearning.microservices.report;

import com.epamlearning.microservices.report.dtos.TrainerSummaryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportServiceClientFallback implements ReportServiceClient{

    @Override
    public ResponseEntity<TrainerSummaryDTO> getByUsername(String username) {
        log.error("Report Service is not available");
        TrainerSummaryDTO fallbackTrainingSummary = new TrainerSummaryDTO(0L, username, null, null, false, null, null);
        return new ResponseEntity<>(fallbackTrainingSummary, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
