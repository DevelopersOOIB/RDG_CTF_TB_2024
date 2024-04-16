package ru.dvfu.demo.mapper;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.dvfu.demo.dto.RoleDto;
import ru.dvfu.demo.dto.UserDto;
import ru.dvfu.demo.entity.RoleEntity;
import ru.dvfu.demo.entity.UserEntity;

@Component
public class EntityToDtoMapper {

    public UserDto userEntityToDto(UserEntity source) {
        if (source == null) {
            return null;
        }

        UserDto target = new UserDto();
        target.setId(source.getId());
        target.setUsername(source.getUsername());
        target.setRole(source.getRole().getName());

        return target;
    }

    public RoleDto roleEntityToDto(RoleEntity source) {
        if (source == null) {
            return null;
        }

        RoleDto target = new RoleDto();
        target.setId(source.getId());
        target.setName(source.getName());

        return target;
    }

    public List<RoleDto> roleEntityListToDtoList(List<RoleEntity> source) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        return source
            .stream()
            .map(this::roleEntityToDto)
            .toList();
    }
}
