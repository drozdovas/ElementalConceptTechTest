package com.drozdovas.elementalconcepttechtest;

import com.drozdovas.elementalconcepttechtest.dto.IpInfo;
import com.drozdovas.elementalconcepttechtest.model.OutputRecord;
import com.drozdovas.elementalconcepttechtest.service.FileProcessingService;
import com.drozdovas.elementalconcepttechtest.service.IPValidationService;
import com.drozdovas.elementalconcepttechtest.repository.RequestLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FileProcessingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FileProcessingService fileProcessingService;

    @MockBean
    private IPValidationService ipValidationService;

    @MockBean
    private RequestLogRepository requestLogRepository;

    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        mockFile = new MockMultipartFile("file", "EntryFile.txt", "text/plain",
                "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1".getBytes());
    }

    @Test
    void processFile_validRequest_returnsOk() throws Exception {
        // Mock the IP validation response
        IpInfo ipInfo = new IpInfo(200);
        ipInfo.setCountry("United Kingdom");
        ipInfo.setIsp("BT");

        when(ipValidationService.validateIp(anyString())).thenReturn(ipInfo);

        // Mock the file processing service to return a sample output
        OutputRecord outputRecord = new OutputRecord("John Smith", "Rides A Bike", 12.1);
        when(fileProcessingService.processFile(any(), anyBoolean())).thenReturn(Collections.singletonList(outputRecord));

        // Perform the POST request with the mock file
        mockMvc.perform(multipart("/api/files/process")
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"OutcomeFile.json\""))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(outputRecord))));

        // Verify that the logRequest method was called
        Mockito.verify(requestLogRepository).save(any());
    }

    @Test
    void processFile_ipValidationFails_returnsForbidden() throws Exception {
        // Mock the IP validation to return an empty Optional, indicating failure
        when(ipValidationService.validateIp(any())).thenReturn(new IpInfo(403));

        // Perform the POST request with the mock file
        mockMvc.perform(multipart("/api/files/process")
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isForbidden());

        // Verify that the logRequest method was called
        Mockito.verify(requestLogRepository).save(any());
    }
}
