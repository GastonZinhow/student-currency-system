package student.currency.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import student.currency.dtos.transaction.TransactionRequestDTO;
import student.currency.dtos.transaction.TransactionResponseDTO;
import student.currency.services.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TransactionResponseDTO>> getStudentTransactions(@PathVariable Long studentId) {
        return ResponseEntity.ok(transactionService.getTransactionsByStudent(studentId));
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<TransactionResponseDTO>> getProfessorTransactions(@PathVariable Long professorId) {
        return ResponseEntity.ok(transactionService.getTransactionsByProfessor(professorId));
    }

    @PostMapping("/send")
    public ResponseEntity<TransactionResponseDTO> sendCoins(@RequestBody TransactionRequestDTO dto) {
        TransactionResponseDTO transaction = transactionService.sendCoins(
                dto.getProfessorId(),
                dto.getStudentId(),
                dto.getAmount(),
                dto.getReason());
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }
}