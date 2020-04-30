package com.weiss.weiss.dao;

import com.weiss.weiss.model.forum.Post;
import com.weiss.weiss.model.forum.PostListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PostDao {
    @LoadBalanced
    @Autowired
    WebClient.Builder webClient;

    public void save(Post postMsg) {
        webClient.build().post().uri("http://weiss-data/api/post")
                .bodyValue (postMsg)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse-> Mono.error(new IllegalArgumentException("can't create post")))
                .bodyToMono(Void.class)
                .block();
    }
    public PostListDto getListNewPost() {
        PostListDto postList = webClient.build().get().uri("http://weiss-data/api/post")
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new IllegalArgumentException("can't create post")))
                .bodyToMono(PostListDto.class)
                .block();
        return postList;
    }

    public void likeOrDislike(String publicationId, String userId, byte val) {
        webClient.baseUrl("http://weiss-data").build().get().uri(uriBuilder -> uriBuilder.path("/api/likeOrDislike")
                .queryParam("publicationId",publicationId)
                .queryParam("userId",userId)
                .queryParam("value",val).build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new IllegalArgumentException("can't create post")))
                .bodyToMono(PostListDto.class)
                .block();
    }
}
