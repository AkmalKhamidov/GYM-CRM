package com.epamlearning.actuator.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEngagementMetrics {

    private final Counter newTraineeRegistrations;
    private final Counter newTrainerRegistrations;

    private final Counter newTrainingRegistrations;

    @Autowired
    public UserEngagementMetrics(MeterRegistry meterRegistry) {
        this.newTraineeRegistrations = Counter.builder("gym.trainee.registrations")
                .description("Number of new trainee registrations")
                .register(meterRegistry);

        this.newTrainerRegistrations = Counter.builder("gym.trainer.registrations")
                .description("Number of new trainer registrations")
                .register(meterRegistry);

        this.newTrainingRegistrations = Counter.builder("gym.training.registrations")
                .description("Number of new training registrations")
                .register(meterRegistry);
    }

    public void registerNewTrainee() {
        newTraineeRegistrations.increment();
    }

    public void registerNewTrainer() {
        newTrainerRegistrations.increment();
    }

    public void registerNewTraining() {
        newTrainingRegistrations.increment();
    }

}
