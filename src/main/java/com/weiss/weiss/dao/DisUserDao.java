package com.weiss.weiss.dao;

import com.weiss.weiss.model.dis.DisUserDto;
import com.weiss.weiss.model.dis.DisqusResponse;
import com.weiss.weiss.model.git.GitHubUser;
import com.weiss.weiss.model.vk.VkUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DisUserDao {
    @Value("${dis.apiKey}")
    private String disClientId;
    @Value("${dis.apiSecret}")
    private String disSecret;
    @Autowired
    private WebClient.Builder webClient;

    public DisqusResponse getDisUserData(String token) {
        DisUserDto disUser = webClient.baseUrl("https://disqus.com").build().get()
                .uri(uriBuilder -> uriBuilder.path("/api/3.0/users/details.json")
                        .queryParam("api_secret",disSecret)
                        .queryParam("access_token",token)
                        .queryParam("api_key",disClientId)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new UsernameNotFoundException("cant read disqus token")))
                .bodyToMono(DisUserDto.class).block();
        if(disUser.getCode()!=0){
            throw new UsernameNotFoundException("cant read disqus token");
        }
        return disUser.getResponse();
    }
}
