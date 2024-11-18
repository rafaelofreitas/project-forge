package com.projectforge.template.infrastructure.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInputDTO {

  @NotBlank(message = "'name' cannot be empty")
  @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters.")
  private String name;

  @NotBlank(message = "'name' cannot be empty")
  @Email(message = "Email should be valid.")
  private String email;
}
