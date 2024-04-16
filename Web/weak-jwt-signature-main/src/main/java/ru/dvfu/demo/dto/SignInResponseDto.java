package ru.dvfu.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ запроса аутентификации")
public class SignInResponseDto {
    @Schema(description = "Токен доступа", example = "eyJhbGciOiJPUlgiLCJ0eXAiOiJKV1QifQ.eyJzdWIiOiJhZG1pbiIsInJvbCI6IkFETUlO...")
    private String accessToken;
    @Schema(description = "Токен обновления доступа", example = "eyJhbGciOiJPUlgiLCJ0eXAiOiJKV1QifQ.eyJzdWIiOiJhZG1pbiIsInJvbCI6IkFETUlO...")
    private String refreshToken;
}
