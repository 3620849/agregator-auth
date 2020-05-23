package com.weiss.weiss.controllers;

import com.weiss.weiss.config.UserAuthentication;
import com.weiss.weiss.exceptions.ErrorObj;
import com.weiss.weiss.model.UserInfo;
import com.weiss.weiss.model.forum.ListMessages;
import com.weiss.weiss.model.forum.Message;
import com.weiss.weiss.model.forum.MessageListDto;
import com.weiss.weiss.model.forum.MessageType;
import com.weiss.weiss.services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
        if (authentication instanceof UserAuthentication) {
            UserAuthentication auth = (UserAuthentication) authentication;
            messageMsg.setUserData(auth.getUserInfo());
            messageMsg.setClientId(auth.getClientId());
            messageMsg.setShortContent(postService.truncateContent(messageMsg));
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

    @RequestMapping(value = "/p/message", method = RequestMethod.POST , consumes = "application/json")
    public ResponseEntity getListOfMsg(@RequestBody ListMessages idsList) {
        //TODO implement pagination
        MessageListDto msgListNew = null;
        try {
            if(idsList==null || idsList.getIdsList()==null || idsList.getIdsList().length<1){
                if(idsList.getIdsList().length>20){
                    throw new IllegalArgumentException("list of ids too long max length is 20");
                }
                throw new IllegalArgumentException("id list is empty");
            }
            msgListNew = postService.getMessageListById(idsList);
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
        if (authentication instanceof UserAuthentication) {
            UserAuthentication auth = (UserAuthentication) authentication;
            UserInfo userInfo = auth.getUserInfo();
            userId = userInfo.getId();
            clientId = auth.getClientId();
        }
        boolean result = postService.likeOrDislike(messageId,userId,clientId,value);
        if(result){
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.I_AM_A_TEAPOT);
        }
    }
    /*@RequestMapping(value = "/p/message/{id}", method = RequestMethod.POST , consumes = "application/json")
    public ResponseEntity getSpecificMessage(@RequestParam("id") String messageId) {
        MessageListDto msgListNew = null;
        try {
            if(messageId==null){
                throw new IllegalArgumentException("id list is empty");
            }
            String [] ids={messageId};
            msgListNew = postService.getMessageListById(new ListMessages(ids));
            List<Message> list = msgListNew.getMessageList().stream().map(message ->{
                message.setResponseTime(new Date().getTime());return message;}
            ).collect(Collectors.toList());
            msgListNew.setMessageList(list);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity(ErrorObj.builder().message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(msgListNew, HttpStatus.OK);
    }*/
}
