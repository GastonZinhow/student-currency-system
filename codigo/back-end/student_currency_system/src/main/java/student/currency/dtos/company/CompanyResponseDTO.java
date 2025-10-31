package student.currency.dtos.company;

import lombok.Data;

@Data
public class CompanyResponseDTO {
  private Long id;
  private String login;
  private String name;
  private String email;
  private String role;
}