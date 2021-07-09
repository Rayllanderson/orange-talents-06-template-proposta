package br.com.zupacademy.rayllanderson.proposta.utils;

import javax.validation.constraints.NotBlank;

public class HeaderUtils {

    public static long getReturnedId(@NotBlank String header){
        return Long.parseLong(header.substring(header.lastIndexOf("/") + 1));
    }
}
