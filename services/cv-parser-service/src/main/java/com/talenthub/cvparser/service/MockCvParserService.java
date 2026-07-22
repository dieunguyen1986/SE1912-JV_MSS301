package com.talenthub.cvparser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

import java.util.UUID;

@Service
@Slf4j
public class MockCvParserService {

    private final Random random = new Random();

    public void processCvParsing(UUID candidateId, String cvFileUrl) {
        log.info("Started parsing CV for candidateId: {}, url: {}", candidateId, cvFileUrl);

        try {
            // 1. Simulating Processing Time (3 to 5 seconds)
            int sleepTime = 3000 + random.nextInt(2000);
            log.info("Simulating delay of {} ms for parsing...", sleepTime);
            Thread.sleep(sleepTime);

            // 2. Deterministic Mocking based on Input
            if (cvFileUrl != null && cvFileUrl.toLowerCase().contains("error")) {
                log.error("Error trigger found in cvFileUrl: {}", cvFileUrl);
                throw new RuntimeException("Simulated parsing error due to 'error' keyword in filename.");
            }

            log.info("Successfully parsed CV for candidateId: {}", candidateId);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Parsing interrupted for candidateId: {}", candidateId);
            throw new RuntimeException("Thread interrupted", e);
        }
    }
}
