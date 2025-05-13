package Service;

import DTOs.LessonDTO;
import Entity.Course;
import Entity.Lesson;
import Repository.CourseRepository;
import Repository.LessonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    public Page<LessonDTO> getLessonsByCourse(Long courseId, Pageable pageable) {
        return lessonRepository.findByCourseId(courseId, pageable)
                .map(this::convertToDTO);
    }

    public LessonDTO getLessonById(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bài học với ID: " + lessonId));
        return convertToDTO(lesson);
    }

    @Transactional
    public LessonDTO createLesson(Long courseId, LessonDTO lessonDTO) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khóa học với ID: " + courseId));

        Lesson lesson = convertToEntity(lessonDTO);
        lesson.setCourse(course);
        
        Lesson savedLesson = lessonRepository.save(lesson);
        return convertToDTO(savedLesson);
    }

    @Transactional
    public LessonDTO updateLesson(Long lessonId, LessonDTO lessonDTO) {
        Lesson existingLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bài học với ID: " + lessonId));

        updateLessonFromDTO(existingLesson, lessonDTO);
        Lesson updatedLesson = lessonRepository.save(existingLesson);
        return convertToDTO(updatedLesson);
    }

    @Transactional
    public void deleteLesson(Long lessonId) {
        if (!lessonRepository.existsById(lessonId)) {
            throw new EntityNotFoundException("Không tìm thấy bài học với ID: " + lessonId);
        }
        lessonRepository.deleteById(lessonId);
    }

    private LessonDTO convertToDTO(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setDurationHours(lesson.getDurationHours());
        dto.setContent(lesson.getContent());
        dto.setCourseId(lesson.getCourse() != null ? lesson.getCourse().getId() : null);
        return dto;
    }

    private Lesson convertToEntity(LessonDTO dto) {
        Lesson lesson = new Lesson();
        lesson.setId(dto.getId());
        lesson.setTitle(dto.getTitle());
        lesson.setDurationHours(dto.getDurationHours());
        lesson.setContent(dto.getContent());
        
        if (dto.getCourseId() != null) {
            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khóa học với ID: " + dto.getCourseId()));
            lesson.setCourse(course);
        }
        
        return lesson;
    }

    private void updateLessonFromDTO(Lesson lesson, LessonDTO dto) {
        lesson.setTitle(dto.getTitle());
        lesson.setDurationHours(dto.getDurationHours());
        lesson.setContent(dto.getContent());
        
        if (dto.getCourseId() != null && (lesson.getCourse() == null || !lesson.getCourse().getId().equals(dto.getCourseId()))) {
            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khóa học với ID: " + dto.getCourseId()));
            lesson.setCourse(course);
        }
    }
} 