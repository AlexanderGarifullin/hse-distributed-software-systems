package hse.dss.controller;

import hse.dss.entity.Test;
import hse.dss.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/tasks/{taskId}/tests")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    // Список тестов для задачи
    @GetMapping
    public String listTests(@PathVariable Long taskId, Model model) {
        List<Test> tests = testService.getTestsByTaskId(taskId);
        model.addAttribute("tests", tests);
        model.addAttribute("taskId", taskId);
        return "tests"; // Отобразится шаблон tests.html
    }

    // Скачать тесты в виде ZIP-архива
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadTests(@PathVariable Long taskId) {
        byte[] zipData = testService.exportTests(taskId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "tests_" + taskId + ".zip");
        return ResponseEntity.ok().headers(headers).body(zipData);
    }

    // Инициировать асинхронную генерацию тестов (по умолчанию 5, можно указать другое количество)
    @PostMapping("/generate")
    public String generateTests(@PathVariable Long taskId,
                                @RequestParam(defaultValue = "5") int count,
                                Model model) {
        testService.requestTestGeneration(taskId, count);
        model.addAttribute("message", "Test generation initiated.");
        return "redirect:/tasks/" + taskId + "/tests";
    }

    // Форма редактирования теста (показывает полный input)
    @GetMapping("/{testId}/edit")
    public String editTestForm(@PathVariable Long taskId,
                               @PathVariable Integer testId,
                               Model model) {
        Test test = testService.getTestById(testId);
        model.addAttribute("test", test);
        model.addAttribute("taskId", taskId);
        return "editTest";
    }

    // Обновление теста
    @PostMapping("/{testId}")
    public String updateTest(@PathVariable Long taskId,
                             @PathVariable Integer testId,
                             @RequestParam String input,
                             Model model) {
        Test test = testService.getTestById(testId);
        if (test != null) {
            testService.updateTest(test, input);
        }
        return "redirect:/tasks/" + taskId + "/tests";
    }

    // Удаление теста
    @PostMapping("/{testId}/delete")
    public String deleteTest(@PathVariable Long taskId,
                             @PathVariable Integer testId,
                             Model model) {
        testService.deleteTest(testId);
        return "redirect:/tasks/" + taskId + "/tests";
    }
}
