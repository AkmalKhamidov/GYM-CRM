package com.epamlearning.microservices.report.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrainerSummaryDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean status;
    private List<Integer> years;
    private Map<String, Map<String, BigDecimal>> monthlySummary;
}
