package com.openclassrooms.starterjwt.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionMapperTest {

  @Mock
  private TeacherService teacherService;

  @Mock
  private UserService userService;

  @InjectMocks
  private SessionMapperImpl sessionMapper;

  @Test
  void testToEntity() {
    // Arrange
    SessionDto sessionDto = new SessionDto();
    sessionDto.setDescription("Test Session DTO");
    sessionDto.setTeacher_id(1L);
    sessionDto.setUsers(Arrays.asList(2L, 3L));

    Teacher teacher = new Teacher();
    when(teacherService.findById(anyLong())).thenReturn(teacher);

    User user1 = new User();
    User user2 = new User();
    when(userService.findById(2L)).thenReturn(user1);
    when(userService.findById(3L)).thenReturn(user2);

    // Act
    Session result = sessionMapper.toEntity(sessionDto);

    // Assert
    assertThat(result.getDescription()).isEqualTo(sessionDto.getDescription());
    assertThat(result.getTeacher()).isEqualTo(teacher);
    assertThat(result.getUsers()).containsExactly(user1, user2);
  }

  @Test
  void testToEntity_WithNullSessionDTO_ShouldReturnNull() {
    // Arrange
    SessionDto nullSessionDto = null;

    // Act
    Session result = sessionMapper.toEntity(nullSessionDto);

    // Assert
    assertThat(result).isNull();
    verify(userService, never()).findById(anyLong());
    verify(teacherService, never()).findById(anyLong());
  }

  @Test
  void testToDto() {
    // Arrange
    Session session = new Session();
    session.setDescription("Test Session");
    session.setId(1L);
    session.setTeacher(new Teacher());
    User user1 = new User();
    user1.setId(1L);
    User user2 = new User();
    user2.setId(2L);
    session.setUsers(Arrays.asList(user1, user2));

    // Act
    SessionDto result = sessionMapper.toDto(session);

    // Assert
    assertThat(result.getDescription()).isEqualTo(session.getDescription());
    assertThat(result.getTeacher_id()).isEqualTo(session.getTeacher().getId());
    assertThat(result.getUsers()).containsExactly(1L, 2L);
  }

  @Test
  void testToDto_WithNullSession_ShouldReturnNull() {
    // Arrange
    Session nullSession = null;

    // Act
    SessionDto result = sessionMapper.toDto(nullSession);

    // Assert
    assertThat(result).isNull();
    verify(userService, never()).findById(anyLong());
    verify(teacherService, never()).findById(anyLong());
  }

  @Test
  void testSessionDtoList_ToEntity_ShouldReturnSessionList() {
    // Arrange
    List<SessionDto> sessionDtoList = new ArrayList<>();
    SessionDto sessionDto1 = new SessionDto();
    sessionDto1.setId(1L);
    SessionDto sessionDto2 = new SessionDto();
    sessionDto2.setId(2L);

    sessionDtoList.add(sessionDto1);
    sessionDtoList.add(sessionDto2);

    // Act
    List<Session> resultList = sessionMapper.toEntity(sessionDtoList);

    // Assert
    assertThat(resultList).hasSize(2);

    Session session1 = resultList.get(0);
    Session session2 = resultList.get(1);

    assertThat(session1.getId()).isEqualTo(1L);
    assertThat(session2.getId()).isEqualTo(2L);
  }

  @Test
  void testNullSessionDtoList_ToEntity_ShouldReturnNull() {
    // Arrange
    List<SessionDto> nullSessionDtoList = null;

    // Act
    List<Session> result = sessionMapper.toEntity(nullSessionDtoList);

    // Assert
    assertThat(result).isNull();
  }

  @Test
  void testSessionList_ToDto_ShouldReturnSessionDtoList() {
    // Arrange
    List<Session> sessionList = new ArrayList<>();
    Session session1 = new Session();
    session1.setId(1L);
    Session session2 = new Session();
    session2.setId(2L);

    sessionList.add(session1);
    sessionList.add(session2);

    // Act
    List<SessionDto> resultList = sessionMapper.toDto(sessionList);

    // Assert
    assertThat(resultList).hasSize(2);

    SessionDto sessionDto1 = resultList.get(0);
    SessionDto sessionDto2 = resultList.get(1);

    assertThat(sessionDto1.getId()).isEqualTo(1L);
    assertThat(sessionDto2.getId()).isEqualTo(2L);
  }

  @Test
  void testNullSessionList_ToDto_ShouldReturnNull() {
    // Arrange
    List<Session> nullSessionList = null;

    // Act
    List<SessionDto> result = sessionMapper.toDto(nullSessionList);

    // Assert
    assertThat(result).isNull();
  }
}
