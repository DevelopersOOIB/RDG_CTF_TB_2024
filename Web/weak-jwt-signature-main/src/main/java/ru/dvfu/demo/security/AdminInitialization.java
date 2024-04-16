package ru.dvfu.demo.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.dvfu.demo.entity.UserEntity;
import ru.dvfu.demo.enumiration.RoleEnum;
import ru.dvfu.demo.exception.NotFoundException;
import ru.dvfu.demo.service.UserService;

@Component
@RequiredArgsConstructor
public class AdminInitialization {

    private final UserService userService;

    @Value("${spring.security.user.name}")
    private String username;
    @Value("${spring.security.user.password}")
    private String password;

    @PostConstruct
    public void init() {
        try {
            userService.updatePassword(username, password);
        } catch (NotFoundException exc) {
            UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(password)
                .build();
            userService.signUp(userEntity, RoleEnum.ADMIN);
        }

        username = null;
        password = null;
    }

}
