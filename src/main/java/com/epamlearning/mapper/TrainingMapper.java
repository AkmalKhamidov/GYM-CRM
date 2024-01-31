package com.epamlearning.mapper;

import com.epamlearning.dtos.training.response.TraineeTrainingsResponseDTO;
import com.epamlearning.dtos.training.response.TrainerTrainingsResponseDTO;
import com.epamlearning.entities.Training;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

    TrainingMapper INSTANCE = Mappers.getMapper(TrainingMapper.class);

    @Mappings({
            @Mapping(source = "trainingType.trainingTypeName", target = "trainingTypeName"),
            @Mapping(expression = "java(training.getTrainer().getUser().getFirstName() + ' ' + training.getTrainer().getUser().getLastName())", target = "trainerFullName")
    })
    TraineeTrainingsResponseDTO trainingToTraineeTrainingsResponseDTO(Training training);

    @InheritConfiguration(name = "trainingToTraineeTrainingsResponseDTO")
    List<TraineeTrainingsResponseDTO> trainingsToTraineeTrainingsResponseDTOs(List<Training> trainings);


    @Mappings({
            @Mapping(source = "trainingType.trainingTypeName", target = "trainingTypeName"),
            @Mapping(expression = "java(training.getTrainee().getUser().getFirstName() + ' ' + training.getTrainee().getUser().getLastName())", target = "traineeFullName")
    })
    TrainerTrainingsResponseDTO trainingToTrainerTrainingsResponseDTO(Training training);

    @InheritConfiguration(name = "trainingToTraineeTrainingsResponseDTO")
    List<TrainerTrainingsResponseDTO> trainingsToTrainerTrainingsResponseDTOs(List<Training> trainings);

}
