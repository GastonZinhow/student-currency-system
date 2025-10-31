package student.currency.dtos.authrequest;

import lombok.Data;

@Data
public class ProfessorLoginResponseDTO {
  private String message;
  private String login;
  private String role;
  private Long professorId;
  private String name;
  private String department;
  private Integer coins;
}