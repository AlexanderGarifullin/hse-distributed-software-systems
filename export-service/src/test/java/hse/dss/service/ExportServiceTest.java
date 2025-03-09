package hse.dss.service;

import hse.dss.entity.Test;
import hse.dss.repository.TestRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExportServiceTest {

    @Mock
    private TestRepository testRepository;

    @InjectMocks
    private ExportService exportService;

    @org.junit.jupiter.api.Test
    public void exportTests_returnsValidZipArchive() throws Exception {
        Long taskId = 1L;
        Test test1 = new Test();
        test1.setId(1);
        test1.setInput("Input data 1");
        Test test2 = new Test();
        test2.setId(2);
        test2.setInput("Input data 2");

        when(testRepository.findByTaskId(taskId)).thenReturn(Arrays.asList(test1, test2));

        byte[] zipBytes = exportService.exportTests(taskId);

        assertNotNull(zipBytes);
        ByteArrayInputStream bais = new ByteArrayInputStream(zipBytes);
        ZipInputStream zis = new ZipInputStream(bais);

        int entryCount = 0;
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            entryCount++;
            String entryName = entry.getName();
            if ("test_1.txt".equals(entryName)) {
                byte[] buffer = new byte[1024];
                int len = zis.read(buffer);
                String content = new String(buffer, 0, len, StandardCharsets.UTF_8);
                assertEquals("Input data 1", content);
            } else if ("test_2.txt".equals(entryName)) {
                byte[] buffer = new byte[1024];
                int len = zis.read(buffer);
                String content = new String(buffer, 0, len, StandardCharsets.UTF_8);
                assertEquals("Input data 2", content);
            } else {
                fail("Unexpected zip entry: " + entryName);
            }
            zis.closeEntry();
        }
        zis.close();
        assertEquals(2, entryCount, "Zip archive should contain 2 entries");
    }

    @org.junit.jupiter.api.Test
    public void exportTestsAsync_returnsValidZipArchive() throws Exception {
        Long taskId = 1L;
        Test test1 = new Test();
        test1.setId(1);
        test1.setInput("Input data 1");

        when(testRepository.findByTaskId(taskId)).thenReturn(Arrays.asList(test1));

        CompletableFuture<byte[]> future = exportService.exportTestsAsync(taskId);
        byte[] zipBytes = future.get();

        assertNotNull(zipBytes);
        ByteArrayInputStream bais = new ByteArrayInputStream(zipBytes);
        ZipInputStream zis = new ZipInputStream(bais);
        ZipEntry entry = zis.getNextEntry();
        assertNotNull(entry, "Expected one zip entry");
        assertEquals("test_1.txt", entry.getName());
        byte[] buffer = new byte[1024];
        int len = zis.read(buffer);
        String content = new String(buffer, 0, len, StandardCharsets.UTF_8);
        assertEquals("Input data 1", content);
        zis.closeEntry();

        assertNull(zis.getNextEntry());
        zis.close();
    }
}
