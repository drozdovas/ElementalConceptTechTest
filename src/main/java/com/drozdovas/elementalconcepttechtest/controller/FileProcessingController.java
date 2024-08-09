package com.drozdovas.elementalconcepttechtest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileProcessingController {

    @PostMapping("/process")
    public ResponseEntity<String> processFile(@RequestParam("file") MultipartFile file) {

            return ResponseEntity.ok().body("Success");
    }
}