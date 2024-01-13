package com.epamlearning.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "trainings")
@NoArgsConstructor
@Getter
@Setter
public class Training implements BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "training_name",nullable = false)
    private String trainingName;

    @Column(name = "training_date",nullable = false)
    @Temporal(TemporalType.DATE)
    private Date trainingDate;

    @Column(name = "training_duration",nullable = false)
    private BigDecimal trainingDuration;

    @ManyToOne
    @JoinColumn(name="trainer_id", referencedColumnName = "id")
    @Cascade({CascadeType.PERSIST, CascadeType.MERGE})
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name="trainee_id", referencedColumnName = "id")
    @Cascade({CascadeType.PERSIST, CascadeType.MERGE})
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name="training_type_id", referencedColumnName = "id")
    private TrainingType trainingType;

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", trainingName='" + trainingName + '\'' +
                ", trainingDate=" + trainingDate +
                ", trainingDuration=" + trainingDuration +
                ", trainer=" + trainer +
                ", trainee=" + trainee +
                ", trainingType=" + trainingType +
                '}';
    }
}
