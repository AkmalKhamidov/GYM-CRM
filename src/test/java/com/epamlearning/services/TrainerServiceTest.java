package com.epamlearning.services;

//import com.epamlearning.daos.TrainerDAOImpl;

import com.epamlearning.models.Trainer;
import com.epamlearning.models.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TrainerServiceTest {

//    @Mock
//    private TrainerDAOImpl trainerDAO;

    @Mock
    private UserService userService;

    @InjectMocks
    private TrainerService trainerService;

    @Mock
    private TraineeService traineeService;
    private User user;

    private Trainer trainer;
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        TrainingType trainingType = new TrainingType();
//        trainingType.setId(999L);
//        trainingType.setTrainingTypeName("TestTrainingTypeName");
//        user = new User();
//        user.setId(999L);
//        user.setUsername("TestUsername");
//        user.setFirstName("TestFirstName");
//        user.setLastName("TestLastName");
//        user.setPassword("testPassword");
//        user.setActive(true);
//        trainer = new Trainer();
//        trainer.setId(999L);
//        trainer.setUser(user);
//        trainer.setSpecialization(trainingType);
//    }
//
//    @Test
//    void saveTrainer() {
//        when(trainerDAO.saveOrUpdate(trainer)).thenReturn(Optional.of(trainer));
//
//        Optional<Trainer> savedTrainer = trainerService.save(trainer);
//
//        assertTrue(savedTrainer.isPresent());
//        assertEquals(trainer, savedTrainer.get());
//    }
//
//    @Test
//    void updateTrainer() {
//        long trainerId = 999L;
//        TrainingType trainingType = new TrainingType();
//        trainingType.setId(999L);
//        trainingType.setTrainingTypeName("NewTestTrainingTypeName");
//        trainer.getUser().setUsername("NewTestUsername");
//        trainer.setSpecialization(trainingType);
//        when(trainerDAO.findById(anyLong())).thenReturn(Optional.of(trainer));
//        when(trainerDAO.saveOrUpdate(any(Trainer.class))).thenReturn(Optional.of(trainer));
//
//        Optional<Trainer> trainerUpdated = trainerService.update(trainerId, trainer);
//
//        assertTrue(trainerUpdated.isPresent());
//        assertEquals(trainer, trainerUpdated.get());
//    }
//
//    @Test
//    void findTrainerById() {
//        long trainerId = 999L;
//        Trainer trainer = new Trainer();
//        when(trainerDAO.findById(trainerId)).thenReturn(Optional.of(trainer));
//
//        Optional<Trainer> foundTrainer = trainerService.findById(trainerId);
//
//        assertTrue(foundTrainer.isPresent());
//        assertEquals(trainer, foundTrainer.get());
//    }
//
//    @Test
//    void findAllTrainers() {
//        List<Optional<Trainer>> trainers = new ArrayList<>();
//        when(trainerDAO.findAll()).thenReturn(trainers);
//
//        List<Optional<Trainer>> foundTrainers = trainerService.findAll();
//
//        assertEquals(trainers, foundTrainers);
//    }
//
//    @Test
//    void deleteTrainerById() {
//        long trainerId = 999L;
//        when(trainerDAO.findById(trainerId)).thenReturn(Optional.of(trainer));
//
//        assertDoesNotThrow(() -> trainerService.deleteById(trainerId));
//        verify(trainerDAO, times(1)).delete(trainer);
//    }
//
//    @Test
//    void deleteTrainerByIdNotFound() {
//        long trainerId = 999L;
//        when(trainerDAO.findById(trainerId)).thenReturn(Optional.empty());
//
//        NotFoundException exception = assertThrows(NotFoundException.class, () -> trainerService.deleteById(trainerId));
//        assertEquals("Trainer with ID " + trainerId + " not found.", exception.getMessage());
//        verify(trainerDAO, never()).delete(trainer);
//    }
//
//    @Test
//    void findByUsername() {
//        String username = "testUser";
//        Trainer trainer = new Trainer();
//        when(trainerDAO.findByUserName(username)).thenReturn(Optional.of(trainer));
//
//        Optional<Trainer> foundTrainer = trainerService.findByUsername(username);
//
//        assertTrue(foundTrainer.isPresent());
//        assertEquals(trainer, foundTrainer.get());
//    }
//
//    @Test
//    void findByUsernameNotFound() {
//        String username = "nonExistentUser";
//        when(trainerDAO.findByUserName(username)).thenReturn(Optional.empty());
//
//        NotFoundException exception = assertThrows(NotFoundException.class, () -> trainerService.findByUsername(username));
//        assertEquals("Trainer with username " + username + " not found.", exception.getMessage());
//    }
//
//    @Test
//    void authenticateTrainer() {
//        String username = "testUser";
//        String password = "testPassword";
//        when(trainerDAO.findByUserName(username)).thenReturn(Optional.of(trainer));
//
//        assertEquals(trainer.getId(), trainerService.authenticate(username, password));
//    }
//
//    @Test
//    void authenticateTrainerNotFound() {
//        String username = "nonExistentUser";
//        String password = "testPassword";
//        when(trainerDAO.findByUserName(username)).thenReturn(Optional.empty());
//
//        NotFoundException exception = assertThrows(NotFoundException.class, () -> trainerService.authenticate(username, password));
//        assertEquals("Trainer with username " + username + " not found.", exception.getMessage());
//    }
//
//    @Test
//    void authenticateTrainerWrongPassword() {
//        String username = "testUser";
//        String correctPassword = "correctPassword";
//        String wrongPassword = "wrongPassword";
//        Trainer trainer = new Trainer();
//        user.setPassword(correctPassword);
//        trainer.setUser(user);
//        when(trainerDAO.findByUserName(username)).thenReturn(Optional.of(trainer));
//
//        NotAuthenticated exception = assertThrows(NotAuthenticated.class, () -> trainerService.authenticate(username, wrongPassword));
//        assertEquals("Wrong password. Username: " + username, exception.getMessage());
//    }
//
//    @Test
//    void updateTrainerActiveStatus() {
//        long trainerId = 999L;
//        boolean newActiveStatus = false;
//        trainer.getUser().setActive(newActiveStatus);
//        when(trainerDAO.findById(trainerId)).thenReturn(Optional.of(trainer));
//        when(trainerDAO.saveOrUpdate(any(Trainer.class))).thenReturn(Optional.of(trainer));
//
//        Optional<Trainer> trainerUpdated = trainerService.updateActive(trainerId, newActiveStatus);
//
//        assertTrue(trainerUpdated.isPresent());
//        assertEquals(trainer, trainerUpdated.get());
//        verify(trainerDAO, times(1)).saveOrUpdate(any(Trainer.class));
//    }
//
//    @Test
//    void updateTrainerPassword() {
//        long trainerId = 999L;
//        String newPassword = "newPassword";
//        trainer.getUser().setPassword(newPassword);
//        when(trainerDAO.findById(trainerId)).thenReturn(Optional.of(trainer));
//        when(trainerDAO.saveOrUpdate(any(Trainer.class))).thenReturn(Optional.of(trainer));
//
//        Optional<Trainer> trainerUpdated = trainerService.updatePassword(trainerId, newPassword);
//
//        assertTrue(trainerUpdated.isPresent());
//        assertEquals(trainer, trainerUpdated.get());
//        verify(trainerDAO, times(1)).saveOrUpdate(any(Trainer.class));
//    }
//
//    @Test
//    void findNotAssignedActiveTrainers() {
//        Long traineeId = 999L;
//        Trainee trainee = new Trainee();
//        trainee.setId(traineeId);
//
//        List<Optional<Trainer>> notAssignedActiveTrainers = List.of(Optional.of(trainer));
//        when(traineeService.findById(traineeId)).thenReturn(Optional.of(trainee));
//        when(trainerDAO.findNotAssignedActiveTrainers(trainee)).thenReturn(notAssignedActiveTrainers);
//
//        List<Optional<Trainer>> foundTrainers = trainerService.findNotAssignedActiveTrainers(traineeId);
//
//        assertEquals(notAssignedActiveTrainers, foundTrainers);
//    }
}
