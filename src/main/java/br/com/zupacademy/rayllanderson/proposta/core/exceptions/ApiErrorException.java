package br.com.zupacademy.rayllanderson.proposta.core.exceptions;

import org.springframework.http.HttpStatus;

public class ApiErrorException extends RuntimeException{
    private final HttpStatus status;
    private final String message;

    public ApiErrorException(HttpStatus status, String cause) {
        this.status = status;
        this.message = cause;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
