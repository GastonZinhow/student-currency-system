package student.currency.mappers;

import org.springframework.stereotype.Component;

import student.currency.dtos.transaction.TransactionResponseDTO;
import student.currency.models.Transaction;

@Component
public class TransactionMapper {

    private final ProfessorMapper professorMapper;
    private final StudentMapper studentMapper;

    public TransactionMapper(ProfessorMapper professorMapper, StudentMapper studentMapper) {
        this.professorMapper = professorMapper;
        this.studentMapper = studentMapper;
    }

    public TransactionResponseDTO toResponseDTO(Transaction transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setReason(transaction.getReason());
        dto.setCreatedAt(transaction.getCreatedAt());
        if (transaction.getProfessor() != null) {
            dto.setProfessor(professorMapper.toResponseDTO(transaction.getProfessor()));
        }
        if (transaction.getStudent() != null) {
            dto.setStudent(studentMapper.toResponseDTO(transaction.getStudent()));
        }
        return dto;
    }
}
