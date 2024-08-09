package com.drozdovas.elementalconcepttechtest;

import com.drozdovas.elementalconcepttechtest.model.OutputRecord;
import com.drozdovas.elementalconcepttechtest.service.FileProcessingService;
import com.drozdovas.elementalconcepttechtest.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FileProcessingServiceTest {

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private FileProcessingService fileProcessingService;

    public FileProcessingServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processFile_validInput_success() throws IOException {
        String inputFileContent = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputFileContent.getBytes());

        List<OutputRecord> result = fileProcessingService.processFile(inputStream, false);

        assertEquals(1, result.size());
        assertEquals("John Smith", result.get(0).getName());
        assertEquals("Rides A Bike", result.get(0).getTransport());
        assertEquals(12.1, result.get(0).getTopSpeed());

        verify(validationService, times(1)).validateRecords(any());
    }

    @Test
    void processFile_invalidInputFormat_throwsException() {
        String inputFileContent = "random text";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputFileContent.getBytes());

        assertThrows(IOException.class, () -> {
            fileProcessingService.processFile(inputStream, false);
        });
    }
}