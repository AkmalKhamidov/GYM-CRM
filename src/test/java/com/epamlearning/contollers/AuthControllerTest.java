package com.epamlearning.contollers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epamlearning.controllers.AuthController;
import com.epamlearning.dtos.SessionDTO;
import com.epamlearning.dtos.user.RefreshRequestDTO;
import com.epamlearning.dtos.user.UserAuthDTO;
import com.epamlearning.dtos.user.UserChangePasswordDTO;
import com.epamlearning.security.JWTUtil;
import com.epamlearning.services.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
        .addPlaceholderValue("server.servlet.context-path", "")
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
  void refresh_shouldReturnSessionDTO() throws Exception {
    RefreshRequestDTO refreshRequestDTO = new RefreshRequestDTO("refreshToken");
    SessionDTO sessionDTO = new SessionDTO(new Date(), new Date(), "newAccessToken", new Date(),
        new Date(), "newRefreshToken");

    String username = "username";
    when(jwtUtil.extractUsername(refreshRequestDTO.refreshToken())).thenReturn(username);
    when(jwtUtil.validateToken(refreshRequestDTO.refreshToken())).thenReturn(true);
    when(userService.generateTokens(username)).thenReturn(sessionDTO);

    mockMvc.perform(post("/auth/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(refreshRequestDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value(sessionDTO.accessToken()))
        .andExpect(jsonPath("$.refreshToken").value(sessionDTO.refreshToken()));

    verify(userService, times(1)).generateTokens(username);
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
