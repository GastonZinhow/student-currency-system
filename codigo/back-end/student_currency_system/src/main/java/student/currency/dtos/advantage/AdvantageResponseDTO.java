package student.currency.dtos.advantage;

import lombok.Data;
import student.currency.dtos.company.CompanyResponseDTO;

@Data
public class AdvantageResponseDTO {
  private Long id;
  private String name;
  private String description;
  private Integer cost;
  private String picture;
  private Integer quantity;
  private Boolean isActive;
  private CompanyResponseDTO company;
}