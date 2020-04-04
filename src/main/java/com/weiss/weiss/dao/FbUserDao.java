package com.weiss.weiss.dao;

import com.weiss.weiss.model.fb.FbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Service
public class FbUserDao {
    @Autowired
    private WebClient.Builder webClient;

    public FbUser getFbUserData(String token) {
        FbUser fbUser = webClient.baseUrl("https://graph.facebook.com").build().get()
                .uri(uriBuilder -> uriBuilder.path("/me")
                        .queryParam("fields","id,about,email,gender,last_name,first_name,languages,picture.type(normal)")
                        .queryParam("access_token",token)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new UsernameNotFoundException("cant read github token")))
                .bodyToMono(FbUser.class).block();
        return fbUser;
    }
}
