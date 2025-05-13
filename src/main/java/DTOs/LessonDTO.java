package DTOs;

import lombok.Data;

@Data
public class LessonDTO {
    private Long id;
    private String title;
    private int durationHours;
    private String content;
    private Long courseId;
} 