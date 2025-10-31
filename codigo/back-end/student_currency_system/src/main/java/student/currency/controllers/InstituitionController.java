package student.currency.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import student.currency.dtos.instituition.InstituitionRequestDTO;
import student.currency.dtos.instituition.InstituitionResponseDTO;
import student.currency.services.InstituitionService;

import java.util.List;

@RestController
@RequestMapping("/instituitions")
@CrossOrigin(origins = "http://localhost:3000")
public class InstituitionController {

    @Autowired
    private InstituitionService instituitionService;

    @GetMapping
    public ResponseEntity<List<InstituitionResponseDTO>> getAllInstituitions() {
        return ResponseEntity.ok(instituitionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstituitionResponseDTO> getInstituitionById(@PathVariable Long id) {
        return ResponseEntity.ok(instituitionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<InstituitionResponseDTO> createInstituition(@RequestBody InstituitionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(instituitionService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstituitionResponseDTO> updateInstituition(
            @PathVariable Long id,
            @RequestBody InstituitionRequestDTO dto) {
        return ResponseEntity.ok(instituitionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstituition(@PathVariable Long id) {
        instituitionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}