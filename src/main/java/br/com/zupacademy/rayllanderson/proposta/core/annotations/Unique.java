package br.com.zupacademy.rayllanderson.proposta.core.annotations;

import br.com.zupacademy.rayllanderson.proposta.core.validators.UniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Verifica se o campo é único ao tentar persistir.
 * Deve ser usada somente no contexto do Spring.
 */
@Documented
@Constraint(validatedBy = {UniqueValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {

    String message() default "{Campo deve ser único}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?> entity();

    String field();
}
