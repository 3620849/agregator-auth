package com.weiss.weiss.dao;

import com.weiss.weiss.model.NominationTime;
import com.weiss.weiss.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class NominationTimeDao {
    @LoadBalanced
    @Autowired
    WebClient.Builder webClient;

    public void saveTimers(NominationTime nt) {
        webClient.build().post().uri("http://weiss-data/api/nomination")
                .bodyValue (nt)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse-> Mono.error(new IllegalArgumentException("Can't save nomination time")))
                .bodyToMono(Void.class)
                .block();
    }

    public NominationTime getNominationTime() {
        NominationTime nt = webClient.build()
                .get().uri("http://weiss-data/api/nomination")
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse->Mono.error(new UsernameNotFoundException("no such data")))
                .bodyToMono(NominationTime.class).block();
        return nt;
    }
}
