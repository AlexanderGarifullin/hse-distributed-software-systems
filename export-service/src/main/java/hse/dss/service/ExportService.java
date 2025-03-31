package hse.dss.service;

import hse.dss.entity.Test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportService {

    private final hse.dss.repository.TestRepository testRepository;

    public byte[] exportTests(Long taskId) {
        log.info("Starting export of tests for taskId: {}", taskId);
        List<Test> tests = testRepository.findByTaskId(taskId);
        log.info("Found {} tests for taskId: {}", tests.size(), taskId);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (Test test : tests) {
                String fileName = "test_" + test.getId() + ".txt";
                log.debug("Adding file {} for test id: {}", fileName, test.getId());
                zos.putNextEntry(new ZipEntry(fileName));
                byte[] data = test.getInput().getBytes(StandardCharsets.UTF_8);
                zos.write(data);
                zos.closeEntry();
            }
            zos.finish();
            log.info("Successfully created ZIP archive for taskId: {} ({} bytes)", taskId, baos.size());
        } catch (IOException e) {
            log.error("Error creating ZIP archive for taskId: {}: {}", taskId, e.getMessage(), e);
            throw new RuntimeException("Error creating ZIP archive", e);
        }
        return baos.toByteArray();
    }

    @Async
    public CompletableFuture<byte[]> exportTestsAsync(Long taskId) {
        log.info("Starting asynchronous export for taskId: {}", taskId);
        byte[] zip = exportTests(taskId);
        log.info("Asynchronous export completed for taskId: {} ({} bytes)", taskId, zip.length);
        return CompletableFuture.completedFuture(zip);
    }
}
