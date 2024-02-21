package com.epamlearning.mapper;

import com.epamlearning.dtos.trainer.request.TrainerUpdateRequestDTO;
import com.epamlearning.dtos.trainer.request.TrainerUsernameRequestDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerProfileResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerRegistrationResponseDTO;
import com.epamlearning.entities.Trainer;
import com.epamlearning.entities.enums.TrainingTypeName;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TrainerMapperHelper.class})
public interface TrainerMapper {
    TrainerMapper INSTANCE = Mappers.getMapper(TrainerMapper.class);

    @Mappings({
            @Mapping(source = "user.firstName", target = "firstName"),
            @Mapping(source = "user.lastName", target = "lastName"),
            @Mapping(source = "specialization.trainingTypeName", target = "trainingTypeName"),
            @Mapping(source = "user.active", target = "active"),
            @Mapping(source = "trainees", target = "trainees")
    })
    TrainerProfileResponseDTO trainerToTrainerProfileResponseDTO(Trainer trainer);

    @InheritConfiguration(name = "trainerToTrainerProfileResponseDTO")
    List<TrainerProfileResponseDTO> trainersToTrainerProfileResponseDTOs(List<Trainer> trainers);

    @Mappings({
            @Mapping(source = "user.username", target = "username"),
            @Mapping(source = "user.firstName", target = "firstName"),
            @Mapping(source = "user.lastName", target = "lastName"),
            @Mapping(source = "specialization.trainingTypeName", target = "trainingTypeName")
    })
    TrainerListResponseDTO trainerToTrainerListResponseDTO(Trainer trainer);

    @InheritInverseConfiguration(name = "trainerToTrainerListResponseDTO")
    Trainer trainerListResponseDTOToTrainer(TrainerListResponseDTO trainerListResponseDTO);
    @InheritConfiguration(name = "trainerToTrainerListResponseDTO")
    List<TrainerListResponseDTO> trainersToTrainerListResponseDTOs(List<Trainer> trainers);

    @InheritInverseConfiguration(name = "trainersToTrainerListResponseDTOs")
    List<Trainer> trainerListResponseDTOsToTrainers(List<TrainerListResponseDTO> trainerListResponseDTOs);

    @Mappings({
            @Mapping(source = "user.username", target = "username"),
            @Mapping(source = "user.password", target = "password")
    })
    TrainerRegistrationResponseDTO trainerToTrainerRegistrationResponseDTO(Trainer trainer);

    @Mappings({
            @Mapping(source = "firstName", target = "user.firstName"),
            @Mapping(source = "lastName", target = "user.lastName"),
            @Mapping(source = "trainingTypeName", target = "specialization.trainingTypeName", resultType = TrainingTypeName.class),
            @Mapping(source = "active", target = "user.active"),
    })
    Trainer trainerUpdateRequestDTOToTrainer(TrainerUpdateRequestDTO trainerUpdateRequestDTO);

    @Mappings({
            @Mapping(source = "user.username", target = "username"),
    })
    List<TrainerUsernameRequestDTO> trainersToTrainerUsernameRequestDTOs(List<Trainer> trainers);

}
