package com.epamlearning;

import com.epamlearning.configs.ApplicationConfig;
import com.epamlearning.services.TraineeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class App {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                ApplicationConfig.class
        );

        TraineeService traineeService = context.getBean(TraineeService.class);
        System.out.println(traineeService.findById(1L).getTrainers());

        System.out.println(traineeService.findByUsername("Linda.Cole"));

    }
}
