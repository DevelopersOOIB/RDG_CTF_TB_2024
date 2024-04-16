package ru.dvfu.demo.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dvfu.demo.dto.SignInRequestDto;
import ru.dvfu.demo.dto.SignInResponseDto;
import ru.dvfu.demo.entity.RoleEntity;
import ru.dvfu.demo.entity.UserEntity;
import ru.dvfu.demo.enumiration.RoleEnum;
import ru.dvfu.demo.enumiration.TokenType;
import ru.dvfu.demo.exception.AlreadyExistsException;
import ru.dvfu.demo.exception.AuthException;
import ru.dvfu.demo.exception.NotFoundException;
import ru.dvfu.demo.repository.RoleRepository;
import ru.dvfu.demo.repository.UserRepository;
import ru.dvfu.demo.security.JwtProvider;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserEntity getCurrent(String username) {
        return userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    }

    public void signUp(UserEntity userEntity, RoleEnum roleEnum) {
        Optional<UserEntity> user = userRepository.findByUsername(userEntity.getUsername());
        if (user.isPresent()) {
            throw new AlreadyExistsException();
        }

        Optional<RoleEntity> roleEntity = roleRepository.findByName(roleEnum);
        if (roleEntity.isEmpty()) {
            throw new IllegalArgumentException("Not found USER role in DB");
        }

        userEntity.setRole(roleEntity.get());
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        userRepository.save(userEntity);
    }

    public SignInResponseDto signIn(SignInRequestDto dto) {
        UserEntity entity = userRepository.findByUsername(dto.getUsername()).orElse(null);
        if (entity == null) {
            throw new AuthException("Username or password invalid");
        }

        boolean passwordMatches = passwordEncoder.matches(dto.getPassword(), entity.getPassword());
        if (!passwordMatches) {
            throw new AuthException("Username or password invalid");
        }

        return new SignInResponseDto(
            jwtProvider.generateJwtToken(entity, TokenType.ACCESS),
            jwtProvider.generateJwtToken(entity, TokenType.REFRESH)
        );
    }

    public void updatePassword(String username, String password) {
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new NotFoundException();
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

}
