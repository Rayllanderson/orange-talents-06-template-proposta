package br.com.zupacademy.rayllanderson.proposta.core.validators;

import br.com.zupacademy.rayllanderson.proposta.core.annotations.Unique;
import br.com.zupacademy.rayllanderson.proposta.core.exceptions.ApiErrorException;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    private Class<?> clazz;
    private String field;

    @PersistenceContext
    private final EntityManager em;

    public UniqueValidator(EntityManager em) {
        this.em = em;
    }

    @Override
    public void initialize(Unique constraintAnnotation) {
        clazz = constraintAnnotation.entity();
        field = constraintAnnotation.field();
    }

    @Override
    public boolean isValid(Object fieldValue, ConstraintValidatorContext constraintValidatorContext) {
        if (fieldValue == null || fieldValue.equals("")) return false;
        String jpql = "SELECT 1 FROM " + clazz.getName() + " WHERE " + field + " = :field";
        List<?> result = em.createQuery(jpql).setParameter("field", fieldValue).getResultList();
        if (result.size() > 0) throw new ApiErrorException(HttpStatus.UNPROCESSABLE_ENTITY, field + " já está cadastrado no sistema.");
        return true;
    }
}