package student.currency.dtos.authrequest;

import lombok.Data;

@Data
public class AuthRequestDTO {
  private String login;
  private String password;
}