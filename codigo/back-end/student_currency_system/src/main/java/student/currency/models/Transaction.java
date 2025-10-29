package student.currency.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "professor_id", nullable = false)
  private Professor professor;

  @ManyToOne
  @JoinColumn(name = "student_id", nullable = false)
  private Student student;

  @Column(nullable = false)
  private Integer amount;

  @Column(nullable = false)
  private String reason;

  @Column(nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
}