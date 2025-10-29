package student.currency.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import student.currency.models.Transaction;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  List<Transaction> findByStudentIdOrderByCreatedAtDesc(Long studentId);

  List<Transaction> findByProfessorIdOrderByCreatedAtDesc(Long professorId);
}