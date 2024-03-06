package com.epamlearning.reportmicroservice.services.impl;

import com.epamlearning.reportmicroservice.entities.TrainerSummary;
import com.epamlearning.reportmicroservice.entities.TrainerWorkload;
import com.epamlearning.reportmicroservice.exceptions.NotFoundException;
import com.epamlearning.reportmicroservice.services.TrainerSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.adapter.JmsResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TrainerSummaryServiceImpl implements TrainerSummaryService {

    private final TrainerWorkloadServiceImpl trainerWorkloadService;

    @Autowired
    public TrainerSummaryServiceImpl(TrainerWorkloadServiceImpl trainerWorkloadService) {
        this.trainerWorkloadService = trainerWorkloadService;
    }

    @Override
    public TrainerSummary calculate(String username) {

        List<TrainerWorkload> trainerWorkloads = trainerWorkloadService.getAllByUsername(username);
        if (trainerWorkloads.isEmpty()) {
            throw new NotFoundException("No trainer workload found for username to calculate summary: " + username);
        }
        Map<String, Map<String, BigDecimal>> monthlySummaryMap = new HashMap<>();
        HashSet<Integer> years = new HashSet<>();

        for (TrainerWorkload trainerWorkload : trainerWorkloads) {
            LocalDate trainingDate = trainerWorkload.getTrainingDate();
            YearMonth yearMonth = YearMonth.from(trainingDate);
            String month = yearMonth.getMonth().toString();
            String year = String.valueOf(yearMonth.getYear());
            years.add(yearMonth.getYear());

            BigDecimal duration = trainerWorkload.getTrainingDuration();

            monthlySummaryMap.computeIfAbsent(year, k -> new HashMap<>())
                    .merge(month, duration, BigDecimal::add);
        }

        return createTrainerSummary(trainerWorkloads.getFirst(), years, monthlySummaryMap);
    }

    public TrainerSummary createTrainerSummary(TrainerWorkload trainerWorkload,
                                               HashSet<Integer> years, Map<String,
            Map<String, BigDecimal>> monthlySummaryMap) {
        TrainerSummary trainerSummary = new TrainerSummary();
        trainerSummary.setFirstName(trainerWorkload.getFirstName());
        trainerSummary.setLastName(trainerWorkload.getLastName());
        trainerSummary.setStatus(trainerWorkload.isActive());
        trainerSummary.setUsername(trainerWorkload.getUsername());
        trainerSummary.setYears(years.stream().sorted().toList());
        trainerSummary.setMonthlySummary(monthlySummaryMap);
        return trainerSummary;
    }

    @JmsListener(destination = "trainer-summary-queue")
    public JmsResponse<Message<TrainerSummary>> getTrainerSummary(@Payload String username, @Header("jms_correlationId") String transactionId) {
        MDC.put("transactionId", transactionId);
        Message<TrainerSummary> message = MessageBuilder
                .withPayload(calculate(username))
                .setHeader("jms_correlationId", transactionId)
                .build();
        JmsResponse<Message<TrainerSummary>> jmsResponse = JmsResponse.forQueue(message, "trainer-summary-calculated-queue");
        System.out.println(jmsResponse);
        return jmsResponse;
    }

}
