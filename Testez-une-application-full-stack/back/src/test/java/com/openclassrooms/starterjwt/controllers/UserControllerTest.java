package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock
  private UserService userService;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private UserController userController;

  @Test
  void testFindById_UserExists_ShouldReturnUserDto() {
    // Arrange
    String userId = "1";
    User user = new User();
    when(userService.findById(anyLong())).thenReturn(user);
    when(userMapper.toDto(user)).thenReturn(new UserDto());

    // Act
    ResponseEntity<?> responseEntity = userController.findById(userId);

    // Assert
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isInstanceOf(UserDto.class);
  }

  @Test
  void testFindById_UserDoesNotExist_ShouldReturnNotFound() {
    // Arrange
    String userId = "1";
    when(userService.findById(anyLong())).thenReturn(null);

    // Act
    ResponseEntity<?> responseEntity = userController.findById(userId);

    // Assert
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testFindById_InvalidIdFormat_ShouldReturnBadRequest() {
    // Arrange
    String invalidUserId = "invalidId";

    // Act
    ResponseEntity<?> responseEntity = userController.findById(invalidUserId);

    // Assert
    assertThat(responseEntity.getStatusCode())
      .isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void testSave_UserExistsAndMatchesAuthenticatedUser_ShouldReturnOk() {
    // Arrange
    String userId = "1";
    User user = new User();
    user.setEmail("test@test.com");

    when(userService.findById(anyLong())).thenReturn(user);
    doNothing().when(userService).delete(anyLong());

    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn("test@test.com");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Act
    ResponseEntity<?> responseEntity = userController.save(userId);

    // Assert
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    verify(userService, times(1)).delete(anyLong());
  }

  @Test
  void testSave_UserExistsButDoesNotMatchAuthenticatedUser_ShouldReturnUnauthorized() {
    // Arrange
    String userId = "1";
    User user = new User();
    user.setEmail("wrongemail@test.com");

    when(userService.findById(anyLong())).thenReturn(user);

    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn("test@test.com");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Act
    ResponseEntity<?> responseEntity = userController.save(userId);

    // Assert
    assertThat(responseEntity.getStatusCode())
      .isEqualTo(HttpStatus.UNAUTHORIZED);
    verify(userService, never()).delete(anyLong());
  }

  @Test
  void testSave_UserDoesNotExist_ShouldReturnNotFound() {
    // Arrange
    String userId = "1";
    when(userService.findById(anyLong())).thenReturn(null);

    // Act
    ResponseEntity<?> responseEntity = userController.save(userId);

    // Assert
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    verify(userService, never()).delete(anyLong());
  }

  @Test
  void testSave_InvalidIdFormat_ShouldReturnBadRequest() {
    // Arrange
    String invalidUserId = "invalidId";

    // Act
    ResponseEntity<?> responseEntity = userController.save(invalidUserId);

    // Assert
    assertThat(responseEntity.getStatusCode())
      .isEqualTo(HttpStatus.BAD_REQUEST);
    verify(userService, never()).delete(anyLong());
  }
}
