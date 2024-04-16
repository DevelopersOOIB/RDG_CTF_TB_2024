package ru.dvfu.demo.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import ru.dvfu.demo.security.exception.JwtMakingException;
import ru.dvfu.demo.security.model.JwtBody;
import ru.dvfu.demo.security.model.JwtHeader;

public class JwtXorBuilder {

    private final Base64.Encoder encoder;
    private final ObjectMapper objectMapper;

    private String subject;
    private String role;
    private Date issuedAt;
    private Date expiration;
    private String key;

    public JwtXorBuilder() {
        this.encoder = Base64.getUrlEncoder().withoutPadding();
        this.objectMapper = new ObjectMapper();
    }

    public JwtXorBuilder setRole(String role) {
        this.role = role;
        return this;
    }

    public JwtXorBuilder setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
        return this;
    }

    public JwtXorBuilder setExpiration(Date expiration) {
        this.expiration = expiration;
        return this;
    }

    public JwtXorBuilder setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public JwtXorBuilder setKey(String key) {
        this.key = key;
        return this;
    }

    public String compact() {

        JwtHeader header = JwtHeader
            .builder()
            .alg("312-ORX")
            .typ("JWT")
            .build();
        JwtBody body = JwtBody
            .builder()
            .sub(subject)
            .rol(role)
            .iat(String.valueOf(issuedAt.getTime()))
            .exp(String.valueOf(expiration.getTime()))
            .build();

        Optional<String> headerOpt = writeObjectToJson(header);
        if (headerOpt.isEmpty()) {
            throw new JwtMakingException("Error convert jwt header to valid JSON str");
        }

        Optional<String> bodyOpt = writeObjectToJson(body);
        if (bodyOpt.isEmpty()) {
            throw new JwtMakingException("Error convert jwt body to valid JSON str");
        }

        JwtSignature jwtSignature = new JwtSignature(key);
        byte[] signatureBytes = jwtSignature.makeSign("%s%s".formatted(headerOpt.get(), bodyOpt.get()));

        return "%s.%s.%s".formatted(
            encoder.encodeToString(headerOpt.get().getBytes(StandardCharsets.UTF_8)),
            encoder.encodeToString(bodyOpt.get().getBytes(StandardCharsets.UTF_8)),
            encoder.encodeToString(signatureBytes)
        );
    }

    private Optional<String> writeObjectToJson(Object object) {
        try {
            return Optional.of(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

}
