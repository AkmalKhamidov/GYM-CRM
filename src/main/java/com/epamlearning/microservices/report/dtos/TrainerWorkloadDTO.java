package com.epamlearning.microservices.report.dtos;

import com.epamlearning.microservices.report.ActionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrainerWorkloadDTO {

  private String username;
  private String firstName;
  private String lastName;
  private boolean isActive;
  private LocalDate trainingDate;
  private BigDecimal trainingDuration;
  private ActionType actionType;
  private String traineeUsername;

}
