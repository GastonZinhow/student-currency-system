package student.currency.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import student.currency.models.Transaction;
import student.currency.services.TransactionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/student/{studentId}")
    public List<Transaction> getStudentTransactions(@PathVariable Long studentId) {
        return transactionService.getTransactionsByStudent(studentId);
    }

    @GetMapping("/professor/{professorId}")
    public List<Transaction> getProfessorTransactions(@PathVariable Long professorId) {
        return transactionService.getTransactionsByProfessor(professorId);
    }

    @PostMapping("/{studentId}")
    public ResponseEntity<Transaction> createTransactions(@PathVariable Long studentId,
            @RequestBody Transaction transacoes) {
        return ResponseEntity.ok(transactionService.addTransaction(studentId, transacoes));
    }

    @PostMapping("/send")
    public ResponseEntity<Transaction> sendCoins(@RequestBody Map<String, Object> payload) {
        Long professorId = Long.valueOf(payload.get("professorId").toString());
        Long studentId = Long.valueOf(payload.get("studentId").toString());
        Integer amount = Integer.valueOf(payload.get("amount").toString());
        String reason = payload.get("reason").toString();

        Transaction transaction = transactionService.sendCoins(professorId, studentId, amount, reason);
        return ResponseEntity.ok(transaction);
    }
}