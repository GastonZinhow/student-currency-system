package student.currency.dtos.advantage;

import lombok.Data;

@Data
public class AdvantageRequestDTO {
  private String name;
  private String description;
  private Integer cost;
  private String picture;
  private Integer quantity;
  private Boolean isActive;
  private Long companyId;
}