package student.currency.dtos.redemption;

import lombok.Data;

@Data
public class RedemptionRequestDTO {
  private Long studentId;
  private Long advantageId;
}