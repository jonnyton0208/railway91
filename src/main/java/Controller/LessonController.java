package Controller;

import DTOs.LessonDTO;
import Service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/courses/{courseId}/lessons")
    public ResponseEntity<Page<LessonDTO>> getLessonsByCourse(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(lessonService.getLessonsByCourse(courseId, pageable));
    }

    @GetMapping("/lessons/{lessonId}")
    public ResponseEntity<LessonDTO> getLessonById(@PathVariable Long lessonId) {
        return ResponseEntity.ok(lessonService.getLessonById(lessonId));
    }

    @PostMapping("/courses/{courseId}/lessons")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LessonDTO> createLesson(
            @PathVariable Long courseId,
            @RequestBody LessonDTO lessonDTO) {
        return ResponseEntity.ok(lessonService.createLesson(courseId, lessonDTO));
    }

    @PutMapping("/lessons/{lessonId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LessonDTO> updateLesson(
            @PathVariable Long lessonId,
            @RequestBody LessonDTO lessonDTO) {
        return ResponseEntity.ok(lessonService.updateLesson(lessonId, lessonDTO));
    }

    @DeleteMapping("/lessons/{lessonId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.ok().build();
    }
} 