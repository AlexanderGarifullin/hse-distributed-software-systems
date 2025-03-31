package hse.dss.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/export/tests")
@RequiredArgsConstructor
public class ExportController {

    private final hse.dss.service.ExportService exportService;

    @GetMapping
    public ResponseEntity<ByteArrayResource> exportTests(@RequestParam Long taskId) {
        log.info("Received export request for taskId: {}", taskId);
        byte[] zipBytes = exportService.exportTests(taskId);
        if (zipBytes == null || zipBytes.length == 0) {
            log.warn("No tests found for taskId: {} or ZIP archive is empty", taskId);
        } else {
            log.info("ZIP archive created successfully for taskId: {} ({} bytes)", taskId, zipBytes.length);
        }
        ByteArrayResource resource = new ByteArrayResource(zipBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tests_" + taskId + ".zip");
        log.debug("Returning ZIP file for taskId: {}", taskId);
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
