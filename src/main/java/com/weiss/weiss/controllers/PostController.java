package com.weiss.weiss.controllers;

import com.weiss.weiss.config.UserAuthentication;
import com.weiss.weiss.exceptions.ErrorObj;
import com.weiss.weiss.model.UserInfo;
import com.weiss.weiss.model.forum.Post;
import com.weiss.weiss.model.forum.PostListDto;
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
public class PostController {
    @Autowired
    PostService postService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/p/post", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity registerNewUser(@RequestBody Post postMsg) {
        //TODO generate shortContent in post object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            Object principal = ((AnonymousAuthenticationToken) authentication).getPrincipal();
            //TODO get client id and set in post
            //set user as anonymous
        }
        if (authentication instanceof UserAuthentication) {
            UserInfo userInfo = ((UserAuthentication) authentication).getUserInfo();
            postMsg.setClientId(userInfo.getCurrentClientId());
            postMsg.setUserId(userInfo.getId());
        }
        try {
            postService.savePost(postMsg);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity(ErrorObj.builder().message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/p/post", method = RequestMethod.GET)
    public ResponseEntity registerNewUser(@RequestParam("type") String type, @RequestParam("page") int page) {
        //TODO implement pagination
        PostListDto postListNew = null;
        try {
            postListNew = postService.getPostListNew();
            List<Post> list = postListNew.getPostList().stream().map(post ->{
                post.setResponseTime(new Date().getTime());return post;}
            ).collect(Collectors.toList());
            postListNew.setPostList(list);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity(ErrorObj.builder().message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(postListNew, HttpStatus.OK);
    }

    @RequestMapping(value = "/p/likeOrDislike", method = RequestMethod.GET)
    public ResponseEntity likeOrDislike(@RequestParam("id") String publicationId,@RequestParam("val")byte value) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId ="";
        if (authentication instanceof AnonymousAuthenticationToken) {
            Object principal = ((AnonymousAuthenticationToken) authentication).getPrincipal();
            //TODO get client id and set it
            userId="AN";
        }
        if (authentication instanceof UserAuthentication) {
            UserInfo userInfo = ((UserAuthentication) authentication).getUserInfo();
            userId = userInfo.getCurrentClientId();
        }
        postService.likeOrDislike(publicationId,userId,value);
        return new ResponseEntity(HttpStatus.OK);
    }
}
