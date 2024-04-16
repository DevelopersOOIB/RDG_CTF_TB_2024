package ru.dvfu.demo.security.exception;

public class JwtTokenException extends JwtException {

    public JwtTokenException(String message) {
        super(message);
    }
}
