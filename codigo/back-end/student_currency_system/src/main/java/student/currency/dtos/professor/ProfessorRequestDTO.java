package student.currency.dtos.professor;

import lombok.Data;

@Data
public class ProfessorRequestDTO {
  private String login;
  private String password;
  private String cpf;
  private String department;
  private Long instituitionId;
  private String name;
}