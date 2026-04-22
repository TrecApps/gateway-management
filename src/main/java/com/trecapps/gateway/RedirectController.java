package com.trecapps.gateway;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class RedirectController {

    @GetMapping
    Mono<ResponseEntity<Void>> redirect(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/Pause/");
        ResponseEntity<Void> ret = new ResponseEntity<>(headers, HttpStatus.FOUND);
        return Mono.just(ret);
    }
}
