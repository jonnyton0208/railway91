package Service;

import DTOs.CourseDTO;
import Entity.Category;
import Entity.Course;
import Repository.CategoryRepository;
import Repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    public Page<CourseDTO> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khóa học với ID: " + id));
        return convertToDTO(course);
    }

    @Transactional
    public CourseDTO createCourse(CourseDTO courseDTO) {
        try {
            logger.info("Bắt đầu tạo khóa học mới: {}", courseDTO);
            
            Category category = categoryRepository.findById(courseDTO.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy danh mục với ID: " + courseDTO.getCategoryId()));
            logger.info("Đã tìm thấy category: {}", category.getName());
            
            Course course = convertToEntity(courseDTO);
            logger.info("Đã chuyển đổi DTO thành entity");
            
            Course savedCourse = courseRepository.save(course);
            logger.info("Đã lưu khóa học thành công với ID: {}", savedCourse.getId());
            
            return convertToDTO(savedCourse);
        } catch (Exception e) {
            logger.error("Lỗi khi tạo khóa học: {}", courseDTO, e);
            throw e;
        }
    }

    @Transactional
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khóa học với ID: " + id));

        updateCourseFromDTO(existingCourse, courseDTO);
        Course updatedCourse = courseRepository.save(existingCourse);
        return convertToDTO(updatedCourse);
    }

    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("Không tìm thấy khóa học với ID: " + id);
        }
        courseRepository.deleteById(id);
    }

    public Page<CourseDTO> searchCourses(String keyword, Pageable pageable) {
        return courseRepository.searchByKeyword(keyword, pageable)
                .map(this::convertToDTO);
    }

    public Page<CourseDTO> getCoursesByCategory(Long categoryId, Pageable pageable) {
        return courseRepository.findByCategoryId(categoryId, pageable)
                .map(this::convertToDTO);
    }

    private CourseDTO convertToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setNumberOfSessions(course.getNumberOfSessions());
        dto.setTotalHours(course.getTotalHours());
        dto.setDescription(course.getDescription());
        dto.setCategoryId(course.getCategory() != null ? course.getCategory().getId() : null);
        return dto;
    }

    private Course convertToEntity(CourseDTO dto) {
        Course course = new Course();
        course.setId(dto.getId());
        course.setName(dto.getName());
        course.setNumberOfSessions(dto.getNumberOfSessions());
        course.setTotalHours(dto.getTotalHours());
        course.setDescription(dto.getDescription());
        
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy danh mục với ID: " + dto.getCategoryId()));
            course.setCategory(category);
        }
        
        return course;
    }

    private void updateCourseFromDTO(Course course, CourseDTO dto) {
        course.setName(dto.getName());
        course.setNumberOfSessions(dto.getNumberOfSessions());
        course.setTotalHours(dto.getTotalHours());
        course.setDescription(dto.getDescription());
        
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy danh mục với ID: " + dto.getCategoryId()));
            course.setCategory(category);
        }
    }
} 