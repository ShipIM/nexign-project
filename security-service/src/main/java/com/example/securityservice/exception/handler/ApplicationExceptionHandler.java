package com.example.securityservice.exception.handler;

import com.example.securityservice.dto.error.UiSuccessContainer;
import com.example.securityservice.exception.EntityNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public UiSuccessContainer handleNotFoundException(EntityNotFoundException exception) {
        return new UiSuccessContainer(false, exception.getMessage());
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public UiSuccessContainer handleBadCredentialsException(BadCredentialsException exception) {
        return new UiSuccessContainer(false, exception.getMessage());
    }

}
