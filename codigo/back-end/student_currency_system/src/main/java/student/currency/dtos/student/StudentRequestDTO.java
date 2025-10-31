package student.currency.dtos.student;

import lombok.Data;

@Data
public class StudentRequestDTO {
  private String login;
  private String password;
  private String name;
  private String email;
  private String cpf;
  private String rg;
  private String address;
  private String course;
  private Long instituitionId;
}