package br.com.zupacademy.rayllanderson.proposta.core.security;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

class ProposalDocumentEncryptorTest {

    private final Logger logger = LoggerFactory.getLogger(ProposalDocumentEncryptorTest.class);

    @Test
    void shouldTestCryptography(){
        String normalText = "13729270001";
        String encryptedText = ProposalDocumentEncryptor.encrypt(normalText);
        String decryptedText = ProposalDocumentEncryptor.decrypt(encryptedText);

        logger.info("Texto Normal = {}", normalText);
        logger.info("Texto Criptografado = {}", encryptedText);
        logger.info("Texto Descriptografado = {}", decryptedText);

        assertThat(encryptedText).isNotEqualTo(normalText);
        assertThat(encryptedText).isNotEqualTo(decryptedText);

        assertThat(decryptedText).isEqualTo(normalText);
        assertThat(normalText).isEqualTo(decryptedText);
    }

}