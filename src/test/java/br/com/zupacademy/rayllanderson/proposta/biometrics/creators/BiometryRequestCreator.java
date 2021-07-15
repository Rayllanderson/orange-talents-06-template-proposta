package br.com.zupacademy.rayllanderson.proposta.biometrics.creators;

import br.com.zupacademy.rayllanderson.proposta.biometrics.requets.BiometryRequest;

public class BiometryRequestCreator {

    public static BiometryRequest createAValidBiometryToBeSaved(){
        return new BiometryRequest("2b9p59dxm2/D+DXxSZAhoQ==");
    }

    public static BiometryRequest createInvalidBiometry(){
        return new BiometryRequest("54c531 qualquer coisa 6414");
    }

}
