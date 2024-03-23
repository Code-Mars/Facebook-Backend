package com.facebook.data.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.facebook.data.dto.Image;
import com.facebook.data.service.DataService;

@CrossOrigin("*")
@RestController
@RequestMapping("/data")
public class DataAPI {
    @Autowired
    private DataService dataService;

    @PostMapping()
    public ResponseEntity<Image> save(@RequestPart("file") MultipartFile file) throws IOException {
    	Image res=new Image();
    	res.setId(dataService.save(file));
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping("/{assetId}")
    public ResponseEntity<Resource> fetch(@PathVariable String assetId) throws IOException {
        Resource file = dataService.fetch(assetId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
