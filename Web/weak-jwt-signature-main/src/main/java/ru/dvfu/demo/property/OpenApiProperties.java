package ru.dvfu.demo.property;

import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "open-api")
public class OpenApiProperties {
    @NotNull
    private List<SecurityScheme> securitySchemes;
}
