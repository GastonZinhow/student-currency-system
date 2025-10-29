package student.currency.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import student.currency.models.Transaction;
import student.currency.models.Professor;
import student.currency.models.Student;
import student.currency.repositories.TransactionRepository;
import student.currency.repositories.ProfessorRepository;
import student.currency.repositories.StudentRepository;

import java.util.List;

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

    public List<Transaction> getTransactionsByStudent(Long studentId) {
        return transactionRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
    }

    public List<Transaction> getTransactionsByProfessor(Long professorId) {
        return transactionRepository.findByProfessorIdOrderByCreatedAtDesc(professorId);
    }

    public Transaction addTransaction(Long studentId, Transaction transacoes) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        int newBalance = student.getCoins();
        if ("GAIN".equals(transacoes.getReason())) {
            newBalance += transacoes.getAmount();
        } else if ("SPEND".equals(transacoes.getReason())) {
            if (student.getCoins() < transacoes.getAmount())
                throw new RuntimeException("Saldo insuficiente");
            newBalance -= transacoes.getAmount();
        }
        student.setCoins(newBalance);
        studentRepository.save(student);

        transacoes.setStudent(student);
        return transactionRepository.save(transacoes);
    }

    public Transaction sendCoins(Long professorId, Long studentId, Integer amount, String reason) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (professor.getCoins() < amount) {
            throw new RuntimeException("Insufficient coins");
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
                "Olá " + student.getName() + ",\n\nVocê recebeu " + amount + " moedas do professor "
                        + professor.getName() +
                        " pelo motivo: " + reason + ".\n\nSeu novo saldo é: " + student.getCoins() + " moedas.");

        return savedTransaction;
    }
}