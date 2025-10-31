package student.currency.mappers;

import org.springframework.stereotype.Component;

import student.currency.dtos.advantage.AdvantageRequestDTO;
import student.currency.dtos.advantage.AdvantageResponseDTO;
import student.currency.models.Advantage;
import student.currency.models.Company;

@Component
public class AdvantageMapper {

    private final CompanyMapper companyMapper;

    public AdvantageMapper(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    public Advantage toEntity(AdvantageRequestDTO dto, Company company) {
        Advantage advantage = new Advantage();
        advantage.setDescription(dto.getDescription());
        advantage.setCost(dto.getCost());
        advantage.setPicture(dto.getPicture());
        advantage.setCompany(company);
        return advantage;
    }

    public AdvantageResponseDTO toResponseDTO(Advantage advantage) {
        AdvantageResponseDTO dto = new AdvantageResponseDTO();
        dto.setId(advantage.getId());
        dto.setDescription(advantage.getDescription());
        dto.setCost(advantage.getCost());
        dto.setPicture(advantage.getPicture());
        if (advantage.getCompany() != null) {
            dto.setCompany(companyMapper.toResponseDTO(advantage.getCompany()));
        }
        return dto;
    }

    public void updateEntity(AdvantageRequestDTO dto, Advantage advantage, Company company) {
        advantage.setDescription(dto.getDescription());
        advantage.setCost(dto.getCost());
        advantage.setPicture(dto.getPicture());
        advantage.setCompany(company);
    }
}