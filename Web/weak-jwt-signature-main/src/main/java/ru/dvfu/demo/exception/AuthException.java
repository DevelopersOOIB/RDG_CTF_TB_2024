package ru.dvfu.demo.exception;

public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }
}
