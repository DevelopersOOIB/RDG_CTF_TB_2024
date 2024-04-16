package ru.dvfu.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dvfu.demo.dto.RoleDto;
import ru.dvfu.demo.entity.RoleEntity;
import ru.dvfu.demo.mapper.EntityToDtoMapper;
import ru.dvfu.demo.service.RoleService;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Как же я люблю БТС, вот они слева направо")
public class RoleController {

    private final RoleService roleService;
    private final EntityToDtoMapper entityToDtoMapper;

    @Operation(description = "♥ Намджун, ♥ Чонгук, ♥ Чингачгук, ♥ Гойко Митич..")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<RoleDto>> getAll() {
        List<RoleEntity> roleEntityList = roleService.getAll();
        return ResponseEntity.ok(entityToDtoMapper.roleEntityListToDtoList(roleEntityList));
    }
}
