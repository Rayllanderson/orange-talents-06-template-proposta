package br.com.zupacademy.rayllanderson.proposta.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * Usado para executar transações serem independentes, assim serão executadas separadamente.
 */
@Component
public class TransactionExecutor {

    private final EntityManager manager;

    public TransactionExecutor(EntityManager manager) {
        this.manager = manager;
    }

    @Transactional
    public <T> T save(T object){
        this.manager.persist(object);
        return object;
    }

    @Transactional
    public <T> T update(T object){
        return this.manager.merge(object);
    }
}
