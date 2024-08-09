package com.drozdovas.elementalconcepttechtest.controller;

import com.drozdovas.elementalconcepttechtest.model.OutputRecord;
import com.drozdovas.elementalconcepttechtest.service.FileProcessingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileProcessingController {

    private final FileProcessingService fileProcessingService;

    public FileProcessingController(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @PostMapping("/process")
    public ResponseEntity<List<OutputRecord>> processFile(@RequestParam("file") MultipartFile file) {
        try {
            List<OutputRecord> outputRecords = fileProcessingService.processFile(file.getInputStream());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"OutcomeFile.json\"")
                    .body(outputRecords);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}