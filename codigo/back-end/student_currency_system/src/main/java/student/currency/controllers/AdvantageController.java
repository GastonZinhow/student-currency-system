package student.currency.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import student.currency.models.Advantage;
import student.currency.services.AdvantageService;

import java.util.List;

@RestController
@RequestMapping("/advantages")
@CrossOrigin(origins = "http://localhost:3000")
public class AdvantageController {

    @Autowired
    private AdvantageService advantageService;

    @GetMapping
    public List<Advantage> getAllAdvantages() {
        return advantageService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Advantage> getAdvantageById(@PathVariable Long id) {
        return advantageService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Advantage createAdvantage(@RequestBody Advantage advantage) {
        return advantageService.save(advantage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Advantage> updateAdvantage(@PathVariable Long id, @RequestBody Advantage advantage) {
        if (!advantageService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(advantageService.update(id, advantage));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvantage(@PathVariable Long id) {
        if (!advantageService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        advantageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/company/{companyId}")
    public List<Advantage> getAdvantagesByCompany(@PathVariable Long companyId) {
        return advantageService.findByCompanyId(companyId);
    }
}