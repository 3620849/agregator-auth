package com.weiss.weiss.controllers;

import com.weiss.weiss.dao.FileDao;
import com.weiss.weiss.model.Media;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileController {
    @Autowired
    FileDao fileDao;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @PostMapping(value = "/p/upload",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadFile(@RequestPart MultipartFile file){
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        if(!file.isEmpty()){
            map.add("file",file.getResource());
        }
        Media media =null;
        try{media = fileDao.sendFile(map);}catch (IllegalArgumentException e){
            LOGGER.error(e.getMessage());
            return new ResponseEntity(HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity(media, HttpStatus.OK);
    }
}
