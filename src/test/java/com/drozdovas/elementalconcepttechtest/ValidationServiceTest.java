package com.drozdovas.elementalconcepttechtest;

import com.drozdovas.elementalconcepttechtest.model.InputRecord;
import com.drozdovas.elementalconcepttechtest.service.ValidationService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationServiceTest {

    private final ValidationService validationService = new ValidationService();

    @Test
    void validateRecords_invalidUuid_throwsException() {
        InputRecord record = new InputRecord();
        record.setUuid(null);
        record.setAvgSpeed(10);
        record.setTopSpeed(15);

        assertThrows(IllegalArgumentException.class, () -> {
            validationService.validateRecords(List.of(record));
        });
    }

    @Test
    void validateRecords_invalidSpeeds_throwsException() {
        InputRecord record = new InputRecord();
        record.setUuid("valid-uuid");
        record.setAvgSpeed(20);
        record.setTopSpeed(10); // Invalid as topSpeed < avgSpeed

        assertThrows(IllegalArgumentException.class, () -> {
            validationService.validateRecords(List.of(record));
        });
    }
}
