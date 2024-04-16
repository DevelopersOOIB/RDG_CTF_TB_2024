package ru.dvfu.demo.security.exception;

public class JwtExpirationException extends JwtException {

    public JwtExpirationException(String message) {
        super(message);
    }
}
