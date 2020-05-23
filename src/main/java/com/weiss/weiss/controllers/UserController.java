package com.weiss.weiss.controllers;

import com.weiss.weiss.config.UserAuthentication;
import com.weiss.weiss.exceptions.ErrorObj;
import com.weiss.weiss.model.*;
import com.weiss.weiss.model.forum.Message;
import com.weiss.weiss.model.forum.MessageListDto;
import com.weiss.weiss.services.OauthService;
import com.weiss.weiss.services.PostService;
import com.weiss.weiss.services.SystemSettingsService;
import com.weiss.weiss.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserService userService;
    @Autowired
    OauthService oauthService;
    @Autowired
    SystemSettingsService systemSettingsService;
    @Autowired
    PostService postService;

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @RequestMapping("/s/ka")
    public ResponseEntity<Boolean> keepAlive() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @RequestMapping(value = "/p/user", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity registerNewUser(@RequestBody UserInfo userInfo) {
        userInfo.setMail(userInfo.getLogin());
        userInfo.grantRole(Role.ROLE_USER);
        try {
            userService.addNewUser(userInfo);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity(ErrorObj.builder().message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/p/user/{userId}")
    public ResponseEntity<UserInfo> getUser(@PathVariable("userId") String userId) {
        ResponseEntity<UserInfo> response;
        try {
            UserInfo user = userService.findById(userId);
            user.setPassword(null);
            response = new ResponseEntity(user, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @RequestMapping("/s/user")
    public ResponseEntity<UserInfo> getCurrentUserData() {
        UserAuthentication authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = authentication.getUserInfo();
        userInfo.setPassword("");
        return new ResponseEntity(userInfo, HttpStatus.OK);
    }

    @RequestMapping("/s/user/submissions")
    public ResponseEntity<UserInfo> getSubmissions(@RequestParam("skip") long skip) {
        MessageListDto msgList = null;
        try {
            String userId = null;

            UserAuthentication authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
            if(authentication==null || authentication.getUserInfo()==null ){
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            UserInfo userInfo = authentication.getUserInfo();
            userId = userInfo.getId();

            msgList = postService.getMessageListByUserId(userId,skip);
            List<Message> list = msgList.getMessageList().stream().map(message ->{
                message.setResponseTime(new Date().getTime());return message;}
            ).collect(Collectors.toList());
            msgList.setMessageList(list);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity(ErrorObj.builder().message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(msgList, HttpStatus.OK);
    }

//    @RequestMapping("/s/user/message/{userId}")
//    public ResponseEntity<UserInfo> getSubmissions(@PathVariable("userId") String userId, @RequestParam("skip") long skip) {
//        MessageListDto msgList = null;
//        try {
//            msgList = postService.getMessageListByUserId(userId,skip);
//            List<Message> list = msgList.getMessageList().stream().map(message ->{
//                message.setResponseTime(new Date().getTime());return message;}
//            ).collect(Collectors.toList());
//            msgList.setMessageList(list);
//        } catch (IllegalArgumentException e) {
//            LOGGER.error(e.getMessage());
//            return new ResponseEntity(ErrorObj.builder().message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity(msgList, HttpStatus.OK);
//    }


    @RequestMapping(value = "/p/token", method = RequestMethod.GET)
    public BaseToken authorize(@RequestParam("code") String code, @RequestParam("provider") String provider) {
        return oauthService.getToken(code, provider);
    }

    @RequestMapping(value = "/p/systemSettings", method = RequestMethod.GET)
    public SystemSettings getSystemSettings() {
        ClientId settings = systemSettingsService.getSettings();
        return settings;
    }
}
