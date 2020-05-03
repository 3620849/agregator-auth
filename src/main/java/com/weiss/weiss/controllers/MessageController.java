package com.weiss.weiss.controllers;

import com.weiss.weiss.config.UserAuthentication;
import com.weiss.weiss.exceptions.ErrorObj;
import com.weiss.weiss.model.UserInfo;
import com.weiss.weiss.model.forum.Message;
import com.weiss.weiss.model.forum.MessageListDto;
import com.weiss.weiss.model.forum.MessageType;
import com.weiss.weiss.services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MessageController {
    @Autowired
    PostService postService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/p/message", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity registerNewUser(@RequestBody Message messageMsg) {
        //TODO generate shortContent in post object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            Object principal = ((AnonymousAuthenticationToken) authentication).getPrincipal();
            //TODO get client id and set in post
            //set user as anonymous
        }
        if (authentication instanceof UserAuthentication) {
            UserInfo userInfo = ((UserAuthentication) authentication).getUserInfo();
            messageMsg.setClientId(userInfo.getCurrentClientId());
            messageMsg.setUserId(userInfo.getId());
        }
        if(messageMsg.getAncestorId()==null){
            messageMsg.setType(MessageType.POST);
        }else {
            messageMsg.setType(MessageType.COMMENT);
        }
        try {
            postService.saveMessage(messageMsg);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity(ErrorObj.builder().message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/p/message", method = RequestMethod.GET)
    public ResponseEntity getListOfMsg(@RequestParam("type") String type, @RequestParam("skip") long skip) {
        //TODO implement pagination
        MessageListDto msgListNew = null;
        try {
            msgListNew = postService.getMessageListNew(type,skip);
            List<Message> list = msgListNew.getMessageList().stream().map(message ->{
                message.setResponseTime(new Date().getTime());return message;}
            ).collect(Collectors.toList());
            msgListNew.setMessageList(list);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity(ErrorObj.builder().message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(msgListNew, HttpStatus.OK);
    }

    @RequestMapping(value = "/p/likeOrDislike", method = RequestMethod.GET)
    public ResponseEntity likeOrDislike(@RequestParam("messageId") String messageId,
                                        @RequestParam("value")byte value) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId =null;
        String clientId =null;
        if (authentication instanceof AnonymousAuthenticationToken) {
            Object principal = ((AnonymousAuthenticationToken) authentication).getPrincipal();
            //TODO get client id and set it
            clientId ="123";
        }
        if (authentication instanceof UserAuthentication) {
            UserInfo userInfo = ((UserAuthentication) authentication).getUserInfo();
            userId = userInfo.getCurrentClientId();
        }
        boolean result = postService.likeOrDislike(messageId,userId,clientId,value);
        if(result){
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.I_AM_A_TEAPOT);
        }
    }
}