package br.com.zupacademy.rayllanderson.proposta.travel.clients;

import br.com.zupacademy.rayllanderson.proposta.travel.request.TravelNotifyExternalRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "card-travel-notify", url = "#{'${api.cards.url}'}")
public interface TravelNotifyExternalFeign {

    @PostMapping("/{number}/avisos")
    String notify(@PathVariable String number, TravelNotifyExternalRequest request);
}
