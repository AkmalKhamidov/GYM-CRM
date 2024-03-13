package com.epamlearning.reportmicroservice.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "trainer_summary")
@CompoundIndex(name = "firstname_lastname", def = "{'firstName': 1, 'lastName': 1}")
public class TrainerSummary {
  @Id
  private String id;
  private String username;
  private String firstName;
  private String lastName;
  private boolean status;
  private Map<Integer, Map<String, BigDecimal>> monthlySummary;
}
