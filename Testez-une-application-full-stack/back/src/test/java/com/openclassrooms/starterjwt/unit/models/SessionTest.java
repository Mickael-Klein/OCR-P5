package com.openclassrooms.starterjwt.unit.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SessionTest {

  private final Validator validator = Validation
    .buildDefaultValidatorFactory()
    .getValidator();

  private Long testSessionId = 1L;
  private Long testUserId = 1L;
  private Long testTeacherId = 1L;
  private LocalDateTime fixedDateTime = LocalDateTime.of(2024, 1, 1, 1, 1, 1);
  private Date fixedDate = new Date(1234567890123L);

  private Teacher testTeacher;
  private User testUser;
  private List<User> testUserList;
  private Session testSession;

  @BeforeEach
  public void setUp() {
    testTeacher = new Teacher();
    testTeacher.setId(testTeacherId);
    testTeacher.setFirstName("firstName");
    testTeacher.setLastName("lastName");
    testTeacher.setCreatedAt(fixedDateTime);
    testTeacher.setUpdatedAt(fixedDateTime);

    testUser = new User();
    testUser.setId(testUserId);
    testUser.setFirstName("firstName");
    testUser.setLastName("lastName");
    testUser.setEmail("user@test.com");
    testUser.setAdmin(false);
    testUser.setPassword("password");
    testUser.setCreatedAt(fixedDateTime);
    testUser.setUpdatedAt(fixedDateTime);

    testUserList = new ArrayList<User>();
    testUserList.add(testUser);

    testSession =
      Session
        .builder()
        .id(testSessionId)
        .name("Session 1")
        .date(fixedDate)
        .description("Session 1 description")
        .teacher(testTeacher)
        .users(testUserList)
        .createdAt(fixedDateTime)
        .updatedAt(fixedDateTime)
        .build();
  }

  @Test
  public void testSessionModel() {
    Session sessionUnderTest = testSession;

    assertThat(sessionUnderTest.getId()).isEqualTo(1L);
    assertThat(sessionUnderTest.getName()).isEqualTo("Session 1");
    assertThat(sessionUnderTest.getDate()).isEqualTo(fixedDate);
    assertThat(sessionUnderTest.getDescription())
      .isEqualTo("Session 1 description");
    assertThat(sessionUnderTest.getTeacher()).isEqualTo(testTeacher);
    assertThat(sessionUnderTest.getUsers()).isEqualTo(testUserList);
    assertThat(sessionUnderTest.getCreatedAt()).isEqualTo(fixedDateTime);
    assertThat(sessionUnderTest.getUpdatedAt()).isEqualTo(fixedDateTime);
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  public void testSessionModel_WithBlankFields_ShouldThrowValidationException() {
    Session sessionUnderTest = Session
      .builder()
      .id(null)
      .name(null)
      .date(null)
      .description(null)
      .teacher(null)
      .users(null)
      .createdAt(null)
      .updatedAt(null)
      .build();

    Set<ConstraintViolation<Session>> violations = validator.validate(
      sessionUnderTest
    );

    assertThat(violations).isNotEmpty();
  }

  @Test
  public void testSessionModel_ToString() {
    Session sessionUnderTest = testSession;

    String expectedToString =
      "Session(id=" +
      testSessionId +
      ", name=Session 1, date=" +
      fixedDate +
      ", description=Session 1 description, teacher=" +
      testTeacher +
      ", users=" +
      testUserList +
      ", createdAt=" +
      fixedDateTime +
      ", updatedAt=" +
      fixedDateTime +
      ")";

    assertEquals(expectedToString, sessionUnderTest.toString());
  }

  @Test
  public void testEqualsAndHashCode() {
    Session sessionUnderTest1 = Session.builder().id(testSessionId).build();
    Session sessionUnderTest2 = Session.builder().id(testSessionId).build();
    Session sessionUnderTest3 = Session.builder().id(2L).build();

    assertEquals(sessionUnderTest1, sessionUnderTest2);
    assertNotEquals(sessionUnderTest1, sessionUnderTest3);

    assertEquals(sessionUnderTest1.hashCode(), sessionUnderTest2.hashCode());
    assertNotEquals(sessionUnderTest1.hashCode(), sessionUnderTest3.hashCode());
  }
}
