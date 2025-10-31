package student.currency.dtos.advantage;

import lombok.Data;

@Data
public class AdvantageRequestDTO {
  private String description;
  private Integer cost;
  private String picture;
  private Long companyId;
}