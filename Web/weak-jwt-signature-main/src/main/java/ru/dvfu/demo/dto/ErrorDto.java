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
@Schema(description = "Ответ ошибки валидации")
public class ErrorDto {
    @Schema(description = "Проверяемое на ограничение поле/аргумент", example = "username")
    private String target;
    @Schema(description = "Описание ошибки", example = "username mustn't be empty")
    private String error;
}
