package student.currency.dtos.advantage;

import lombok.Data;
import student.currency.dtos.company.CompanyResponseDTO;

@Data
public class AdvantageResponseDTO {
  private Long id;
  private String description;
  private Integer cost;
  private String picture;
  private CompanyResponseDTO company;
}