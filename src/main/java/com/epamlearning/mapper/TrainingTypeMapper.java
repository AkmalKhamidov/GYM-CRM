package com.epamlearning.mapper;

import com.epamlearning.dtos.trainingtype.response.TrainingTypeResponseDTO;
import com.epamlearning.entities.TrainingType;
import java.util.List;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {

    TrainingTypeMapper INSTANCE = Mappers.getMapper(TrainingTypeMapper.class);

    @Mapping(source = "id", target = "trainingTypeId")
    TrainingTypeResponseDTO trainingTypeToTrainingTypeResponseDTO(TrainingType trainingType);

    @InheritConfiguration(name = "trainingTypeToTrainingTypeResponseDTO")
    List<TrainingTypeResponseDTO> trainingTypesToTrainingTypeResponseDTOs(List<TrainingType> trainingTypes);

}
