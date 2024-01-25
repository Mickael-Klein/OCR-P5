package com.openclassrooms.starterjwt.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TeacherTest {

  private final Validator validator = Validation
    .buildDefaultValidatorFactory()
    .getValidator();
  private Long testTeacherId = 1L;
  private LocalDateTime fixedDateTime = LocalDateTime.of(2024, 1, 1, 1, 1, 1);

  private Teacher testTeacher;

  @BeforeEach
  public void setUp() {
    testTeacher =
      Teacher
        .builder()
        .id(testTeacherId)
        .firstName("firstName")
        .lastName("lastName")
        .createdAt(fixedDateTime)
        .updatedAt(fixedDateTime)
        .build();
  }

  @Test
  public void testTeacherModel() {
    Teacher teacherUnderTest = testTeacher;

    assertThat(teacherUnderTest.getId()).isEqualTo(1L);
    assertThat(teacherUnderTest.getFirstName()).isEqualTo("firstName");
    assertThat(teacherUnderTest.getLastName()).isEqualTo("lastName");
    assertThat(teacherUnderTest.getCreatedAt()).isEqualTo(fixedDateTime);
    assertThat(teacherUnderTest.getUpdatedAt()).isEqualTo(fixedDateTime);
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  public void testTeacherModel_WithBlankFields_ShouldThrowValidationException() {
    Teacher teacherUnderTest = Teacher
      .builder()
      .id(null)
      .firstName(null)
      .lastName(null)
      .createdAt(null)
      .updatedAt(null)
      .build();

    Set<ConstraintViolation<Teacher>> violations = validator.validate(
      teacherUnderTest
    );

    assertThat(violations).isNotEmpty();
  }

  @Test
  public void testTeacherModel_ToString() {
    Teacher teacherUnderTest = testTeacher;

    String expectedToString =
      "Teacher(id=" +
      testTeacherId +
      ", lastName=lastName, firstName=firstName, createdAt=" +
      fixedDateTime +
      ", updatedAt=" +
      fixedDateTime +
      ")";

    assertEquals(expectedToString, teacherUnderTest.toString());
  }

  @Test
  public void testEqualsAndHashCode() {
    Teacher teacherUnderTest1 = Teacher.builder().id(testTeacherId).build();
    Teacher teacherUnderTest2 = Teacher.builder().id(testTeacherId).build();
    Teacher teacherUnderTest3 = Teacher.builder().id(2L).build();

    assertEquals(teacherUnderTest1, teacherUnderTest2);
    assertNotEquals(teacherUnderTest1, teacherUnderTest3);

    assertEquals(teacherUnderTest1.hashCode(), teacherUnderTest2.hashCode());
    assertNotEquals(teacherUnderTest1.hashCode(), teacherUnderTest3.hashCode());
  }

  @Test
  public void testCanEqual() {
    Teacher teacherUnderTest = Teacher.builder().id(testTeacherId).build();
    assertTrue(teacherUnderTest.canEqual(teacherUnderTest));
    assertFalse(teacherUnderTest.canEqual(new Object()));
  }
}
