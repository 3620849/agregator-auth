package com.weiss.weiss.controllers;

import com.weiss.weiss.model.NominationTime;
import com.weiss.weiss.services.NominationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CommonContoller {
    @Autowired
    NominationService nominationService;
    @RequestMapping("/p/nomination/time")
    public ResponseEntity<NominationTime> getNominationTime(){
        NominationTime result = NominationTime.builder().time(nominationService.getNominationTime()).timeout(nominationService.getNominationTimeOut()).build();
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
