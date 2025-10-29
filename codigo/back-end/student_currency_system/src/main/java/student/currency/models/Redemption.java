package student.currency.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Redemption {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "student_id", nullable = false)
  private Student student;

  @ManyToOne
  @JoinColumn(name = "advantage_id", nullable = false)
  private Advantage advantage;

  @Column(nullable = false, unique = true)
  private String generatedCode;

  @Column(nullable = false)
  private LocalDateTime redeemedAt = LocalDateTime.now();

  @Column(nullable = false)
  private String status;
}