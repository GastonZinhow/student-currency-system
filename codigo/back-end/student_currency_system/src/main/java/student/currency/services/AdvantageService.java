package student.currency.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import student.currency.dtos.advantage.AdvantageRequestDTO;
import student.currency.dtos.advantage.AdvantageResponseDTO;
import student.currency.exceptions.ResourceNotFoundException;
import student.currency.mappers.AdvantageMapper;
import student.currency.models.Advantage;
import student.currency.models.Company;
import student.currency.repositories.AdvantageRepository;
import student.currency.repositories.CompanyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdvantageService {

    @Autowired
    private AdvantageRepository advantageRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AdvantageMapper advantageMapper;

    public List<AdvantageResponseDTO> findAll() {
        return advantageRepository.findAll().stream()
                .map(advantageMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public AdvantageResponseDTO findById(Long id) {
        Advantage advantage = advantageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vantagem não encontrada com ID: " + id));
        return advantageMapper.toResponseDTO(advantage);
    }

    public AdvantageResponseDTO save(AdvantageRequestDTO dto) {
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Empresa não encontrada com ID: " + dto.getCompanyId()));

        Advantage advantage = advantageMapper.toEntity(dto, company);
        Advantage savedAdvantage = advantageRepository.save(advantage);
        return advantageMapper.toResponseDTO(savedAdvantage);
    }

    public void delete(Long id) {
        if (!advantageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vantagem não encontrada com ID: " + id);
        }
        advantageRepository.deleteById(id);
    }

    public AdvantageResponseDTO update(Long id, AdvantageRequestDTO dto) {
        Advantage advantage = advantageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vantagem não encontrada com ID: " + id));

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Empresa não encontrada com ID: " + dto.getCompanyId()));

        advantageMapper.updateEntity(dto, advantage, company);
        Advantage updatedAdvantage = advantageRepository.save(advantage);
        return advantageMapper.toResponseDTO(updatedAdvantage);
    }

    public List<AdvantageResponseDTO> findByCompanyId(Long companyId) {
        return advantageRepository.findByCompanyId(companyId).stream()
                .map(advantageMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}