package com.openclassrooms.starterjwt.integration;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountIntTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private UserMapper userMapper;

  private Long userId = 1L;

  private User mockUser;
  private UserDto mockUserDto;

  @BeforeEach
  public void setup() {
    mockUser = new User();
    mockUser.setAdmin(false);
    mockUser.setEmail("test@test.com");
    mockUser.setFirstName("firstname");
    mockUser.setId(1L);
    mockUser.setLastName("lastname");
    mockUser.setPassword("password");

    mockUserDto = new UserDto();
    mockUserDto.setAdmin(false);
    mockUserDto.setEmail("test@test.com");
    mockUserDto.setFirstName("firstname");
    mockUserDto.setId(1L);
    mockUserDto.setLastName("lastname");
    mockUserDto.setPassword("password");
  }

  @Test
  @WithMockUser
  public void testUser_GetFindUserById_ShouldReturnUserDto() throws Exception {
    long userId = 1L;
    User user = mockUser;
    UserDto userDto = mockUserDto;

    when(userService.findById(userId)).thenReturn(user);
    when(userMapper.toDto(user)).thenReturn(userDto);

    mockMvc
      .perform(MockMvcRequestBuilders.get("/api/user/{id}", userId))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(user.getId()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.password").doesNotExist()); // assert than password not returning to end user
  }

  @Test
  @WithMockUser
  public void testUser_GetFindUserByIdWithInvalidIdFormat_ShouldReturnBadRequest()
    throws Exception {
    String invalidUserId = "invalid";

    mockMvc
      .perform(MockMvcRequestBuilders.get("/api/user/{id}", invalidUserId))
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @WithMockUser
  public void testUser_GetFindUserByIdWithInvalidId_ShouldReturnIsNotFound()
    throws Exception {
    Long invalidUserId = 2L;

    doThrow(NotFoundException.class).when(userService).findById(anyLong());

    mockMvc
      .perform(MockMvcRequestBuilders.get("/api/user/{id}", invalidUserId))
      .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @WithMockUser(username = "test@test.com")
  public void testUser_DeleteSaveUser_ShouldReturnOk() throws Exception {
    User user = mockUser;

    when(userService.findById(userId)).thenReturn(user);

    mockMvc
      .perform(MockMvcRequestBuilders.delete("/api/user/{id}", userId))
      .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockUser
  public void testUser_DeleteSaveUserWithInvalidId_ShouldReturnBadRequest()
    throws Exception {
    String invalidUserId = "invalid";

    mockMvc
      .perform(MockMvcRequestBuilders.delete("/api/user/{id}", invalidUserId))
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "wrongemail@test.com")
  public void testUser_DeleteSaveUserWithUnauthorizedUser_ShouldReturnUnauthorized()
    throws Exception {
    User user = mockUser;
    UserDto userDto = mockUserDto;

    when(userService.findById(userId)).thenReturn(user);
    when(userMapper.toDto(user)).thenReturn(userDto);

    mockMvc
      .perform(MockMvcRequestBuilders.delete("/api/user/{id}", userId))
      .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @WithMockUser
  public void testUser_DeleteSaveUserWithNotFoundUser_ShouldReturnNotFound()
    throws Exception {
    when(userService.findById(userId)).thenReturn(null);

    mockMvc
      .perform(MockMvcRequestBuilders.delete("/api/user/{id}", userId))
      .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
