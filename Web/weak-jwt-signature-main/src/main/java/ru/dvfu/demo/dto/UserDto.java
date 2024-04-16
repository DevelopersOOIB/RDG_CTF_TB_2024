package ru.dvfu.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dvfu.demo.enumiration.RoleEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Данные о пользователе")
public class UserDto {
    @Schema(description = "уникальный идентификатор", example = "1")
    private Long id;
    @Schema(description = "имя пользователя", example = "testing")
    private String username;
    @Schema(description = "текущая роль", example = "ADMIN")
    private RoleEnum role;
}
