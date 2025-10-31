package student.currency.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import student.currency.dtos.transaction.TransactionResponseDTO;
import student.currency.exceptions.InsufficientBalanceException;
import student.currency.exceptions.ResourceNotFoundException;
import student.currency.mappers.TransactionMapper;
import student.currency.models.Professor;
import student.currency.models.Student;
import student.currency.models.Transaction;
import student.currency.repositories.ProfessorRepository;
import student.currency.repositories.StudentRepository;
import student.currency.repositories.TransactionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TransactionMapper transactionMapper;

    public List<TransactionResponseDTO> getTransactionsByStudent(Long studentId) {
        return transactionRepository.findByStudentIdOrderByCreatedAtDesc(studentId).stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> getTransactionsByProfessor(Long professorId) {
        return transactionRepository.findByProfessorIdOrderByCreatedAtDesc(professorId).stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TransactionResponseDTO sendCoins(Long professorId, Long studentId, Integer amount, String reason) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado com ID: " + professorId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com ID: " + studentId));

        if (professor.getCoins() < amount) {
            throw new InsufficientBalanceException("Saldo insuficiente. Você possui " + professor.getCoins()
                    + " moedas e está tentando enviar " + amount);
        }

        professor.setCoins(professor.getCoins() - amount);
        student.setCoins(student.getCoins() + amount);

        professorRepository.save(professor);
        studentRepository.save(student);

        Transaction transaction = new Transaction();
        transaction.setProfessor(professor);
        transaction.setStudent(student);
        transaction.setAmount(amount);
        transaction.setReason(reason);

        Transaction savedTransaction = transactionRepository.save(transaction);

        emailService.sendEmail(
                student.getEmail(),
                "Você recebeu moedas!",
                "Olá " + student.getName() + ",\n\n" +
                        "Você recebeu " + amount + " moedas do professor " + professor.getName() + "\n" +
                        "Motivo: " + reason + "\n\n" +
                        "Seu novo saldo é: " + student.getCoins() + " moedas.");

        return transactionMapper.toResponseDTO(savedTransaction);
    }
}