package com.openclassrooms.starterjwt.security.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class UserDetailsImplTest {

  @Test
  void testIsAccountNonExpired() {
    // Arrange
    UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

    // Act & Assert
    assertThat(userDetails.isAccountNonExpired()).isTrue();
  }

  @Test
  void testIsAccountNonLocked() {
    // Arrange
    UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

    // Act & Assert
    assertThat(userDetails.isAccountNonLocked()).isTrue();
  }

  @Test
  void testIsCredentialsNonExpired() {
    // Arrange
    UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

    // Act & Assert
    assertThat(userDetails.isCredentialsNonExpired()).isTrue();
  }

  @Test
  void testIsEnabled() {
    // Arrange
    UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

    // Act & Assert
    assertThat(userDetails.isEnabled()).isTrue();
  }

  @Test
  void testEquals() {
    // Arrange
    UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
    UserDetailsImpl user2 = UserDetailsImpl.builder().id(1L).build();
    UserDetailsImpl user3 = UserDetailsImpl.builder().id(2L).build();

    // Act & Assert
    assertThat(user1.equals(user2)).isTrue();
    assertThat(user1.equals(user3)).isFalse();
  }
}
