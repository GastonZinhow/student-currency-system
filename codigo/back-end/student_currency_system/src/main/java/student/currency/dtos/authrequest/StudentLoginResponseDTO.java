package student.currency.dtos.authrequest;

import lombok.Data;

@Data
public class StudentLoginResponseDTO {
  private String message;
  private String login;
  private String role;
  private Long studentId;
  private String name;
  private String email;
  private Integer coins;
}