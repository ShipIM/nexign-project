package com.example.securityservice.exception.handler;

import com.example.securityservice.dto.error.UiSuccessContainer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class UnexpectedExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public UiSuccessContainer handleInternalException(Exception exception) {
        exception.printStackTrace();
        return new UiSuccessContainer(false, "An unexpected exception has occurred");
    }

}
