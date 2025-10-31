package student.currency.dtos.authrequest;

import lombok.Data;

@Data
public class CompanyLoginResponseDTO {
  private String message;
  private String login;
  private String role;
  private Long companyId;
  private String name;
  private String email;
}