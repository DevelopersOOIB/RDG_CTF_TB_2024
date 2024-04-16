package ru.dvfu.demo.property;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtProperties {
    @NotNull
    private String secret;
    @NotNull
    private Duration accessTokenLifetime;
    @NotNull
    private Duration refreshTokenLifetime;
    @NotNull
    private String authenticationHeader;
    @NotNull
    private String authenticationType;
}
