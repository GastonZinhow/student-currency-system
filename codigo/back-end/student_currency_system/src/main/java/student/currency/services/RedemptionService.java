package student.currency.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import student.currency.models.Redemption;
import student.currency.models.Student;
import student.currency.models.Advantage;
import student.currency.repositories.RedemptionRepository;
import student.currency.repositories.StudentRepository;
import student.currency.repositories.AdvantageRepository;

import java.util.List;
import java.util.UUID;

@Service
public class RedemptionService {

    @Autowired
    private RedemptionRepository redemptionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdvantageRepository advantageRepository;

    @Autowired
    private EmailService emailService;

    public List<Redemption> getRedemptionsByStudent(Long studentId) {
        return redemptionRepository.findByStudentIdOrderByRedeemedAtDesc(studentId);
    }

    public Redemption redeemAdvantage(Long studentId, Long advantageId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Advantage advantage = advantageRepository.findById(advantageId)
                .orElseThrow(() -> new RuntimeException("Advantage not found"));

        if (student.getCoins() < advantage.getCost()) {
            throw new RuntimeException("Insufficient coins");
        }

        student.setCoins(student.getCoins() - advantage.getCost());
        studentRepository.save(student);

        Redemption redemption = new Redemption();
        redemption.setStudent(student);
        redemption.setAdvantage(advantage);
        String code = UUID.randomUUID().toString();
        redemption.setGeneratedCode(code);
        redemption.setStatus("REDEEMED");

        Redemption savedRedemption = redemptionRepository.save(redemption);

        emailService.sendEmail(
                student.getEmail(),
                "Cupom de resgate de vantagem",
                "Olá " + student.getName() + ",\n\nVocê resgatou a vantagem: " + advantage.getDescription() +
                        ".\nCódigo do cupom: " + code +
                        "\nApresente este código para utilizar sua vantagem.\n\nSeu novo saldo é: " + student.getCoins()
                        + " moedas.");

        emailService.sendEmail(
                advantage.getCompany().getEmail(),
                "Novo resgate de vantagem",
                "O aluno " + student.getName() + " (" + student.getEmail() + ") resgatou a vantagem '" +
                        advantage.getDescription() + "'.\nCódigo do cupom: " + code +
                        "\nVerifique o código para liberar o benefício.");

        return savedRedemption;
    }
}