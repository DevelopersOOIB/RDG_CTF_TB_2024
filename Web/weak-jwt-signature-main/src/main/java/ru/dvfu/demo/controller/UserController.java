package ru.dvfu.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dvfu.demo.dto.ErrorDto;
import ru.dvfu.demo.dto.SignInRequestDto;
import ru.dvfu.demo.dto.SignInResponseDto;
import ru.dvfu.demo.dto.SignUpDto;
import ru.dvfu.demo.dto.UserDto;
import ru.dvfu.demo.entity.UserEntity;
import ru.dvfu.demo.enumiration.RoleEnum;
import ru.dvfu.demo.mapper.DtoToEntityMapper;
import ru.dvfu.demo.mapper.EntityToDtoMapper;
import ru.dvfu.demo.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Мы должны оставаться мыми, а они – оними")
public class UserController {

    private final UserService userService;
    private final EntityToDtoMapper entityToDtoMapper;
    private final DtoToEntityMapper dtoToEntityMapper;

    @Operation(description = "Я вам запрещаю с**!!!")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
    })
    @PostMapping(path = "/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpDto dto) {
        userService.signUp(dtoToEntityMapper.signUpDtoToUserEntity(dto), RoleEnum.USER);
        return ResponseEntity.ok("OK");
    }

    @Operation(description = "Делай, как надо. Как не надо, не делай")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SignInResponseDto.class))),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
        @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping(path = "/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody @Valid SignInRequestDto dto) {
        SignInResponseDto response = userService.signIn(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Как говорил мой дед, «Я твой дед»")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
        @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getCurrent(Principal principal) {
        UserEntity response = userService.getCurrent(principal.getName());
        return ResponseEntity.ok(entityToDtoMapper.userEntityToDto(response));
    }
}
