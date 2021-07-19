package br.com.zupacademy.rayllanderson.proposta.wallet.clients;

import br.com.zupacademy.rayllanderson.proposta.wallet.requests.WalletExternalRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "card-wallet-associator", url = "#{'${api.cards.url}'}")
public interface CardAssociatorFeign {

    @PostMapping("/{number}/carteiras")
    ResponseEntity<?> associate(@PathVariable String number, @RequestBody @Valid WalletExternalRequest request);
}
