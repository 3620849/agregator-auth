package com.weiss.weiss.dao;

import com.weiss.weiss.model.forum.ListMessages;
import com.weiss.weiss.model.forum.Message;
import com.weiss.weiss.model.forum.MessageListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MessageDao {
    @LoadBalanced
    @Autowired
    WebClient.Builder webClient;

    public void save(Message messageMsg) {
        webClient.build().put().uri("http://weiss-data/api/message")
                .bodyValue (messageMsg)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse-> Mono.error(new IllegalArgumentException("can't create post")))
                .bodyToMono(Void.class)
                .block();
    }
    public MessageListDto getMessageListNew(String type, long skip) {
        MessageListDto postList = webClient.baseUrl("http://weiss-data").build().get().uri(uriBuilder -> uriBuilder.path("api/message")
                .queryParam("type",type)
                .queryParam("skip",skip)
                .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new IllegalArgumentException("can't create post")))
                .bodyToMono(MessageListDto.class)
                .block();
        return postList;
    }

    public boolean likeOrDislike(String messageId, String userId, String clientId, byte val) {
        Boolean result = webClient.baseUrl("http://weiss-data").build().get().uri(uriBuilder -> uriBuilder.path("/api/likeOrDislike")
                .queryParam("messageId", messageId)
                .queryParam("userId", userId)
                .queryParam("clientId", clientId)
                .queryParam("value", val).build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new IllegalArgumentException("can't create post")))
                .bodyToMono(Boolean.class)
                .block();
        return result;
    }

    public MessageListDto getMessageListById(ListMessages idList) {
        MessageListDto list = webClient.build().post().uri("http://weiss-data/api/message")
                .bodyValue(idList)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new IllegalArgumentException("can't create post")))
                .bodyToMono(MessageListDto.class)
                .block();
        return list;
    }

    public MessageListDto getMessageListByUserId(String userId, long skip) {
        MessageListDto postList = webClient.baseUrl("http://weiss-data").build().get().uri(uriBuilder -> uriBuilder.path("api/user/message")
                .queryParam("userId",userId)
                .queryParam("skip",skip)
                .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new IllegalArgumentException("can't create post")))
                .bodyToMono(MessageListDto.class)
                .block();
        return postList;
    }
}
