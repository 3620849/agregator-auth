package com.weiss.weiss.dao;

import com.weiss.weiss.model.vk.VkUser;
import com.weiss.weiss.model.vk.VkUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class VkUserDao {
    @Autowired
    private WebClient.Builder webClient;

    public VkUser getVkUserData(String token) {
        VkUserDto vuUserDto = webClient.baseUrl("https://api.vk.com").build().get()
                .uri(uriBuilder -> uriBuilder.path("/method/users.get")
                        .queryParam("fields","photo_100,contacts,bdate,city,country,home_town,sex")
                        .queryParam("access_token",token)
                        .queryParam("v","5.103")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new UsernameNotFoundException("cant read vk token")))
                .bodyToMono(VkUserDto.class).block();
        if(vuUserDto==null || vuUserDto.getResponse()==null || vuUserDto.getResponse().length<1){
            new UsernameNotFoundException("error vk response empty");
        }
        return vuUserDto.getResponse()[0];
    }
}
