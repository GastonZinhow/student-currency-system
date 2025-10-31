package student.currency.dtos.student;

import lombok.Data;
import student.currency.dtos.instituition.InstituitionResponseDTO;

@Data
public class StudentResponseDTO {
  private Long id;
  private String login;
  private String name;
  private String email;
  private String cpf;
  private String rg;
  private String address;
  private String course;
  private Integer coins;
  private InstituitionResponseDTO instituition;
  private String role;
}