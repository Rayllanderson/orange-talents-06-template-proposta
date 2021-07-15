package br.com.zupacademy.rayllanderson.proposta.core.annotations;

import br.com.zupacademy.rayllanderson.proposta.core.validators.Base64Validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {Base64Validator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Base64 {

    String message() default "{Campo deve ter um formato de base 64 válido}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
