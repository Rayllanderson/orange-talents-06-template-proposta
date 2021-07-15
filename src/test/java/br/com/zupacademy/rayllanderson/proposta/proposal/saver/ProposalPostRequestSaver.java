package br.com.zupacademy.rayllanderson.proposta.proposal.saver;

import br.com.zupacademy.rayllanderson.proposta.proposal.creators.ProposalPostRequestCreator;
import br.com.zupacademy.rayllanderson.proposta.proposal.requests.ProposalPostRequest;
import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ProposalPostRequestSaver {

    private final MockMvc mockMvc;

    private final Gson gson = new Gson();

    public ProposalPostRequestSaver(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public void saveProposal(ProposalPostRequest request) throws Exception {
        String uri = "/proposals";
        mockMvc.perform(post(uri).content(this.gson.toJson(request)).contentType(MediaType.APPLICATION_JSON));
    }

    public void saveProposalWithCPF() throws Exception {
        ProposalPostRequest request = ProposalPostRequestCreator.createAValidProposalToBeSavedWithCPF();
        saveProposal(request);
    }

    public void saveProposalWithCNPJ() throws Exception {
        ProposalPostRequest request = ProposalPostRequestCreator.createAValidProposalToBeSavedWithCNPJ();
        saveProposal(request);
    }

    public void saveEligibleProposalWithoutCard() throws Exception {
        ProposalPostRequest request = ProposalPostRequestCreator.createAValidProposalToBeSavedWithCPF();
        saveProposal(request);
    }

    public void saveNotEligibleProposal() throws Exception {
        ProposalPostRequest request = ProposalPostRequestCreator.createANotEligibleProposalToBeSavedWithCNPJ();
        saveProposal(request);
    }

}
