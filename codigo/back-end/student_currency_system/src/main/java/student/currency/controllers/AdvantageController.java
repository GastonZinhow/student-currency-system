package student.currency.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import student.currency.dtos.advantage.AdvantageRequestDTO;
import student.currency.dtos.advantage.AdvantageResponseDTO;
import student.currency.services.AdvantageService;

import java.util.List;

@RestController
@RequestMapping("/advantages")
@CrossOrigin(origins = "http://localhost:3000")
public class AdvantageController {

    @Autowired
    private AdvantageService advantageService;

    @GetMapping
    public ResponseEntity<List<AdvantageResponseDTO>> getAllAdvantages() {
        return ResponseEntity.ok(advantageService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvantageResponseDTO> getAdvantageById(@PathVariable Long id) {
        return ResponseEntity.ok(advantageService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AdvantageResponseDTO> createAdvantage(@RequestBody AdvantageRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(advantageService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdvantageResponseDTO> updateAdvantage(
            @PathVariable Long id,
            @RequestBody AdvantageRequestDTO dto) {
        return ResponseEntity.ok(advantageService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvantage(@PathVariable Long id) {
        advantageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<AdvantageResponseDTO>> getAdvantagesByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(advantageService.findByCompanyId(companyId));
    }
}