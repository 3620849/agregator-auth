package com.weiss.weiss.services;

import com.weiss.weiss.model.BaseToken;
import com.weiss.weiss.model.fb.FbToken;
import com.weiss.weiss.model.git.GitHubToken;
import com.weiss.weiss.model.vk.VkToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OauthService {
    @Autowired
    private WebClient.Builder webClient;
    @Value("${github.client_id}")
    private String gitClientId;
    @Value("${github.clientSecret}")
    private String gitSecret;

    @Value("${vk.client_id}")
    private String vkClientId;
    @Value("${vk.clientSecret}")
    private String vkSecret;
    @Value("${vk.redirectUri}")
    private String vkRedirectUrl;

    @Value("${fb.client_id}")
    private String fbClientId;
    @Value("${fb.clientSecret}")
    private String fbSecret;
    @Value("${fb.redirectUri}")
    private String fbRedirectUrl;

    public BaseToken getToken(String code,String provider) {
       BaseToken token;
        switch (provider){
           case "git": token = getGitToken(code); break;
           case "vk": token = getVkToken(code); break;
           case "fb": token = getFbToken(code); break;
           default: throw new IllegalArgumentException("Error you send provider: "+provider+" but allowed providers is git, vk, fb, twt, ggl ");
       }
        return token;
    }
    private BaseToken getFbToken (String code){
        FbToken token = webClient.baseUrl("https://graph.facebook.com").build()
                .get().uri(uriBuilder -> uriBuilder.path("/v6.0/oauth/access_token")
                        .queryParam("client_id", fbClientId)
                        .queryParam("client_secret", fbSecret)
                        .queryParam("redirect_uri", fbRedirectUrl)
                        .queryParam("code", code)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new AccessDeniedException("cant get github token")))
                .bodyToMono(FbToken.class).block();
        return  token;
    }
    private BaseToken getVkToken (String code){
        VkToken token = webClient.baseUrl("https://oauth.vk.com").build()
                .get().uri(uriBuilder -> uriBuilder.path("/access_token")
                        .queryParam("client_id", vkClientId)
                        .queryParam("client_secret", vkSecret)
                        .queryParam("redirect_uri", vkRedirectUrl)
                        .queryParam("scope", "market")
                        .queryParam("code", code)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new AccessDeniedException("cant get github token")))
                .bodyToMono(VkToken.class).block();
        return  token;
    }
    private BaseToken getGitToken(String code) {
        GitHubToken token = webClient.baseUrl("https://github.com").build()
                .post().uri(uriBuilder -> uriBuilder.path("/login/oauth/access_token")
                        .queryParam("client_id", gitClientId)
                        .queryParam("client_secret", gitSecret)
                        .queryParam("code", code)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new AccessDeniedException("cant get github token")))
                .bodyToMono(GitHubToken.class).block();
        return token;
    }


}
