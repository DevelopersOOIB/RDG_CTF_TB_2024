package ru.dvfu.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dvfu.demo.entity.RoleEntity;
import ru.dvfu.demo.enumiration.RoleEnum;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(RoleEnum name);
}
