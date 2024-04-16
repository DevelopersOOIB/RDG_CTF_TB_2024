package ru.dvfu.demo.property;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ctf")
public class CtfProperties {
    @NotNull
    private String flag;
}
