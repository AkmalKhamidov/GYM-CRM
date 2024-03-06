package com.epamlearning.reportmicroservice.consumer;

import com.epamlearning.reportmicroservice.dtos.TrainerWorkloadRequestDTO;
import com.epamlearning.reportmicroservice.services.TrainerWorkloadService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageConsumer {

    private final TrainerWorkloadService trainerWorkloadService;

    @Autowired
    public MessageConsumer(TrainerWorkloadService trainerWorkloadService) {
        this.trainerWorkloadService = trainerWorkloadService;
    }

    @JmsListener(destination = "trainer-workload-queue")
    public void manageTrainerWorkloadListener(@Payload TrainerWorkloadRequestDTO request, @Header("jms_correlationId") String transactionId) {
        MDC.put("transactionId", transactionId);
        log.info("Received message: " + request);
        trainerWorkloadService.manageTrainerWorkload(request);
    }

}
