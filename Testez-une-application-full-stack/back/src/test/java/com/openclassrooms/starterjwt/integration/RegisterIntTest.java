package com.openclassrooms.starterjwt.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterIntTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @MockBean
  private UserRepository userRepository;

  @Test
  public void testRegisterUser_ShouldReturnSuccess() throws Exception {
    String requestBody =
      "{ \"email\": \"newuser@test.com\", \"firstName\": \"firstname\", \"lastName\": \"lastname\", \"password\": \"password\" }";

    when(userRepository.existsByEmail(any())).thenReturn(false);
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post("/api/auth/register")
          .content(requestBody)
          .contentType("application/json")
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(
        MockMvcResultMatchers
          .jsonPath("$.message")
          .value("User registered successfully!")
      );
  }

  @Test
  public void testRegisterUser_WithDuplicateEmail_ShouldReturnBadRequest()
    throws Exception {
    String requestBody =
      "{ \"email\": \"existinguser@test.com\", \"firstName\": \"firstname\", \"lastName\": \"lastname\", \"password\": \"password\" }";

    when(userRepository.existsByEmail(any())).thenReturn(true);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post("/api/auth/register")
          .content(requestBody)
          .contentType("application/json")
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(
        MockMvcResultMatchers
          .jsonPath("$.message")
          .value("Error: Email is already taken!")
      );
  }
}
