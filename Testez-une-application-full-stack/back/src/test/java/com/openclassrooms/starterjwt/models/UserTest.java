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

public class UserTest {

  private Long testUserId = 1L;
  private LocalDateTime fixedDateTime = LocalDateTime.of(2024, 1, 1, 1, 1, 1);
  private final Validator validator = Validation
    .buildDefaultValidatorFactory()
    .getValidator();

  private User testUser;

  @BeforeEach
  public void setUp() {
    testUser =
      User
        .builder()
        .id(testUserId)
        .firstName("firstName")
        .lastName("lastName")
        .email("user@test.com")
        .admin(false)
        .password("password")
        .createdAt(fixedDateTime)
        .updatedAt(fixedDateTime)
        .build();
  }

  @Test
  public void testUserModel() {
    User userUnderTest = User
      .builder()
      .id(testUserId)
      .firstName("firstName")
      .lastName("lastName")
      .email("user@test.com")
      .admin(false)
      .password("password")
      .createdAt(fixedDateTime)
      .updatedAt(fixedDateTime)
      .build();

    assertThat(userUnderTest.getId()).isEqualTo(1L);
    assertThat(userUnderTest.getFirstName()).isEqualTo("firstName");
    assertThat(userUnderTest.getLastName()).isEqualTo("lastName");
    assertThat(userUnderTest.getEmail()).isEqualTo("user@test.com");
    assertThat(userUnderTest.isAdmin()).isFalse();
    assertThat(userUnderTest.getPassword()).isEqualTo("password");
    assertThat(userUnderTest.getCreatedAt()).isEqualTo(fixedDateTime);
    assertThat(userUnderTest.getUpdatedAt()).isEqualTo(fixedDateTime);
  }

  @Test
  public void testUserModel_SettersAndConstructorsMethods() {
    User userUnderTest = new User();
    userUnderTest.setAdmin(true);
    userUnderTest.setCreatedAt(fixedDateTime);
    userUnderTest.setEmail("email");
    userUnderTest.setFirstName("firstname");
    userUnderTest.setId(testUserId);
    userUnderTest.setLastName("lastname");
    userUnderTest.setPassword("password");
    userUnderTest.setUpdatedAt(fixedDateTime);

    User userUnderTest2 = new User(
      "email",
      "lastName",
      "firstName",
      "password",
      false
    );
    User userUnderTest3 = new User(
      testUserId,
      "email",
      "lastName",
      "firstName",
      "password",
      false,
      fixedDateTime,
      fixedDateTime
    );

    assertThat(userUnderTest).isInstanceOf(User.class);
    assertThat(userUnderTest2).isInstanceOf(User.class);
    assertThat(userUnderTest3).isInstanceOf(User.class);
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  public void testTeacherModel_WithBlankFields_ShouldThrowValidationException() {
    User userUnderTest = User
      .builder()
      .id(testUserId)
      .firstName(
        "firstNameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"
      )
      .lastName(
        "lastNameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"
      )
      .email("user@test.com")
      .admin(false)
      .password("password")
      .createdAt(fixedDateTime)
      .updatedAt(fixedDateTime)
      .build();

    Set<ConstraintViolation<User>> violations = validator.validate(
      userUnderTest
    );

    assertThat(violations).isNotEmpty();
  }

  @Test
  public void testUserModel_ToString() {
    User userUnderTest = testUser;

    String expectedToString =
      "User(id=" +
      testUserId +
      ", email=user@test.com, lastName=lastName, firstName=firstName, password=password, admin=false, createdAt=" +
      fixedDateTime +
      ", updatedAt=" +
      fixedDateTime +
      ")";

    assertEquals(expectedToString, userUnderTest.toString());
  }

  @Test
  public void testEqualsAndHashCode() {
    User userUnderTest1 = testUser;
    User userUnderTest2 = testUser;
    User userUnderTest3 = User
      .builder()
      .id(55L)
      .firstName("firstName2")
      .lastName("lastName1")
      .email("user2@test.com")
      .admin(true)
      .password("password2")
      .createdAt(fixedDateTime)
      .updatedAt(fixedDateTime)
      .build();

    assertEquals(userUnderTest1, userUnderTest2);
    assertNotEquals(userUnderTest1, userUnderTest3);

    assertEquals(userUnderTest1.hashCode(), userUnderTest2.hashCode());
    assertNotEquals(userUnderTest1.hashCode(), userUnderTest3.hashCode());
  }

  @Test
  public void testCanEqual() {
    User userUnderTest = testUser;
    assertTrue(userUnderTest.canEqual(userUnderTest));
    assertFalse(userUnderTest.canEqual(new Object()));
  }
}
