package ru.dvfu.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dvfu.demo.service.FlagService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Ты можешь попробовать. Многие пробовали. И где они теперь?")
public class FlagController {

    private final FlagService flagService;

    @Operation(description = "Я приберег для тебе еще одну цитату. Но из проведённых 64-х боёв у меня 64 победы. Все бои были с тенью")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/flag")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getFlag() {
        return ResponseEntity.ok(flagService.getFlag());
    }
}
