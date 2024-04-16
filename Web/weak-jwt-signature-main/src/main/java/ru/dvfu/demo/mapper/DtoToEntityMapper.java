package ru.dvfu.demo.mapper;

import org.springframework.stereotype.Component;
import ru.dvfu.demo.dto.SignUpDto;
import ru.dvfu.demo.entity.UserEntity;

@Component
public class DtoToEntityMapper {

    public UserEntity signUpDtoToUserEntity(SignUpDto source) {
        if (source == null) {
            return null;
        }

        UserEntity target = new UserEntity();
        target.setUsername(source.getUsername());
        target.setPassword(source.getPassword());

        return target;
    }
}
