package student.currency.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Professor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String login;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String cpf;

  @Column(nullable = false)
  private String department;

  @Column(nullable = false)
  private Integer coins = 0;

  @ManyToOne
  @JoinColumn(name = "instituition_id", nullable = false)
  private Instituition instituition;

  @Column(nullable = false)
  private String role = "PROFESSOR";

  @Column(nullable = false)
  private String name;
}
