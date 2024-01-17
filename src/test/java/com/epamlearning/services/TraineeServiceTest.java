package com.epamlearning.services;

//import com.epamlearning.daos.TraineeDAOImpl;

import com.epamlearning.models.Trainee;
import com.epamlearning.models.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class TraineeServiceTest {

//    @Mock
//    private TraineeDAOImpl traineeDAO;

    @Mock
    private UserService userService;

    @InjectMocks
    private TraineeService traineeService;

    @Mock
    private TrainingService trainingService;

    private User user;
    private Trainee trainee;
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        user = new User();
//        user.setId(999L);
//        user.setUsername("TestUsername.TestLastName");
//        user.setFirstName("TestFirstName");
//        user.setLastName("TestLastName");
//        user.setPassword("testPassword");
//        user.setActive(true);
//        trainee = new Trainee();
//        trainee.setId(999L);
//        trainee.setUser(user);
//        trainee.setAddress("TestAddress");
//        trainee.setDateOfBirth(new Calendar.Builder().setDate(2000,12,10).build().getTime());
//    }
//
//    @Test
//    void saveTrainee() {
//        when(traineeDAO.saveOrUpdate(trainee)).thenReturn(Optional.of(trainee));
//
//        Optional<Trainee> savedTrainee = traineeService.save(trainee);
//
//        assertTrue(savedTrainee.isPresent());
//        assertEquals(trainee, savedTrainee.get());
//    }
//
//    @Test
//    void updateTrainee() {
//        long traineeId = 999L;
//        trainee.setAddress("NewTestAddress");
//        trainee.getUser().setUsername("NewTestUsername.NewTestLastName");
//        when(traineeDAO.findById(anyLong())).thenReturn(Optional.of(trainee));
//        when(traineeDAO.saveOrUpdate(any(Trainee.class))).thenReturn(Optional.of(trainee));
//
//        Optional<Trainee> traineeUpdated = traineeService.update(traineeId, trainee);
//
//        assertTrue(traineeUpdated.isPresent());
//        assertEquals(trainee, traineeUpdated.get());
//    }
//
//    @Test
//    void findTraineeById() {
//        long traineeId = 1L;
//        Trainee trainee = new Trainee();
//        when(traineeDAO.findById(traineeId)).thenReturn(Optional.of(trainee));
//
//        Optional<Trainee> foundTrainee = traineeService.findById(traineeId);
//
//        assertTrue(foundTrainee.isPresent());
//        assertEquals(trainee, foundTrainee.get());
//    }
//
//    @Test
//    void findAllTrainees() {
//        List<Optional<Trainee>> trainees = new ArrayList<>();
//        when(traineeDAO.findAll()).thenReturn(trainees);
//
//        List<Optional<Trainee>> foundTrainees = traineeService.findAll();
//
//        assertEquals(trainees, foundTrainees);
//    }
//
////    @Test
////    void deleteTraineeById() {
////        long traineeId = 999L;
////        when(traineeDAO.findById(traineeId)).thenReturn(Optional.of(trainee));
////        assertDoesNotThrow(() -> traineeService.deleteById(traineeId));
////        verify(traineeDAO, times(1)).delete(trainee);
////    }
//
//    @Test
//    void deleteTraineeByIdNotFound() {
//        long traineeId = 1L;
//        when(traineeDAO.findById(traineeId)).thenReturn(Optional.empty());
//
//        NotFoundException exception = assertThrows(NotFoundException.class, () -> traineeService.deleteById(traineeId));
//        assertEquals("Trainee with ID " + traineeId + " not found.", exception.getMessage());
//        verify(traineeDAO, never()).delete(any(Trainee.class));
//    }
//
//    @Test
//    void findByUsername() {
//        String username = "testUser";
//        when(traineeDAO.findByUserName(username)).thenReturn(Optional.of(trainee));
//
//        Optional<Trainee> foundTrainee = traineeService.findByUsername(username);
//
//        assertTrue(foundTrainee.isPresent());
//        assertEquals(trainee, foundTrainee.get());
//    }
//
//    @Test
//    void findByUsernameNotFound() {
//        String username = "nonExistentUser";
//        when(traineeDAO.findByUserName(username)).thenReturn(Optional.empty());
//
//        NotFoundException exception = assertThrows(NotFoundException.class, () -> traineeService.findByUsername(username));
//        assertEquals("Trainee with username " + username + " not found.", exception.getMessage());
//    }
//
//    @Test
//    void authenticateTrainee() {
//        String username = "testUser";
//        String password = "testPassword";
//        when(traineeDAO.findByUserName(username)).thenReturn(Optional.of(trainee));
//
//        assertEquals(trainee.getId(), traineeService.authenticate(username, password));
//    }
//
//    @Test
//    void authenticateTraineeNotFound() {
//        String username = "nonExistentUser";
//        String password = "testPassword";
//        when(traineeDAO.findByUserName(username)).thenReturn(Optional.empty());
//
//        NotFoundException exception = assertThrows(NotFoundException.class, () -> traineeService.authenticate(username, password));
//        assertEquals("Trainee with username " + username + " not found.", exception.getMessage());
//    }
//
//    @Test
//    void authenticateTraineeWrongPassword() {
//        String username = "testUser";
//        String correctPassword = "correctPassword";
//        String wrongPassword = "wrongPassword";
//        Trainee trainee = new Trainee();
//        user.setPassword(correctPassword);
//        trainee.setUser(user);
//        when(traineeDAO.findByUserName(username)).thenReturn(Optional.of(trainee));
//
//        NotAuthenticated exception = assertThrows(NotAuthenticated.class, () -> traineeService.authenticate(username, wrongPassword));
//        assertEquals("Wrong password. Username: " + username, exception.getMessage());
//    }
//
//    @Test
//    void updateTraineeActiveStatus() {
//        long traineeId = 999L;
//        boolean newActiveStatus = true;
//        trainee.getUser().setActive(newActiveStatus);
//        when(traineeDAO.findById(traineeId)).thenReturn(Optional.of(trainee));
//        when(traineeDAO.saveOrUpdate(any(Trainee.class))).thenReturn(Optional.of(trainee));
//
//        Optional<Trainee> traineeUpdated = traineeService.updateActive(traineeId, newActiveStatus);
//
//        assertTrue(traineeUpdated.isPresent());
//        assertEquals(trainee, traineeUpdated.get());
//        verify(traineeDAO, times(1)).saveOrUpdate(any(Trainee.class));
//    }
//
//    @Test
//    void updateTraineePassword() {
//        long traineeId = 1L;
//        String newPassword = "newPassword";
//        trainee.getUser().setPassword(newPassword);
//        when(traineeDAO.findById(traineeId)).thenReturn(Optional.of(trainee));
//        when(traineeDAO.saveOrUpdate(any(Trainee.class))).thenReturn(Optional.of(trainee));
//
//        Optional<Trainee> traineeUpdated = traineeService.updatePassword(traineeId, newPassword);
//
//        assertTrue(traineeUpdated.isPresent());
//        assertEquals(trainee, traineeUpdated.get());
//        verify(traineeDAO, times(1)).saveOrUpdate(any(Trainee.class));
//    }
}
