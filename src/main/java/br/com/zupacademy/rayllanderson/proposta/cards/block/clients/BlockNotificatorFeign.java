package br.com.zupacademy.rayllanderson.proposta.cards.block.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(value = "card-block", url = "#{'${api.cards.url}'}")
public interface BlockNotificatorFeign {

    @PostMapping("/{number}/bloqueios")
    String notify(@PathVariable String number, Map<String, String> request);
}
