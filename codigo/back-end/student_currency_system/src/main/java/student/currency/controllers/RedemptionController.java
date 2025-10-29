package student.currency.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import student.currency.models.Redemption;
import student.currency.services.RedemptionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/redemptions")
@CrossOrigin(origins = "http://localhost:3000")
public class RedemptionController {

    @Autowired
    private RedemptionService redemptionService;

    @GetMapping("/student/{studentId}")
    public List<Redemption> getStudentRedemptions(@PathVariable Long studentId) {
        return redemptionService.getRedemptionsByStudent(studentId);
    }

    @PostMapping("/redeem")
    public ResponseEntity<Redemption> redeemAdvantage(@RequestBody Map<String, Object> payload) {
        Long studentId = Long.valueOf(payload.get("studentId").toString());
        Long advantageId = Long.valueOf(payload.get("advantageId").toString());

        Redemption redemption = redemptionService.redeemAdvantage(studentId, advantageId);
        return ResponseEntity.ok(redemption);
    }
}