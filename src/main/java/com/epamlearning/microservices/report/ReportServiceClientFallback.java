package com.epamlearning.microservices.report;

import com.epamlearning.microservices.report.dtos.TrainerSummaryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ReportServiceClientFallback implements ReportServiceClient{

    @Override
    public ResponseEntity<TrainerSummaryDTO> getByUsername(String username) {
        log.error("Report Service is not available");
        TrainerSummaryDTO fallbackTrainingSummary = new TrainerSummaryDTO("000", username, null, null, false, null);
        return new ResponseEntity<>(fallbackTrainingSummary, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    public ResponseEntity<List<TrainerSummaryDTO>> getByFirstNameAndLastName(String firstName, String lastName) {
        log.error("Report Service is not available");
        List<TrainerSummaryDTO> fallbackTrainingSummaryList = List.of(new TrainerSummaryDTO("000", null, null, null, false, null));
        return new ResponseEntity<>(fallbackTrainingSummaryList, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
