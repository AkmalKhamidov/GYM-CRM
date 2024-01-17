package com.epamlearning.dtos.trainee.request;

import com.epamlearning.dtos.BaseDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeRegistrationRequestDTO
         implements BaseDTO {

        @NotNull(message = "User first name cannot be null.")
        @NotEmpty(message = "User first name cannot be empty.")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
        private String firstName;

        @NotNull(message = "User last name cannot be null.")
        @NotEmpty(message = "User last name cannot be empty.")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
        private String lastName;

        @DateTimeFormat(pattern = "dd-MM-yyyy")
        @Past(message = "Date of birth cannot be in the future.")
        private Date dateOfBirth;
        private String address;

}
