package student.currency.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Advantage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private Integer cost;

  @Column(nullable = false)
  private String picture;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private Boolean isActive;

  @ManyToOne
  @JoinColumn(name = "company_id", nullable = false)
  private Company company;
}