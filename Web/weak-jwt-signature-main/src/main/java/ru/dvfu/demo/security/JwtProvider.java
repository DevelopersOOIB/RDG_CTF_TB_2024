package ru.dvfu.demo.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.dvfu.demo.entity.UserEntity;
import ru.dvfu.demo.enumiration.RoleEnum;
import ru.dvfu.demo.enumiration.TokenType;
import ru.dvfu.demo.property.JwtProperties;
import ru.dvfu.demo.repository.UserRepository;
import ru.dvfu.demo.security.exception.JwtException;
import ru.dvfu.demo.security.exception.JwtExpirationException;
import ru.dvfu.demo.security.exception.JwtSignatureException;
import ru.dvfu.demo.security.jwt.Jwts;
import ru.dvfu.demo.security.model.CustomUserDetails;
import ru.dvfu.demo.security.model.JwtBody;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtProvider {

    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;

    public Authentication getAuthentication(@NonNull String token) {
        final JwtBody jwtBody = Jwts.parserBuilder()
            .setKey(jwtProperties.getSecret())
            .build()
            .parseJwt(token)
            .getBody();

        UserEntity user = userRepository
            .findByUsername(jwtBody.getSub())
            .orElse(null);
        if (user == null) {
            return null;
        }

        UserDetails userDetails = CustomUserDetails
            .builder()
            .id(user.getId())
            .username(jwtBody.getSub())
            .role(RoleEnum.valueOf(jwtBody.getRol()))
            .build();

        return new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
    }

    public String generateJwtToken(@NonNull UserEntity user, @NonNull TokenType tokenType) {
        Duration duration = switch (tokenType) {
            case ACCESS -> jwtProperties.getAccessTokenLifetime();
            case REFRESH -> jwtProperties.getRefreshTokenLifetime();
        };

        Date expireDate = Date.from(Instant.now().plus(duration));
        Date issuedAt = new Date();

        return Jwts.builder()
            .setSubject(user.getUsername())
            .setRole(user.getRole().getName().name())
            .setExpiration(expireDate)
            .setIssuedAt(issuedAt)
            .setKey(jwtProperties.getSecret())
            .compact();
    }

    public Pair<Boolean, String> validateJwtToken(@NonNull String token) {
        String message;
        try {
            Jwts.parserBuilder()
                .setKey(jwtProperties.getSecret())
                .build()
                .parseJwt(token);
            return Pair.of(true, "OK");
        } catch (JwtSignatureException signExc) {
            message = "Invalid signature";
        } catch (JwtExpirationException expExc) {
            message = "Token expired";
        } catch (JwtException e) {
            message = "Invalid token";
        }
        return Pair.of(false, message);
    }
}
