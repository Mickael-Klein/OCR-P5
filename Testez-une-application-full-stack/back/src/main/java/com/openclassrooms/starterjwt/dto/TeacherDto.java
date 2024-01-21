package com.openclassrooms.starterjwt.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto {

  private Long id;

  @NotBlank
  @Size(max = 20)
  private String lastName;

  @NotBlank
  @Size(max = 20)
  private String firstName;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
