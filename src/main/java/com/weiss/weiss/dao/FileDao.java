package com.weiss.weiss.dao;

import com.weiss.weiss.model.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FileDao {
    @LoadBalanced
    @Autowired
    private WebClient.Builder webClient;

    public Media sendFile(LinkedMultiValueMap<String, Object> file) {
        return webClient.build().post().uri("http://file-storage/api/upload")
                .bodyValue (file)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse->Mono.error(new IllegalArgumentException("can't add")))
                .onStatus(HttpStatus::is5xxServerError, clientResponse->Mono.error(new IllegalArgumentException("something wrong on server")))
                .bodyToMono(Media.class)
                .block();
    }
}
