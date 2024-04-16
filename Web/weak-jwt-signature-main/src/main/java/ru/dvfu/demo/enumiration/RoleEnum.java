package ru.dvfu.demo.enumiration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum RoleEnum implements GrantedAuthority {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    GUEST("ROLE_GUEST");

    private final String value;

    @Override
    public String getAuthority() {
        return this.value;
    }
}
