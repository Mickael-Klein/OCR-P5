package com.openclassrooms.starterjwt.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginIntTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthenticationManager authenticationManager;

  @MockBean
  private JwtUtils jwtUtils;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @MockBean
  private UserRepository userRepository;

  @Test
  public void testLogin_ShouldReturnJwtResponse() throws Exception {
    Authentication authentication = mock(Authentication.class);

    UserDetailsImpl userDetails = new UserDetailsImpl(
      10L,
      "test@test.com",
      "first",
      "last",
      true,
      "password"
    );
    when(authenticationManager.authenticate(any())).thenReturn(authentication);
    when(authenticationManager.authenticate(any()).getPrincipal())
      .thenReturn(userDetails);
    when(jwtUtils.generateJwtToken(any())).thenReturn("mockedToken");
    when(userRepository.findByEmail(any()))
      .thenReturn(
        Optional.of(
          new User("test@test.com", "lastname", "firstname", "password", true)
        )
      );

    String requestBody =
      "{ \"email\": \"test@test.com\", \"password\": \"password\" }";

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post("/api/auth/login")
          .content(requestBody)
          .contentType("application/json")
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());
  }

  @Test
  public void testLoginWithWrongCredentials_ShouldReturnUnauthorized()
    throws Exception {
    when(authenticationManager.authenticate(any()))
      .thenThrow(new BadCredentialsException("Bad credentials"));

    String requestBody =
      "{ \"email\": \"test@test.com\", \"password\": \"wrongpassword\" }";

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post("/api/auth/login")
          .content(requestBody)
          .contentType("application/json")
      )
      .andExpect(MockMvcResultMatchers.status().isUnauthorized())
      .andExpect(MockMvcResultMatchers.jsonPath("$.token").doesNotExist())
      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(401))
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.error").value("Unauthorized")
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.message").value("Bad credentials")
      );
  }
}
