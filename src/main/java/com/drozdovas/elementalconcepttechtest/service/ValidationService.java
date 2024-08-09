package com.drozdovas.elementalconcepttechtest.service;

import com.drozdovas.elementalconcepttechtest.model.InputRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationService {

    public void validateRecords(List<InputRecord> records) {
        for (InputRecord record : records) {
            validateUuid(record.getUuid());
            validateSpeeds(record.getAvgSpeed(), record.getTopSpeed());
        }
    }

    private void validateUuid(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            throw new IllegalArgumentException("UUID is invalid");
        }
    }

    private void validateSpeeds(double avgSpeed, double topSpeed) {
        if (avgSpeed < 0 || topSpeed < 0) {
            throw new IllegalArgumentException("Speeds must be non-negative");
        }
        if (topSpeed < avgSpeed) {
            throw new IllegalArgumentException("Top speed must be greater than or equal to average speed");
        }
    }
}