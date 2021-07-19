package br.com.zupacademy.rayllanderson.proposta.core.validators;

import br.com.zupacademy.rayllanderson.proposta.core.annotations.UniqueDocument;
import br.com.zupacademy.rayllanderson.proposta.core.exceptions.ApiErrorException;
import br.com.zupacademy.rayllanderson.proposta.core.security.ProposalDocumentEncryptor;
import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UniqueDocumentValidator implements ConstraintValidator<UniqueDocument, Object> {

    @PersistenceContext
    private final EntityManager em;

    public UniqueDocumentValidator(EntityManager em) {
        this.em = em;
    }

    @Override
    public boolean isValid(Object fieldValue, ConstraintValidatorContext constraintValidatorContext) {
        if (fieldValue == null || fieldValue.equals("")) return false;
        String value = (String) fieldValue;
        String jpql = "SELECT 1 FROM " + Proposal.class.getName() + " WHERE document = :field";
        List<?> result = em.createQuery(jpql).setParameter("field", ProposalDocumentEncryptor.encrypt(value)).getResultList();
        if (result.size() > 0) throw new ApiErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Documento já está cadastrado no sistema.");
        return true;
    }
}