package ru.dvfu.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.dvfu.demo.property.OpenApiProperties;

@Configuration
@EnableConfigurationProperties(OpenApiProperties.class)
@RequiredArgsConstructor
public class SwaggerConfig {

    private final OpenApiProperties openApiProperties;

    @Bean
    public OpenAPI customizeOpenAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement();
        Components components = new Components();

        openApiProperties.getSecuritySchemes().forEach(securityScheme -> {
            securityRequirement.addList(securityScheme.getName());
            components.addSecuritySchemes(securityScheme.getName(), securityScheme);
        });

        return new OpenAPI()
            .addSecurityItem(securityRequirement)
            .components(components);
    }
}
