package ru.dvfu.demo.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dvfu.demo.entity.RoleEntity;
import ru.dvfu.demo.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<RoleEntity> getAll() {
        return roleRepository.findAll();
    }

}
