package com.weiss.weiss.dao;

import com.weiss.weiss.model.git.GitHubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GitHubUserDao {

    @Autowired
    private WebClient.Builder webClient;

    public GitHubUser getGitHubUserData(String token) {
        GitHubUser userInfo = webClient.baseUrl("https://api.github.com").build().get()
                .uri(uriBuilder -> uriBuilder.path("/user").build())
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "token " + token)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new UsernameNotFoundException("cant read github token")))
                .bodyToMono(GitHubUser.class).block();
        return userInfo;
    }
}
