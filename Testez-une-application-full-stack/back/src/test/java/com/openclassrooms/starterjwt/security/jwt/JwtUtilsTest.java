package com.openclassrooms.starterjwt.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = { "oc.app.jwtSecret=testSecretKey" })
class JwtUtilsTest {

  @Mock
  private Authentication authentication;

  @InjectMocks
  private JwtUtils jwtUtils;

  @Test
  void testGenerateJwtToken() {
    // Arrange
    UserDetailsImpl userDetails = createUserDetails();
    when(authentication.getPrincipal()).thenReturn(userDetails);

    ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000);
    ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecretKey");

    // Act
    String token = jwtUtils.generateJwtToken(authentication);

    // Assert
    assertThat(token).isNotNull();
  }

  @Test
  void testGetUserNameFromJwtToken() {
    // Arrange
    String token = generateTestToken();

    // Act
    String username = jwtUtils.getUserNameFromJwtToken(token);

    // Assert
    assertThat(username).isEqualTo("test@test.com");
  }

  @Test
  void testValidateJwtToken() {
    // Arrange
    String validToken = generateTestToken();
    String invalidToken = "invalidToken";

    // Act & Assert
    assertThat(jwtUtils.validateJwtToken(validToken)).isTrue();
    assertThat(jwtUtils.validateJwtToken(invalidToken)).isFalse();
  }

  private String generateTestToken() {
    UserDetailsImpl userDetails = createUserDetails();
    when(authentication.getPrincipal()).thenReturn(userDetails);

    ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000);
    ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecretKey");

    return jwtUtils.generateJwtToken(authentication);
  }

  private UserDetailsImpl createUserDetails() {
    UserDetailsImpl userDetails = new UserDetailsImpl(
      1L,
      "test@test.com",
      "firstname",
      "lastname",
      false,
      "password"
    );

    return userDetails;
  }
}
