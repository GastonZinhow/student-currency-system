package student.currency.dtos.company;

import lombok.Data;

@Data
public class CompanyRequestDTO {
  private String login;
  private String password;
  private String name;
  private String email;
}