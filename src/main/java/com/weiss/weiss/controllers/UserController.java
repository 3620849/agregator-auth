package com.weiss.weiss.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @RequestMapping("/user/{userId}")
    public String getUser(@PathVariable("userId") String userId){
        SecurityContext context = SecurityContextHolder.getContext();
        return ""+context.getAuthentication().getDetails().toString() + "'}";
    }
    @RequestMapping("/authenticate")
    public String getAuth(  String userId){
        return "123";
    }

}
