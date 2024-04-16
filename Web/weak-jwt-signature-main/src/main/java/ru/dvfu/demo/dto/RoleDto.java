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
@Schema(description = "Данные о ролях пользователей")
public class RoleDto {
    @Schema(description = "уникальный идентификатор", example = "1")
    private Long id;
    @Schema(description = "наименование", example = "ADMIN")
    private RoleEnum name;
}
