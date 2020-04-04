package com.weiss.weiss.dao;

import com.weiss.weiss.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public class UserDAO {
    @LoadBalanced
    @Autowired
    WebClient.Builder webClient;
    public UserInfo findUserById(String id ){
        UserInfo userInfo = webClient.build()
                .get().uri("http://weiss-data/api/getUserById/"+id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse->Mono.error(new UsernameNotFoundException("no such user")))
                .bodyToMono(UserInfo.class).block();
        return userInfo;
    }
    public UserInfo findUserByName(String name ){
        UserInfo userInfo = webClient.build()
                .get().uri("http://weiss-data/api/getUserByName/"+name)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse->Mono.error(new UsernameNotFoundException("no such user")))
                .bodyToMono(UserInfo.class).block();
        return userInfo;
    }

    public void addUser(UserInfo user) {
        webClient.build().post().uri("http://weiss-data/api/addUser")
                .bodyValue (user)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse->Mono.error(new IllegalArgumentException("User with name "+ user.getUsername()+" already exist")))
                .bodyToMono(Void.class)
                .block();
    }
    public UserInfo findUserByLogin(String login ) {
        UserInfo userInfo = webClient.build()
                .get().uri("http://weiss-data/api/getUserByLogin/" + login)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new UsernameNotFoundException("no such user")))
                .bodyToMono(UserInfo.class).block();
        return userInfo;
    }
}
