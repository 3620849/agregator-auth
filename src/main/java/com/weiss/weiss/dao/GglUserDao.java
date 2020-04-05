package com.weiss.weiss.dao;

import com.weiss.weiss.model.fb.FbUser;
import com.weiss.weiss.model.ggl.GglUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Service
public class GglUserDao {
    @Autowired
    private WebClient.Builder webClient;

    public GglUser getGglUserData(String token) {
        GglUser gglUser = webClient.baseUrl("https://www.googleapis.com").build().get()
                .uri(uriBuilder -> uriBuilder.path("/oauth2/v2/userinfo").build())
                .header("Authorization","Bearer "+token)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new UsernameNotFoundException("cant read github token")))
                .bodyToMono(GglUser.class).block();
        return gglUser;
    }
}
