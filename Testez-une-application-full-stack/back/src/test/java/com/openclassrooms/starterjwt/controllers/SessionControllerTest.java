package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

  @Mock
  private SessionService sessionService;

  @Mock
  private SessionMapper sessionMapper;

  @InjectMocks
  private SessionController sessionController;

  @Test
  void findById_SessionExists_ShouldReturnSessionDto() {
    // Arrange
    Long sessionId = 1L;
    Session session = new Session();
    when(sessionService.getById(sessionId)).thenReturn(session);

    SessionDto sessionDto = new SessionDto();
    when(sessionMapper.toDto(session)).thenReturn(sessionDto);

    // Act
    ResponseEntity<?> responseEntity = sessionController.findById(
      sessionId.toString()
    );

    // Assert
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isEqualTo(sessionDto);
  }

  @Test
  void findById_SessionDoesNotExist_ShouldReturnNotFound() {
    // Arrange
    Long sessionId = 1L;
    when(sessionService.getById(sessionId)).thenReturn(null);

    // Act
    ResponseEntity<?> responseEntity = sessionController.findById(
      sessionId.toString()
    );

    // Assert
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(responseEntity.getBody()).isNull();
  }

  @Test
  void findAll_ShouldReturnSessionDtoList() {
    // Arrange
    List<Session> sessions = Collections.singletonList(new Session());
    when(sessionService.findAll()).thenReturn(sessions);

    List<SessionDto> sessionDtos = Collections.singletonList(new SessionDto());
    when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

    // Act
    ResponseEntity<?> responseEntity = sessionController.findAll();

    // Assert
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isEqualTo(sessionDtos);
  }

  @Test
  void testCreate_ShouldReturnSessionDto() {
    // Arrange
    SessionDto sessionDto = new SessionDto();
    Session session = new Session();
    when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
    when(sessionService.create(session)).thenReturn(session);
    when(sessionMapper.toDto(session)).thenReturn(sessionDto);

    // Act
    ResponseEntity<?> responseEntity = sessionController.create(sessionDto);

    // Assert
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isEqualTo(sessionDto);
  }

  @Test
  void testUpdate_SessionExists_ShouldReturnUpdatedSessionDto() {
    // Arrange
    String sessionId = "1";
    SessionDto sessionDto = new SessionDto();
    Session session = new Session();
    when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
    when(sessionService.update(anyLong(), eq(session))).thenReturn(session);
    when(sessionMapper.toDto(session)).thenReturn(sessionDto);

    // Act
    ResponseEntity<?> responseEntity = sessionController.update(
      sessionId,
      sessionDto
    );

    // Assert
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isEqualTo(sessionDto);
  }

  @Test
  void testUpdate_InvalidIdFormat_ShouldReturnBadRequest() {
    // Arrange
    String invalidSessionId = "invalidId";
    SessionDto sessionDto = new SessionDto();

    // Act
    ResponseEntity<?> responseEntity = sessionController.update(
      invalidSessionId,
      sessionDto
    );

    // Assert
    assertThat(responseEntity.getStatusCode())
      .isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void testDeleteSave_SessionExists_ShouldReturnOk() {
    // Arrange
    String sessionId = "1";
    when(sessionService.getById(anyLong())).thenReturn(new Session());

    // Act
    ResponseEntity<?> responseEntity = sessionController.save(sessionId);

    // Assert
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testDeleteSave_SessionDoesNotExist_ShouldReturnNotFound() {
    // Arrange
    String sessionId = "1";
    when(sessionService.getById(anyLong())).thenReturn(null);

    // Act
    ResponseEntity<?> responseEntity = sessionController.save(sessionId);

    // Assert
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testDeleteSave_InvalidIdFormat_ShouldReturnBadRequest() {
    // Arrange
    String invalidSessionId = "invalidId";

    // Act
    ResponseEntity<?> responseEntity = sessionController.save(invalidSessionId);

    // Assert
    assertThat(responseEntity.getStatusCode())
      .isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void testParticipate_ShouldReturnOk() {
    // Arrange
    String sessionId = "1";
    String userId = "2";

    // Act
    ResponseEntity<?> responseEntity = sessionController.participate(
      sessionId,
      userId
    );

    // Assert
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    verify(sessionService, times(1)).participate(anyLong(), anyLong());
  }

  @Test
  void testParticipate_InvalidIdFormat_ShouldReturnBadRequest() {
    // Arrange
    String invalidSessionId = "invalidId";
    String userId = "2";

    // Act
    ResponseEntity<?> responseEntity = sessionController.participate(
      invalidSessionId,
      userId
    );

    // Assert
    assertThat(responseEntity.getStatusCode())
      .isEqualTo(HttpStatus.BAD_REQUEST);
    verify(sessionService, never()).participate(anyLong(), anyLong());
  }

  @Test
  void testNoLongerParticipate_ShouldReturnOk() {
    // Arrange
    String sessionId = "1";
    String userId = "2";

    // Act
    ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(
      sessionId,
      userId
    );

    // Assert
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    verify(sessionService, times(1)).noLongerParticipate(anyLong(), anyLong());
  }

  @Test
  void testNoLongerParticipate_InvalidIdFormat_ShouldReturnBadRequest() {
    // Arrange
    String invalidSessionId = "invalidId";
    String userId = "2";

    // Act
    ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(
      invalidSessionId,
      userId
    );

    // Assert
    assertThat(responseEntity.getStatusCode())
      .isEqualTo(HttpStatus.BAD_REQUEST);
    verify(sessionService, never()).noLongerParticipate(anyLong(), anyLong());
  }
}
