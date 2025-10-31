package student.currency.dtos.professor;

import lombok.Data;
import student.currency.dtos.instituition.InstituitionResponseDTO;

@Data
public class ProfessorResponseDTO {
  private Long id;
  private String login;
  private String cpf;
  private String department;
  private Integer coins;
  private InstituitionResponseDTO instituition;
  private String role;
  private String name;
}