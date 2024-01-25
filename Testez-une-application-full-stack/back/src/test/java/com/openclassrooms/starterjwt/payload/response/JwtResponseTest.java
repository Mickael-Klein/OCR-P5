package com.openclassrooms.starterjwt.payload.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class JwtResponseTest {

  @Test
  void testJwtResponseConstructorAndGetters() {
    // Arrange
    String accessToken = "mockAccessToken";
    Long id = 1L;
    String username = "username";
    String firstName = "firstname";
    String lastName = "lastname";
    Boolean admin = true;

    // Act
    JwtResponse jwtResponse = new JwtResponse(
      accessToken,
      id,
      username,
      firstName,
      lastName,
      admin
    );

    // Assert
    assertThat(jwtResponse.getToken()).isEqualTo(accessToken);
    assertThat(jwtResponse.getId()).isEqualTo(id);
    assertThat(jwtResponse.getUsername()).isEqualTo(username);
    assertThat(jwtResponse.getFirstName()).isEqualTo(firstName);
    assertThat(jwtResponse.getLastName()).isEqualTo(lastName);
    assertThat(jwtResponse.getAdmin()).isEqualTo(admin);
    assertThat(jwtResponse.getType()).isEqualTo("Bearer");
  }

  @Test
  void testJwtResponseSetters() {
    // Arrange
    String accessToken = "mockAccessToken";
    Long id = 1L;
    String username = "username";
    String firstName = "firstname";
    String lastName = "lastname";
    Boolean admin = true;

    // Act
    JwtResponse jwtResponse = new JwtResponse(
      accessToken,
      id,
      username,
      firstName,
      lastName,
      admin
    );

    jwtResponse.setAdmin(false);
    jwtResponse.setFirstName("firstName2");
    jwtResponse.setId(2L);
    jwtResponse.setLastName("lastName2");
    jwtResponse.setToken("new Token");
    jwtResponse.setUsername("username2");
    jwtResponse.setType("other type");

    // Assert
    assertThat(jwtResponse.getToken()).isEqualTo("new Token");
    assertThat(jwtResponse.getId()).isEqualTo(2L);
    assertThat(jwtResponse.getUsername()).isEqualTo("username2");
    assertThat(jwtResponse.getFirstName()).isEqualTo("firstName2");
    assertThat(jwtResponse.getLastName()).isEqualTo("lastName2");
    assertThat(jwtResponse.getAdmin()).isEqualTo(false);
    assertThat(jwtResponse.getType()).isEqualTo("other type");
  }
}
