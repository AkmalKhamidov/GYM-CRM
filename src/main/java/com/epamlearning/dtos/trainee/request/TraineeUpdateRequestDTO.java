package com.epamlearning.dtos.trainee.request;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.dtos.user.ProfileDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TraineeUpdateRequestDTO extends ProfileDTO implements BaseDTO {
    @Schema(description = "Trainee date of birth", example = "2000-01-02")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @Schema(description = "Trainee address", example = "Some address")
    private String address;
}
