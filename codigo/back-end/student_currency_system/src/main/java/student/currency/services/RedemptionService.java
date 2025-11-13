package student.currency.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import student.currency.dtos.redemption.RedemptionResponseDTO;
import student.currency.exceptions.InsufficientBalanceException;
import student.currency.exceptions.ResourceNotFoundException;
import student.currency.mappers.RedemptionMapper;
import student.currency.models.Advantage;
import student.currency.models.Redemption;
import student.currency.models.Student;
import student.currency.repositories.AdvantageRepository;
import student.currency.repositories.RedemptionRepository;
import student.currency.repositories.StudentRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        @Autowired
        private RedemptionMapper redemptionMapper;

        public List<RedemptionResponseDTO> getRedemptionsByStudent(Long studentId) {
                return redemptionRepository.findByStudentIdOrderByRedeemedAtDesc(studentId).stream()
                                .map(redemptionMapper::toResponseDTO)
                                .collect(Collectors.toList());
        }

        public RedemptionResponseDTO redeemAdvantage(Long studentId, Long advantageId) {
                Student student = studentRepository.findById(studentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Aluno não encontrado com ID: " + studentId));

                Advantage advantage = advantageRepository.findById(advantageId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Vantagem não encontrada com ID: " + advantageId));

                if (student.getCoins() < advantage.getCost()) {
                        throw new InsufficientBalanceException("Saldo insuficiente. Você possui " + student.getCoins()
                                        + " moedas e precisa de " + advantage.getCost());
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

                String html = """
                                <html>
                                  <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f2f2f2;">
                                    <div style="max-width: 600px; margin: 0 auto; background: #ffffff;">
                                      <div style="width: 600px; height: 80px; background-color: #000;"></div>
                                      <div style="padding: 20px; text-align: center; color: #333;">
                                        <h2>Olá, %s!</h2>
                                        <p>Você resgatou a vantagem: <strong>%s</strong></p>
                                        <p><b>Código do cupom:</b> %s</p>
                                        <p>Seu novo saldo é: <b>%d moedas</b>.</p>
                                        <br>
                                        <img src="%s" alt="Logo" width="200" style="margin-top: 10px;">
                                        <p>IMAGEM: %s</p>

                                        <br><br>
                                        <small>Equipe Academic Moeda</small>
                                      </div>
                                      <div style="width: 600px; height: 60px; background-color: #000;"></div>
                                    </div>
                                  </body>
                                </html>"""
                                .formatted(
                                                escapePercent(student.getName()),
                                                escapePercent(advantage.getDescription()),
                                                escapePercent(code),
                                                student.getCoins(),
                                                escapePercent(advantage.getPicture()),
                                                escapePercent(advantage.getPicture()));

                emailService.sendEmail(
                                student.getEmail(),
                                "Cupom de resgate de vantagem",
                                html);

                String htmlCompany = """
                                <html>
                                  <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f2f2f2;">
                                    <div style="max-width: 600px; margin: 0 auto; background: #ffffff;">
                                      <div style="width: 600px; height: 80px; background-color: #000;"></div>
                                      <div style="padding: 20px; text-align: center; color: #333;">
                                        <h2>Olá, %s!</h2>
                                        <p>O aluno: <strong>%s</strong></p>
                                        <p>Resgatou a vantagem: <strong>%s</strong></p>
                                        <p><b>Código do cupom:</b> %s</p>
                                        <br>
                                        <img src="%s" alt="Logo" width="200" style="margin-top: 10px;">
                                        <p>IMAGEM: %s</p>

                                        <br><br>
                                        <small>Equipe Academic Moeda</small>
                                      </div>
                                      <div style="width: 600px; height: 60px; background-color: #000;"></div>
                                    </div>
                                  </body>
                                </html>"""
                                .formatted(
                                                escapePercent(advantage.getCompany().getName()),
                                                escapePercent(student.getName()),
                                                escapePercent(advantage.getDescription()),
                                                escapePercent(code),
                                                escapePercent(advantage.getPicture()),
                                                escapePercent(advantage.getPicture()));

                emailService.sendEmail(advantage.getCompany().getEmail(), "Sua vantagem foi resgatada!", htmlCompany);

                return redemptionMapper.toResponseDTO(savedRedemption);
        }

        private static String escapePercent(String value) {
                if (value == null)
                        return "";
                return value.replace("%", "%%");
        }
}