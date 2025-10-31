package student.currency.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import student.currency.dtos.redemption.RedemptionRequestDTO;
import student.currency.dtos.redemption.RedemptionResponseDTO;
import student.currency.services.RedemptionService;

import java.util.List;

@RestController
@RequestMapping("/redemptions")
@CrossOrigin(origins = "http://localhost:3000")
public class RedemptionController {

    @Autowired
    private RedemptionService redemptionService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<RedemptionResponseDTO>> getStudentRedemptions(@PathVariable Long studentId) {
        return ResponseEntity.ok(redemptionService.getRedemptionsByStudent(studentId));
    }

    @PostMapping("/redeem")
    public ResponseEntity<RedemptionResponseDTO> redeemAdvantage(@RequestBody RedemptionRequestDTO dto) {
        RedemptionResponseDTO redemption = redemptionService.redeemAdvantage(dto.getStudentId(), dto.getAdvantageId());
        return ResponseEntity.status(HttpStatus.CREATED).body(redemption);
    }
}