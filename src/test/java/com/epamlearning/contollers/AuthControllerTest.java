package com.epamlearning.contollers;

import com.epamlearning.controllers.AuthController;
import com.epamlearning.dtos.SessionDTO;
import com.epamlearning.dtos.user.RefreshRequestDTO;
import com.epamlearning.dtos.user.UserAuthDTO;
import com.epamlearning.dtos.user.UserChangePasswordDTO;
import com.epamlearning.security.JWTUtil;
import com.epamlearning.services.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {

  @Mock
  private UserServiceImpl userService;

  @Mock
  private JWTUtil jwtUtil;

  @Mock
  private AuthenticationManager authenticationManager;

  @InjectMocks
  private AuthController authController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(authController)
        .build();
  }

  @Test
  void login_shouldReturnSessionDTO() throws Exception {
    UserAuthDTO userAuthDTO = new UserAuthDTO("username", "password");
    SessionDTO sessionDTO = new SessionDTO(new Date(), new Date(), "accessToken", new Date(),
        new Date(), "refreshToken");

    when(userService.authenticate(anyString(), anyString())).thenReturn(sessionDTO);

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userAuthDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value(sessionDTO.accessToken()))
        .andExpect(jsonPath("$.refreshToken").value(sessionDTO.refreshToken()));

    verify(userService, times(1)).authenticate(anyString(), anyString());
  }

  @Test
  void logout_shouldReturnOk() throws Exception {
    mockMvc.perform(post("/auth/logout"))
            .andExpect(status().isOk());

    verify(userService, times(1)).logout(any(), any());
  }

  @Test
  void refresh_shouldReturnSessionDTO() throws Exception {
    RefreshRequestDTO refreshRequestDTO = new RefreshRequestDTO("refreshToken");
    SessionDTO sessionDTO = new SessionDTO(new Date(), new Date(), "newAccessToken", new Date(),
        new Date(), "newRefreshToken");

    String username = "username";
    when(userService.refreshToken(refreshRequestDTO)).thenReturn(sessionDTO);

    mockMvc.perform(post("/auth/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(refreshRequestDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value(sessionDTO.accessToken()))
        .andExpect(jsonPath("$.refreshToken").value(sessionDTO.refreshToken()));

    verify(userService, times(1)).refreshToken(any(RefreshRequestDTO.class));
  }

  @Test
  void changePassword_shouldReturnOk() throws Exception {
    UserChangePasswordDTO userChangePasswordDTO = new UserChangePasswordDTO("username",
        "oldPassword", "newPassword");

    mockMvc.perform(put("/auth/change-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userChangePasswordDTO)))
        .andExpect(status().isOk());

    verify(userService, times(1)).updatePassword(anyString(), anyString(), anyString());
  }

  // Helper method to convert objects to JSON string
  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
