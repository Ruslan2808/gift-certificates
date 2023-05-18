package ru.clevertec.ecl.advice;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.clevertec.ecl.exception.ApiError;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.exception.OrderNotFoundException;
import ru.clevertec.ecl.exception.TagAlreadyExistsException;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.exception.UserAlreadyExistsException;
import ru.clevertec.ecl.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({
            TagNotFoundException.class,
            GiftCertificateNotFoundException.class,
            OrderNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ApiError> handleEntityNotFound(RuntimeException e) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .type(HttpStatus.NOT_FOUND)
                .messages(List.of(e.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler({
            TagAlreadyExistsException.class,
            UserAlreadyExistsException.class
    })
    public ResponseEntity<ApiError> handleEntityAlreadyExists(RuntimeException ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.CONFLICT.value())
                .type(HttpStatus.CONFLICT)
                .messages(List.of(ex.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(HttpStatus.BAD_REQUEST)
                .messages(errorMessages)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}
