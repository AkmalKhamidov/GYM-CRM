package com.epamlearning.mapper;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.models.BaseModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    private final ModelMapper modelMapper;

    @Autowired
    public Mapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <R extends BaseDTO, T extends BaseModel> R mapToDTO(T t, Class<R> destinationType) {
        return modelMapper.map(t, destinationType);
    }

    public <R extends BaseModel, T extends BaseDTO> R mapToModel(T t, Class<R> destinationType) {
        return modelMapper.map(t, destinationType);
    }

}
