package com.weiss.weiss.controllers;

import com.weiss.weiss.exceptions.ErrorObj;
import com.weiss.weiss.model.forum.Message;
import com.weiss.weiss.model.forum.MessageListDto;
import com.weiss.weiss.services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    PostService postService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @RequestMapping(value = "/p/comment/{id}", method = RequestMethod.GET)
    public ResponseEntity getParticularMsg(@PathVariable("id") String parentId) {
        MessageListDto msgListNew = null;
        try {
            msgListNew = postService.getCommentsById(parentId);
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

}
