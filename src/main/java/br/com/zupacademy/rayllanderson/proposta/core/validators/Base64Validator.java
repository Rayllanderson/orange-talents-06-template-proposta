package br.com.zupacademy.rayllanderson.proposta.core.validators;

import br.com.zupacademy.rayllanderson.proposta.core.annotations.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Base64Validator implements ConstraintValidator<Base64, Object> {

    private final Logger logger = LoggerFactory.getLogger(Base64Validator.class);

    @Override
    public boolean isValid(Object base64, ConstraintValidatorContext constraintValidatorContext) {
        if(base64 == null) return false;
        java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
        String possibleValidBase64 = (String) base64;
        try {
            decoder.decode(possibleValidBase64);
            return true;
        } catch(IllegalArgumentException e) {
            logger.warn("Ocorreu uma entrada de base64 em formato inválido.");
            return false;
        }
    }
}
