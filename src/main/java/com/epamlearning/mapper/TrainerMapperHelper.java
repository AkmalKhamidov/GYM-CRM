package com.epamlearning.mapper;

import com.epamlearning.dtos.trainee.response.TraineeListResponseDTO;
import com.epamlearning.entities.Trainee;
import java.util.List;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrainerMapperHelper {

    TrainerMapperHelper INSTANCE = Mappers.getMapper(TrainerMapperHelper.class);

    @Mappings({
            @Mapping(source = "user.username", target = "username"),
            @Mapping(source = "user.firstName", target = "firstName"),
            @Mapping(source = "user.lastName", target = "lastName"),
    })
    TraineeListResponseDTO traineeToTraineeListResponseDTO(Trainee trainee);

    @InheritConfiguration(name = "traineeToTraineeListResponseDTO")
    List<TraineeListResponseDTO> traineesToTraineeListResponseDTOs(List<Trainee> trainees);


}
