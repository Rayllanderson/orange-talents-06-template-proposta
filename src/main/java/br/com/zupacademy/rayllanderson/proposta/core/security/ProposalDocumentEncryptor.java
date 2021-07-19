package br.com.zupacademy.rayllanderson.proposta.core.security;

import org.springframework.security.crypto.encrypt.Encryptors;

public class ProposalDocumentEncryptor {

    private static final String SALT = "4b192a8189607797";

    public static String encrypt(String text){
        return Encryptors.queryableText("document", SALT).encrypt(text);
    }

    public static String decrypt(String text){
        return Encryptors.queryableText("document", SALT).decrypt(text);
    }

    /**
     * Usado para testes.
     * Use o método estático ao invés desse.
     * @deprecated
     */
    public String encryptTEST(String text){
        return Encryptors.queryableText("document", SALT).encrypt(text);
    }
}
