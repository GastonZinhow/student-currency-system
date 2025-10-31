package student.currency.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import student.currency.dtos.company.CompanyRequestDTO;
import student.currency.dtos.company.CompanyResponseDTO;
import student.currency.exceptions.ResourceNotFoundException;
import student.currency.exceptions.ValidationException;
import student.currency.mappers.CompanyMapper;
import student.currency.models.Company;
import student.currency.repositories.CompanyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<CompanyResponseDTO> findAll() {
        return companyRepository.findAll().stream()
                .map(companyMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CompanyResponseDTO findById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com ID: " + id));
        return companyMapper.toResponseDTO(company);
    }

    public CompanyResponseDTO save(CompanyRequestDTO dto) {
        if (companyRepository.existsByEmail(dto.getEmail())) {
            throw new ValidationException("Email já cadastrado: " + dto.getEmail());
        }

        Company company = companyMapper.toEntity(dto);
        company.setPassword(passwordEncoder.encode(dto.getPassword()));
        Company savedCompany = companyRepository.save(company);
        return companyMapper.toResponseDTO(savedCompany);
    }

    public void delete(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empresa não encontrada com ID: " + id);
        }
        companyRepository.deleteById(id);
    }

    public CompanyResponseDTO update(Long id, CompanyRequestDTO dto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com ID: " + id));

        companyMapper.updateEntity(dto, company);
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            company.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Company updatedCompany = companyRepository.save(company);
        return companyMapper.toResponseDTO(updatedCompany);
    }

    public Company findByLogin(String login) {
        return companyRepository.findByLogin(login);
    }
}