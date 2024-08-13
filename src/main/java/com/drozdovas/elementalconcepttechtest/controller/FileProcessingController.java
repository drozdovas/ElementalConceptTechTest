package com.drozdovas.elementalconcepttechtest.controller;

import com.drozdovas.elementalconcepttechtest.dto.IpInfo;
import com.drozdovas.elementalconcepttechtest.model.OutputRecord;
import com.drozdovas.elementalconcepttechtest.model.RequestLog;
import com.drozdovas.elementalconcepttechtest.service.FileProcessingService;
import com.drozdovas.elementalconcepttechtest.service.IPValidationService;
import com.drozdovas.elementalconcepttechtest.repository.RequestLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class FileProcessingController {

    private final FileProcessingService fileProcessingService;
    private final IPValidationService ipValidationService;
    private final RequestLogRepository requestLogRepository;

    @Value("${file.validation.skip:false}")
    private boolean skipValidation;

    public FileProcessingController(FileProcessingService fileProcessingService,
                                    IPValidationService ipValidationService,
                                    RequestLogRepository requestLogRepository) {
        this.fileProcessingService = fileProcessingService;
        this.ipValidationService = ipValidationService;
        this.requestLogRepository = requestLogRepository;
    }

    @PostMapping("/process")
    public ResponseEntity<List<OutputRecord>> processFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        UUID requestId = UUID.randomUUID();
        String requestIpAddress = request.getRemoteAddr();
        long startTime = System.currentTimeMillis();

        try {
            IpInfo ipInfo =ipValidationService.validateIp(requestIpAddress);

            List<OutputRecord> outputRecords = fileProcessingService.processFile(file.getInputStream(), skipValidation);

            long timeLapsed = System.currentTimeMillis() - startTime;
            logRequest(requestId, request, ipInfo.getResponseCode(), requestIpAddress, timeLapsed, ipInfo.getCountry(), ipInfo.getIsp());

            if (ipInfo.getResponseCode() == 200)
                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"OutcomeFile.json\"")
                    .body(outputRecords);
            else return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        } catch (RuntimeException | IOException e) {
            long timeLapsed = System.currentTimeMillis() - startTime;
            logRequest(requestId, request, HttpStatus.INTERNAL_SERVER_ERROR.value(), requestIpAddress, timeLapsed, "","");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public void logRequest(UUID requestId, HttpServletRequest request, int responseCode, String ipAddress, long timeLapsed, String country, String isp) {
        RequestLog log = new RequestLog();
        log.setRequestId(requestId);
        log.setRequestUri(request.getRequestURI());
        log.setRequestTimestamp(LocalDateTime.now());
        log.setResponseCode(responseCode);
        log.setRequestIpAddress(ipAddress);
        log.setTimeLapsed(timeLapsed);
        log.setRequestCountryCode(country);
        log.setRequestIpProvider(isp);
        requestLogRepository.save(log);
    }
}