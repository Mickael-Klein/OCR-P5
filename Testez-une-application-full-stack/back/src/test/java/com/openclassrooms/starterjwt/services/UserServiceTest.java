package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  private Long testUserId = 1L;
  private LocalDateTime fixedDateTime = LocalDateTime.of(2024, 1, 1, 1, 1, 1);

  private User testUser;

  @BeforeEach
  public void setUp() {
    testUser = new User();
    testUser.setId(1L);
    testUser.setFirstName("firstName");
    testUser.setLastName("lastName");
    testUser.setEmail("user@test.com");
    testUser.setAdmin(false);
    testUser.setPassword("password");
    testUser.setCreatedAt(fixedDateTime);
    testUser.setUpdatedAt(fixedDateTime);
  }

  @Test
  public void testDeleteUser() {
    // Arrange
    Long userIdUnderTest = testUserId;

    // Act
    userService.delete(userIdUnderTest);

    // Assert
    verify(userRepository).deleteById(userIdUnderTest);
  }

  @Test
  public void testFindUserById_ExistingId() {
    // Arrange
    Long userIdUnderTest = testUserId;
    User userUnderTest = testUser;
    when(userRepository.findById(userIdUnderTest))
      .thenReturn(Optional.of(testUser));

    // Act
    User retrievedUser = userService.findById(userIdUnderTest);

    // Assert
    assertThat(retrievedUser).isEqualTo(userUnderTest);
    verify(userRepository).findById(userIdUnderTest);
  }

  @Test
  public void testFindUserById_NonExistingId() {
    // Arrange
    Long nonExistingUserId = 99L;
    when(userRepository.findById(nonExistingUserId))
      .thenReturn(Optional.empty());

    // Act
    User retrievedUser = userService.findById(nonExistingUserId);

    // Assert
    assertThat(retrievedUser).isNull();
    verify(userRepository).findById(nonExistingUserId);
  }
}
