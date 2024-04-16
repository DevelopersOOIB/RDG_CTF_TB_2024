package ru.dvfu.demo.exception;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException() {
        super("Record already exists");
    }
}
