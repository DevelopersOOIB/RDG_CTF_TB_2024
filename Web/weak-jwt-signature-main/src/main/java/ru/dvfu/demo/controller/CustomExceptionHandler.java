package ru.dvfu.demo.controller;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.dvfu.demo.dto.ErrorDto;
import ru.dvfu.demo.exception.AlreadyExistsException;
import ru.dvfu.demo.exception.AuthException;
import ru.dvfu.demo.exception.NotFoundException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({ NotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({ AuthException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleAuthException(AuthException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }

    @ExceptionHandler({ AlreadyExistsException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleAlreadyExistsException(AlreadyExistsException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException (HttpMessageNotReadableException exception){
        return ResponseEntity.badRequest().body(new ErrorDto("Message body", "Required request body is missing"));
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ErrorDto>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        List<ErrorDto> errors = exception.getFieldErrors()
            .stream()
            .map(error -> new ErrorDto(error.getField(), error.getDefaultMessage()))
            .toList();

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ErrorDto>> handleConstraintViolationException(ConstraintViolationException exception) {

        List<ErrorDto> errors = exception.getConstraintViolations()
            .stream()
            .map(error -> new ErrorDto(
                "%s %s".formatted(error.getRootBeanClass().getName(), error.getPropertyPath()),
                error.getMessage()
            ))
            .toList();

        return ResponseEntity.badRequest().body(errors);
    }
}
