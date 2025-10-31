package student.currency.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import student.currency.dtos.professor.ProfessorRequestDTO;
import student.currency.dtos.professor.ProfessorResponseDTO;
import student.currency.services.ProfessorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/professors")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @GetMapping
    public ResponseEntity<List<ProfessorResponseDTO>> getAllProfessors() {
        return ResponseEntity.ok(professorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> getProfessorById(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> createProfessor(@RequestBody ProfessorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> updateProfessor(
            @PathVariable Long id,
            @RequestBody ProfessorRequestDTO dto) {
        return ResponseEntity.ok(professorService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        professorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/add-semester-coins")
    public ResponseEntity<ProfessorResponseDTO> addSemesterCoins(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.addSemesterCoins(id));
    }

    @PostMapping("/add-semester-coins-all")
    public ResponseEntity<Map<String, String>> addSemesterCoinsToAll() {
        professorService.addSemesterCoinsToAll();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Moedas semestrais adicionadas para todos os professores");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-semester-coins-eligible")
    public ResponseEntity<Map<String, Object>> addSemesterCoinsToEligible() {
        int count = professorService.addSemesterCoinsToEligible();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Recarga semestral processada");
        response.put("professorsUpdated", count);
        return ResponseEntity.ok(response);
    }
}
