package ru.dvfu.demo.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import lombok.Getter;
import ru.dvfu.demo.enumiration.RoleEnum;
import ru.dvfu.demo.security.exception.JwtException;
import ru.dvfu.demo.security.exception.JwtExpirationException;
import ru.dvfu.demo.security.exception.JwtTokenException;
import ru.dvfu.demo.security.exception.JwtSignatureException;
import ru.dvfu.demo.security.model.JwtBody;
import ru.dvfu.demo.security.model.JwtHeader;

public class JwtParser {

    private final String key;
    private final Base64.Decoder decoder;
    private final ObjectMapper objectMapper;

    @Getter
    private JwtBody body;
    @Getter
    private JwtHeader header;

    public JwtParser(String key) {
        this.decoder = Base64.getUrlDecoder();
        this.objectMapper = new ObjectMapper();
        this.key = key;
    }

    public JwtParser parseJwt(String content) {

        // checking jwt structure
        String[] items = content.split("\\.");

        if (items.length < 3) {
            throw new JwtException("Invalid jwt token");
        }

        // checking header presented
        String headerStr = new String(decoder.decode(items[0]), StandardCharsets.UTF_8);
        Optional<JwtHeader> jwtHeader = parseJson(headerStr, JwtHeader.class);

        if (jwtHeader.isEmpty()) {
            throw new JwtException("Jwt header not found");
        }
        this.header = jwtHeader.get();

        // checking body claims presented
        String bodyStr = new String(decoder.decode(items[1]), StandardCharsets.UTF_8);
        Optional<JwtBody> jwtBody = parseJson(bodyStr, JwtBody.class);

        if (jwtBody.isEmpty()) {
            throw new JwtException("Jwt body not found");
        }
        this.body = jwtBody.get();

        // checking signature is valid
        String signatureStr = new String(decoder.decode(items[2]), StandardCharsets.UTF_8);
        JwtSignature jwtSignature = new JwtSignature(this.key);

        if (!jwtSignature.validateSign("%s%s".formatted(headerStr, bodyStr), signatureStr)) {
            throw  new JwtSignatureException("Jwt signature not valid");
        }

        // checking a role presented
        Optional<RoleEnum> role = parseRole(getBody().getRol());

        if (role.isEmpty()) {
            throw new JwtTokenException("Unknown role value");
        }

        // checking expiration date presented
        Optional<Date> expirationDate = parseDate(this.body.getExp());

        if (expirationDate.isEmpty()) {
            throw new JwtExpirationException("Jwt expiration date not found");
        }

        // checking expiration date valid
        Date currentDate = Date.from(Instant.now());

        if (currentDate.after(expirationDate.get())) {
            throw new JwtExpirationException("Jwt has been expired");
        }

        return this;
    }

    private <T> Optional<T> parseJson(String content, Class<T> clazz) {
        try {
           T object = objectMapper.readValue(content, clazz);
           return Optional.of(object);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<Date> parseDate(String date) {
        try {
            long dateAsLong = Long.parseLong(date);
            return Optional.of(new Date(dateAsLong));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    private Optional<RoleEnum> parseRole(String role) {
        try {
            RoleEnum value = RoleEnum.valueOf(role);
            return Optional.of(value);
        } catch (IllegalArgumentException exc) {
            return Optional.empty();
        }
    }
}
