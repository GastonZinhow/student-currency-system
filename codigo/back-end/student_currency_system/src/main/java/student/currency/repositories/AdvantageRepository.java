package student.currency.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import student.currency.models.Advantage;
import java.util.List;

public interface AdvantageRepository extends JpaRepository<Advantage, Long> {
  List<Advantage> findByCompanyId(Long companyId);
}