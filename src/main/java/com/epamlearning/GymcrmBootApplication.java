package com.epamlearning;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class GymcrmBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymcrmBootApplication.class, args);
    }


//    public TraineeProfileResponseDTO getTraineeProfile(Long id) {
//        Trainee trainee = new Trainee();
//
//        User user = new User();
//        user.setUsername("username");
//        user.setPassword("password");
//        user.setFirstName("firstName");
//        user.setLastName("lastName");
//        user.setActive(true);
//
//        trainee.setId(10L);
//        trainee.setUser(user);
//        trainee.setAddress("address");
//        trainee.setDateOfBirth(new Date());
//        trainee.setTrainers(null);
//
//        return (TraineeProfileResponseDTO) traineeMapper.toDTO(trainee);
//    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
