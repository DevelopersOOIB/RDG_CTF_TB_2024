package org.example.hardauthorization.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.example.hardauthorization.domain.model.User;
import org.example.hardauthorization.service.MainService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @Operation(
            summary = "Спроси, может у него есть то, что тебе нужно",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(
                                    value = "cat"
                            )
                    )
            )
    )
    @PostMapping("/ask")
    public String answer(@RequestBody String input, @AuthenticationPrincipal User user) {
        return mainService.check(input, user);
    }
}
