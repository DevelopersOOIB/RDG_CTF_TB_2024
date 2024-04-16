package ru.dvfu.demo.security.exception;

public class JwtException extends RuntimeException {

    public JwtException(String message) {
        super(message);
    }
}
