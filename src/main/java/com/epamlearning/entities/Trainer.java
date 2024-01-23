package com.epamlearning.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "trainers")
@NoArgsConstructor
@Getter
@Setter
public class Trainer implements BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Cascade({CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
    private User user;

    @ManyToOne
    @JoinColumn(name="specialization", referencedColumnName = "id")
    private TrainingType specialization;

    @ManyToMany(mappedBy = "trainers", fetch = FetchType.EAGER)
    private List<Trainee> trainees = new ArrayList<>();

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + id +
                ", user=" + user +
                ", specialization=" + specialization +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return Objects.equals(id, trainer.id) && Objects.equals(user, trainer.user) && Objects.equals(specialization, trainer.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, specialization);
    }
}
