package student.currency.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import student.currency.models.Redemption;
import java.util.List;

public interface RedemptionRepository extends JpaRepository<Redemption, Long> {
  List<Redemption> findByStudentIdOrderByRedeemedAtDesc(Long studentId);

  List<Redemption> findByAdvantageIdOrderByRedeemedAtDesc(Long advantageId);
}