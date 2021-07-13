package br.com.zupacademy.rayllanderson.proposta.proposal.creators;

import br.com.zupacademy.rayllanderson.proposta.proposal.requests.ProposalPostRequest;

public class ProposalPostRequestCreator {

    public static ProposalPostRequest createAValidProposalToBeSavedWithCPF(){
        String email = "ray@zup.com.br";
        String document = "01151572047";
        String name = "Ray";
        String address = "avenida qualquer, número 23232, Brasil";
        double salary = 100.0;
        return new ProposalPostRequest(document, email, name, address, salary);
    }

    public static ProposalPostRequest createAValidProposalToBeSavedWithCNPJ(){
        String email = "kaguya@zup.com.br";
        String document = "26067995000100";
        String name = "Kaguya sama";
        String address = "avenida qualquer, número 23232, Brasil";
        double salary = 15000.0;
        return new ProposalPostRequest(document, email, name, address, salary);
    }

    public static ProposalPostRequest createANotEligibleProposalToBeSavedWithCNPJ(){
        String email = "Mikasa@zup.com.br";
        String document = "37093601000104";
        String name = "Mikasa Ackerman";
        String address = "avenida qualquer, número 23232, Brasil";
        double salary = 35000.0;
        return new ProposalPostRequest(document, email, name, address, salary);
    }

    public static ProposalPostRequest createProposalWithDocumentInvalid(){
        String email = "ray@zup.com.br";
        String document = "0147";
        String name = "Ray";
        String address = "avenida qualquer, número 23232, Brasil";
        double salary = 100.0;
        return new ProposalPostRequest(document, email, name, address, salary);
    }

    public static ProposalPostRequest createProposalWithNameBlank(){
        String email = "ray@zup.com.br";
        String document = "01151572047";
        String name = "";
        String address = "avenida qualquer, número 23232, Brasil";
        double salary = 100.0;
        return new ProposalPostRequest(document, email, name, address, salary);
    }

    public static ProposalPostRequest createProposalWithAddressNull(){
        String email = "ray@zup.com.br";
        String document = "01151572047";
        String name = "Ray";
        String address = null;
        double salary = 100.0;
        return new ProposalPostRequest(document, email, name, address, salary);
    }

    public static ProposalPostRequest createProposalWithSalaryNegative(){
        String email = "ray@zup.com.br";
        String document = "01151572047";
        String name = "Ray";
        String address = "avenida qualquer, número 23232, Brasil";
        double salary = - 100.0;
        return new ProposalPostRequest(document, email, name, address, salary);
    }

    public static ProposalPostRequest createProposalWithSalaryNull(){
        String email = "ray@zup.com.br";
        String document = "01151572047";
        String name = "Ray";
        String address = "avenida qualquer, número 23232, Brasil";
        Double salary = null;
        return new ProposalPostRequest(document, email, name, address, salary);
    }
}
