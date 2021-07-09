package br.com.zupacademy.rayllanderson.proposta.core.handler;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorOutputDto {
    private final List<String> globalErrorMessages = new ArrayList<>();
    private final List<FieldErrorOutputDto> fieldErrors = new ArrayList<>();

    public void addError(String message) {
        this.globalErrorMessages.add(message);
    }

    public void addFieldError(String field, String message) {
        FieldErrorOutputDto errorOutputDto = new FieldErrorOutputDto(field, message);
        this.fieldErrors.add(errorOutputDto);
    }

    public List<String> getGlobalErrorMessages() {
        return globalErrorMessages;
    }

    public List<FieldErrorOutputDto> getFieldErrors() {
        return fieldErrors;
    }
}
