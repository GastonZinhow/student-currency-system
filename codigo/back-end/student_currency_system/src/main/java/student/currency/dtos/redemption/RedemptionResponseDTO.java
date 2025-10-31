package student.currency.dtos.redemption;

import lombok.Data;
import student.currency.dtos.advantage.AdvantageResponseDTO;
import student.currency.dtos.student.StudentResponseDTO;

import java.time.LocalDateTime;

@Data
public class RedemptionResponseDTO {
  private Long id;
  private StudentResponseDTO student;
  private AdvantageResponseDTO advantage;
  private String generatedCode;
  private LocalDateTime redeemedAt;
  private String status;
}