package student.currency.dtos.transaction;

import lombok.Data;
import student.currency.dtos.professor.ProfessorResponseDTO;
import student.currency.dtos.student.StudentResponseDTO;

import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {
  private Long id;
  private ProfessorResponseDTO professor;
  private StudentResponseDTO student;
  private Integer amount;
  private String reason;
  private LocalDateTime createdAt;
}