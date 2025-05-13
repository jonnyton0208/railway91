package DTOs;

import Entity.CertificateType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CertificateDTO {
    private Long id;
    private CertificateType type;
    private Long accountId;
    private Long courseId;
    private LocalDateTime issuedDate;
    private String certificateNumber;
} 