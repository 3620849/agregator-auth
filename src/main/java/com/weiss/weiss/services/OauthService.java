package com.weiss.weiss.services;

import com.weiss.weiss.model.BaseToken;
import com.weiss.weiss.model.fb.FbToken;
import com.weiss.weiss.model.ggl.GglToken;
import com.weiss.weiss.model.git.GitHubToken;
import com.weiss.weiss.model.vk.VkToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;

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

    @Value("${ggl.client_id}")
    private String gglClientId;
    @Value("${ggl.clientSecret}")
    private String gglSecret;
    @Value("${ggl.redirectUri}")
    private String gglRedirectUrl;

    public BaseToken getToken(String code,String provider) {
       BaseToken token;
        switch (provider){
           case "git": token = getGitToken(code); break;
           case "vk": token = getVkToken(code); break;
           case "fb": token = getFbToken(code); break;
           case "ggl": token = getGglToken(code); break;
           case "twt": token = getTwtToken(code); break;
           default: throw new IllegalArgumentException("Error you send provider: "+provider+" but allowed providers is git, vk, fb, twt, ggl ");
       }
        return token;
    }

    private BaseToken getTwtToken(String code) {
       //TODO implement
        return null;
    }

    private BaseToken getGglToken(String code) {
        LinkedMultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("client_id",gglClientId);
        data.add("client_secret",gglSecret);
        data.add("code",code);
        data.add("grant_type","authorization_code");
        data.add("redirect_uri",gglRedirectUrl);
        GglToken token = webClient.baseUrl("https://oauth2.googleapis.com").build()
                .post().uri(uriBuilder -> uriBuilder.path("/token").build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new AccessDeniedException("cant get github token")))
                .bodyToMono(GglToken.class).block();
        return  token;
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
