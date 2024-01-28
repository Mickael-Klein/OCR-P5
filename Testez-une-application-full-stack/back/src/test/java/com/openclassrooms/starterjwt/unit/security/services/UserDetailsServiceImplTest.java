package com.openclassrooms.starterjwt.unit.security.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserDetailsServiceImpl userDetailsService;

  @Test
  void testLoadUserByUsername() {
    // Arrange
    String username = "test@test.com";
    User user = new User();
    user.setId(1L);
    user.setEmail(username);
    user.setFirstName("firstname");
    user.setLastName("lastname");
    user.setPassword("password");

    when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

    // Act
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    // Assert
    assertThat(userDetails).isInstanceOf(UserDetailsImpl.class);
    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
    assertThat(userDetailsImpl.getId()).isEqualTo(user.getId());
    assertThat(userDetailsImpl.getUsername()).isEqualTo(user.getEmail());
    assertThat(userDetailsImpl.getFirstName()).isEqualTo(user.getFirstName());
    assertThat(userDetailsImpl.getLastName()).isEqualTo(user.getLastName());
    assertThat(userDetailsImpl.getPassword()).isEqualTo(user.getPassword());
  }

  @Test
  void testLoadUserByUsername_UserNotFound() {
    // Arrange
    String username = "nonexistent@test.com";

    when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> userDetailsService.loadUserByUsername(username))
      .isInstanceOf(UsernameNotFoundException.class)
      .hasMessage("User Not Found with email: " + username);
  }
}
