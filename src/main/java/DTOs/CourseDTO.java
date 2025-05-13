package DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CourseDTO {
    private Long id;
    
    @NotBlank(message = "Tên khóa học không được để trống")
    private String name;
    
    @Min(value = 1, message = "Số buổi học phải lớn hơn 0")
    private int numberOfSessions;
    
    @Min(value = 1, message = "Tổng số giờ phải lớn hơn 0")
    private int totalHours;
    
    @NotBlank(message = "Mô tả không được để trống")
    private String description;
    
    @NotNull(message = "ID danh mục không được để trống")
    private Long categoryId;
} 