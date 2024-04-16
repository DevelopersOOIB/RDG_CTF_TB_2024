package ru.dvfu.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос аутентификации пользователя")
public class SignInRequestDto {
    @Schema(description = "имя пользователя", example = "testing")
    @NotBlank(message = "username mustn't be empty")
    private String username;
    @Schema(description = "пароль", example = "testing")
    @NotBlank(message = "password mustn't be empty")
    private String password;
}
