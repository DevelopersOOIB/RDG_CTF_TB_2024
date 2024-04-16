package ru.dvfu.demo.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("Nothing found");
    }
}
