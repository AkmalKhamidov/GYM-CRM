package com.epamlearning.reportmicroservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "trainer_workloads")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class TrainerWorkload {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "is_active", nullable = false)
  private boolean isActive;

  @Column(name = "training_date", nullable = false)
  @Temporal(TemporalType.DATE)
  private LocalDate trainingDate;

  @Column(name = "training_duration",nullable = false)
  private BigDecimal trainingDuration;

  @Column(name = "trainee_username", nullable = false)
  private String traineeUsername;
}
