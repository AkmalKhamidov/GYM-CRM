package com.epamlearning.reportmicroservice.mappers;

import com.epamlearning.reportmicroservice.dtos.TrainerWorkloadRequestDTO;
import com.epamlearning.reportmicroservice.entities.TrainerWorkload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrainerWorkloadMapper {

  TrainerWorkloadMapper INSTANCE = Mappers.getMapper(TrainerWorkloadMapper.class);

  @Mapping(source = "dto.firstName", target = "firstName")
  TrainerWorkload TrainerWorkloadRequestDTOToTrainerWorkload(TrainerWorkloadRequestDTO dto);

}
