package ru.dvfu.demo.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtBody {
    private String sub;
    private String rol;
    private String iat;
    private String exp;
}
