package student.currency.dtos.transaction;

import lombok.Data;

@Data
public class TransactionRequestDTO {
  private Long professorId;
  private Long studentId;
  private Integer amount;
  private String reason;
}