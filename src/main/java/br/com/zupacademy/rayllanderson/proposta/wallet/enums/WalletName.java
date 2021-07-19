package br.com.zupacademy.rayllanderson.proposta.wallet.enums;

public enum WalletName {
    PAYPAL("paypal"), SAMSUNG_PAY("samsung-pay");

    private final String endpointValue;

    WalletName(String value) {
        this.endpointValue = value;
    }

    public String getEndpointValue() {
        return endpointValue;
    }
}
