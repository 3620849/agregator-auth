package com.weiss.weiss.dao;

import com.weiss.weiss.model.dis.DisUserDto;
import com.weiss.weiss.model.dis.DisqusResponse;
import com.weiss.weiss.model.ggl.GglUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Repository
public class AndroidUserDao {
    @Autowired
    private WebClient.Builder webClient;

    public GglUser getUserData(String token) {
        GglUser userData = webClient.baseUrl("https://oauth2.googleapis.com").build().get()
                .uri(uriBuilder -> uriBuilder.path("/tokeninfo")
                        .queryParam("id_token",token)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new UsernameNotFoundException("cant read disqus token")))
                .bodyToMono(GglUser.class).block();
        return userData;
    }
}
