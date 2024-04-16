package ru.dvfu.demo.security.exception;

public class JwtSignatureException extends JwtException {

    public JwtSignatureException(String message) {
        super(message);
    }
}
