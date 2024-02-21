package com.epamlearning.reportmicroservice.entities;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrainerSummary {
  private String username;
  private String firstName;
  private String lastName;
  private boolean status;
  private List<Integer> years;
  private Map<String, Map<String, BigDecimal>> monthlySummary;
}
