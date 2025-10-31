package student.currency.mappers;

import org.springframework.stereotype.Component;

import student.currency.dtos.company.CompanyRequestDTO;
import student.currency.dtos.company.CompanyResponseDTO;
import student.currency.models.Company;

@Component
public class CompanyMapper {

    public Company toEntity(CompanyRequestDTO dto) {
        Company company = new Company();
        company.setLogin(dto.getLogin());
        company.setPassword(dto.getPassword());
        company.setName(dto.getName());
        company.setEmail(dto.getEmail());
        company.setRole("COMPANY");
        return company;
    }

    public CompanyResponseDTO toResponseDTO(Company company) {
        CompanyResponseDTO dto = new CompanyResponseDTO();
        dto.setId(company.getId());
        dto.setLogin(company.getLogin());
        dto.setName(company.getName());
        dto.setEmail(company.getEmail());
        dto.setRole(company.getRole());
        return dto;
    }

    public void updateEntity(CompanyRequestDTO dto, Company company) {
        company.setLogin(dto.getLogin());
        company.setName(dto.getName());
        company.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            company.setPassword(dto.getPassword());
        }
    }
}