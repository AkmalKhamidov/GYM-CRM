package com.epamlearning.services;

import com.epamlearning.dtos.trainee.request.TraineeUpdateRequestDTO;
import com.epamlearning.dtos.trainee.response.TraineeProfileResponseDTO;
import com.epamlearning.dtos.trainee.response.TraineeRegistrationResponseDTO;
import com.epamlearning.entities.Trainee;
import com.epamlearning.entities.User;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.mapper.TraineeMapper;
import com.epamlearning.repositories.TraineeRepository;
import com.epamlearning.services.impl.AuthorizationServiceImpl;
import com.epamlearning.services.impl.TraineeServiceImpl;
import com.epamlearning.services.impl.TrainingServiceImpl;
import com.epamlearning.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TraineeServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;
    @Mock
    private TrainingServiceImpl trainingServiceImpl;

    @Mock
    private AuthorizationServiceImpl authService;
    @InjectMocks
    private TraineeServiceImpl traineeServiceImpl;

    private Trainee sampleTrainee;

    @BeforeEach
    void setUp() {
        sampleTrainee = new Trainee();
        sampleTrainee.setId(1L);
        sampleTrainee.setDateOfBirth(new Date());
        sampleTrainee.setAddress("Sample Address");

        User sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setUsername("sampleUsername");
        sampleUser.setPassword("samplePassword");
        sampleUser.setFirstName("John");
        sampleUser.setLastName("Doe");
        sampleUser.setActive(true);

        sampleTrainee.setUser(sampleUser);
    }

    @Test
    void findAll_ReturnsListOfTraineeProfileResponseDTOs() {
        // Arrange
        List<TraineeProfileResponseDTO> expectedTrainees = Arrays.asList(new TraineeProfileResponseDTO(), new TraineeProfileResponseDTO());
        List<Trainee> foundTrainee = List.of(new Trainee(), new Trainee());
        when(traineeRepository.findAll()).thenReturn(foundTrainee);
        when(traineeMapper.traineesToTraineeProfileResponseToDTOs(traineeRepository.findAll())).thenReturn(expectedTrainees);

        // Act
        List<TraineeProfileResponseDTO> result = traineeServiceImpl.findAll();

        // Assert
        assertEquals(expectedTrainees, result);
    }

    @Test
    void findByUsername_ValidUsername_ReturnsTraineeProfileResponseDTO() {
        // Arrange
        String username = "sampleUsername";
        TraineeProfileResponseDTO traineeProfileResponseDTO = traineeMapper.traineeToTraineeProfileResponseToDTO(sampleTrainee);
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.of(sampleTrainee));
        when(traineeMapper.traineeToTraineeProfileResponseToDTO(sampleTrainee)).thenReturn(traineeProfileResponseDTO);

        // Act
        TraineeProfileResponseDTO result = traineeServiceImpl.findByUsername(username);

        // Assert
        assertEquals(traineeProfileResponseDTO, result);
    }

    @Test
    void findByUsername_NullUsername_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> traineeServiceImpl.findByUsername(null));
    }

    @Test
    void findByUsername_EmptyUsername_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> traineeServiceImpl.findByUsername(""));
    }

    @Test
    void findByUsername_NonExistentUsername_ThrowsNotFoundException() {
        // Arrange
        String nonExistentUsername = "nonExistentUsername";
        when(traineeRepository.findTraineeByUserUsername(nonExistentUsername)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> traineeServiceImpl.findByUsername(nonExistentUsername));
    }

    @Test
    void update_ValidData_ReturnsUpdatedTrainee() {
        // Arrange
        String username = "sampleUsername";
        TraineeUpdateRequestDTO updatedTrainee = new TraineeUpdateRequestDTO();
        updatedTrainee.setDateOfBirth(new Date());
        updatedTrainee.setAddress("Updated Address");
        updatedTrainee.setFirstName("Updated John");
        updatedTrainee.setLastName("Updated Doe");
        updatedTrainee.setActive(false);

        sampleTrainee.setAddress("Updated Address");
        sampleTrainee.setDateOfBirth(new Date());
        User user = new User();
        user.setUsername("sampleUsername");
        user.setFirstName("Updated John");
        user.setLastName("Updated Doe");
        user.setActive(false);
        sampleTrainee.setUser(user);

        TraineeProfileResponseDTO traineeProfileResponseDTO = traineeMapper.traineeToTraineeProfileResponseToDTO(sampleTrainee);
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.of(sampleTrainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(sampleTrainee);
        when(traineeMapper.traineeToTraineeProfileResponseToDTO(sampleTrainee)).thenReturn(traineeProfileResponseDTO);
        // Act
        TraineeProfileResponseDTO result = traineeServiceImpl.update(username, updatedTrainee);

        // Assert
        assertEquals(traineeProfileResponseDTO, result);
    }

    @Test
    void update_NullTrainee_ThrowsNullPointerException() {
        String username = sampleTrainee.getUser().getUsername();
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.of(sampleTrainee));
        // Act & Assert
        assertThrows(NullPointerException.class, () -> traineeServiceImpl.update(username, null));
    }

    @Test
    void createTrainee_ValidData_ReturnsTrainee() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String address = "Sample Address";
        Date dateOfBirth = new Date();
        TraineeRegistrationResponseDTO traineeRegistrationResponseDTO = new TraineeRegistrationResponseDTO();
        traineeRegistrationResponseDTO.setUsername("sampleUsername");
        when(userServiceImpl.createUser(firstName, lastName)).thenReturn(sampleTrainee.getUser());
        when(traineeRepository.save(any(Trainee.class))).thenReturn(sampleTrainee);
        when(traineeMapper.traineeToTraineeRegistrationResponseDTO(sampleTrainee)).thenReturn(traineeRegistrationResponseDTO);
        // Act
        TraineeRegistrationResponseDTO result = traineeServiceImpl.createTrainee(firstName, lastName, address, dateOfBirth);

        // Assert
        assertNotNull(result);
        assertEquals("sampleUsername", result.getUsername());
    }


    @Test
    void deleteByUsername_ValidUsername_DeletesTrainee() {
        // Arrange
        String usernameToDelete = sampleTrainee.getUser().getUsername();
        doNothing().when(authService).authorizeUser(usernameToDelete);
        when(traineeRepository.findTraineeByUserUsername(usernameToDelete)).thenReturn(Optional.of(sampleTrainee));

        // Act
        traineeServiceImpl.deleteByUsername(usernameToDelete);

        // Assert
        verify(trainingServiceImpl, times(1)).deleteTrainingsByTraineeUsername(any());
        verify(traineeRepository, times(1)).delete(any());
    }

    @Test
    void deleteByUsername_UnauthorizedUser_ThrowsNotAuthenticatedException() {
        String usernameToDelete = sampleTrainee.getUser().getUsername();

        // Arrange
        doThrow(new NotAuthenticated("Unauthorized")).when(authService).authorizeUser(usernameToDelete);

        // Act & Assert
        assertThrows(NotAuthenticated.class, () -> traineeServiceImpl.deleteByUsername(usernameToDelete));
    }


}
