package com.epamlearning.mapper;

import com.epamlearning.dtos.trainee.request.TraineeUpdateRequestDTO;
import com.epamlearning.dtos.trainee.response.TraineeListResponseDTO;
import com.epamlearning.dtos.trainee.response.TraineeProfileResponseDTO;
import com.epamlearning.dtos.trainee.response.TraineeRegistrationResponseDTO;
import com.epamlearning.entities.Trainee;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TrainerMapper.class})
public interface TraineeMapper {
    TraineeMapper INSTANCE = Mappers.getMapper(TraineeMapper.class);

    @Mappings({
            @Mapping(source = "user.firstName", target = "firstName"),
            @Mapping(source = "user.lastName", target = "lastName"),
            @Mapping(source = "user.active", target = "active"),
            @Mapping(source = "trainers", target = "trainerList")
    })
    TraineeProfileResponseDTO traineeToTraineeProfileResponseToDTO(Trainee trainee);

    @InheritConfiguration(name = "traineeToTraineeProfileResponseToDTO")
    List<TraineeProfileResponseDTO> traineesToTraineeProfileResponseToDTOs(List<Trainee> trainees);

//    @InheritInverseConfiguration(name = "traineeToTraineeProfileResponseToDTO")
//    Trainee traineeProfileResponseDTOToTrainee(TraineeProfileResponseDTO traineeProfileResponseDTO);

    @Mappings({
            @Mapping(source = "user.username", target = "username"),
            @Mapping(source = "user.firstName", target = "firstName"),
            @Mapping(source = "user.lastName", target = "lastName"),
    })
    TraineeListResponseDTO traineeToTraineeListResponseDTO(Trainee trainee);

//    @InheritConfiguration(name = "traineeToTraineeListResponseDTO")
//    List<TraineeListResponseDTO> traineesToTraineeListResponseDTOs(List<Trainee> trainees);


    @InheritInverseConfiguration(name = "traineeToTraineeListResponseDTO")
    Trainee traineeListDTOToTrainee(TraineeListResponseDTO traineeListResponseDTO);

    @Mappings({
            @Mapping(source = "firstName", target = "user.firstName"),
            @Mapping(source = "lastName", target = "user.lastName"),
            @Mapping(source = "active", target = "user.active")
    })
    Trainee traineeUpdateRequestDTOToTrainee(TraineeUpdateRequestDTO traineeUpdateRequestDTO);

    @Mappings({
            @Mapping(source = "user.username", target = "username"),
            @Mapping(source = "user.password", target = "password")
    })
    TraineeRegistrationResponseDTO traineeToTraineeRegistrationResponseDTO(Trainee trainee);

}
