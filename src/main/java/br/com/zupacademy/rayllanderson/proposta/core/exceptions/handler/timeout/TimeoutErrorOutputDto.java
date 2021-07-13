package br.com.zupacademy.rayllanderson.proposta.core.exceptions.handler.timeout;

import java.util.ArrayList;
import java.util.List;

public class TimeoutErrorOutputDto {
    private final List<String> globalErrorMessages = new ArrayList<>();

    public void addError(String message) {
        this.globalErrorMessages.add(message);
    }

    public List<String> getGlobalErrorMessages() {
        return globalErrorMessages;
    }
}
