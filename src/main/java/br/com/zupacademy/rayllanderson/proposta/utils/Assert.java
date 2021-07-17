package br.com.zupacademy.rayllanderson.proposta.utils;

import br.com.zupacademy.rayllanderson.proposta.core.exceptions.ApiErrorException;
import org.springframework.http.HttpStatus;

public class Assert {

    /**
     * Verifica se o ip ou user agent estão nulos.
     * @throws ApiErrorException  Caso algum campo esteja nulo
     */
    public static void notNull(String ip, String userAgent) throws ApiErrorException{
        if(ip == null || userAgent == null){
            throw new ApiErrorException(HttpStatus.BAD_REQUEST, "Endereço ip ou user agent não podem ser nulos");
        }
        if (ip.isBlank() || userAgent.isBlank()){
            throw new ApiErrorException(HttpStatus.BAD_REQUEST, "Endereço ip ou user agent não podem ser vazios");
        }
    }
}
