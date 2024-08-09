package com.drozdovas.elementalconcepttechtest.service;

import com.drozdovas.elementalconcepttechtest.model.InputRecord;
import com.drozdovas.elementalconcepttechtest.model.OutputRecord;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileProcessingService {

    private final ValidationService validationService;

    public FileProcessingService(ValidationService validationService) {
        this.validationService = validationService;
    }

    public List<OutputRecord> processFile(InputStream fileStream, boolean skipValidation) throws IOException {
        List<InputRecord> inputRecords = parseFile(fileStream);

        if (!skipValidation) {
            validationService.validateRecords(inputRecords);
        }

        return inputRecords.stream()
                .map(record -> new OutputRecord(record.getName(), record.getTransport(), record.getTopSpeed()))
                .collect(Collectors.toList());
    }

    private List<InputRecord> parseFile(InputStream fileStream) throws IOException {
        List<InputRecord> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|");
                if (fields.length == 7) {
                    InputRecord record = new InputRecord();
                    record.setUuid(fields[0]);
                    record.setId(fields[1]);
                    record.setName(fields[2]);
                    record.setLikes(fields[3]);
                    record.setTransport(fields[4]);
                    record.setAvgSpeed(Double.parseDouble(fields[5]));
                    record.setTopSpeed(Double.parseDouble(fields[6]));
                    records.add(record);
                } else {
                    throw new IOException("Invalid input format");
                }
            }
        }

        return records;
    }
}
