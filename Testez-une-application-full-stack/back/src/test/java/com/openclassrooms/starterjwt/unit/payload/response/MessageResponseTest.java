package com.openclassrooms.starterjwt.unit.payload.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import org.junit.jupiter.api.Test;

class MessageResponseTest {

  @Test
  void testMessageResponseConstructorAndGetters() {
    // Arrange
    String messageContent = "Test message";

    // Act
    MessageResponse messageResponse = new MessageResponse(messageContent);

    // Assert
    assertThat(messageResponse.getMessage()).isEqualTo(messageContent);
  }

  @Test
  void testMessageResponseSetter() {
    // Arrange
    MessageResponse messageResponse = new MessageResponse("Initial message");

    // Act
    messageResponse.setMessage("Updated message");

    // Assert
    assertThat(messageResponse.getMessage()).isEqualTo("Updated message");
  }
}
