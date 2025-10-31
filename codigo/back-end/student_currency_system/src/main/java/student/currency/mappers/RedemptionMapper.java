package student.currency.mappers;

import org.springframework.stereotype.Component;

import student.currency.dtos.redemption.RedemptionResponseDTO;
import student.currency.models.Redemption;

@Component
public class RedemptionMapper {

    private final StudentMapper studentMapper;
    private final AdvantageMapper advantageMapper;

    public RedemptionMapper(StudentMapper studentMapper, AdvantageMapper advantageMapper) {
        this.studentMapper = studentMapper;
        this.advantageMapper = advantageMapper;
    }

    public RedemptionResponseDTO toResponseDTO(Redemption redemption) {
        RedemptionResponseDTO dto = new RedemptionResponseDTO();
        dto.setId(redemption.getId());
        dto.setGeneratedCode(redemption.getGeneratedCode());
        dto.setRedeemedAt(redemption.getRedeemedAt());
        dto.setStatus(redemption.getStatus());
        if (redemption.getStudent() != null) {
            dto.setStudent(studentMapper.toResponseDTO(redemption.getStudent()));
        }
        if (redemption.getAdvantage() != null) {
            dto.setAdvantage(advantageMapper.toResponseDTO(redemption.getAdvantage()));
        }
        return dto;
    }
}