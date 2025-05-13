package Repository;

import Entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByAccountId(Long accountId);
    List<Certificate> findByCourseId(Long courseId);
    boolean existsByAccountIdAndCourseId(Long accountId, Long courseId);
} 