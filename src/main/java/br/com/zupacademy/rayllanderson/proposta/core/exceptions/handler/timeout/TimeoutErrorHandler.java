package br.com.zupacademy.rayllanderson.proposta.core.exceptions.handler.timeout;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TimeoutErrorHandler {

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(feign.RetryableException.class)
    public TimeoutErrorOutputDto handleFeignTimeoutException(feign.RetryableException exception){
        TimeoutErrorOutputDto errors = new TimeoutErrorOutputDto();
        errors.addError("Não foi possível criar uma proposta nesse momento. Tente novamente.");
        return errors;
    }
}
