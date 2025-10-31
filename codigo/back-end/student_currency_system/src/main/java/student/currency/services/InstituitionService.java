package student.currency.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import student.currency.dtos.instituition.InstituitionRequestDTO;
import student.currency.dtos.instituition.InstituitionResponseDTO;
import student.currency.exceptions.ResourceNotFoundException;
import student.currency.exceptions.ValidationException;
import student.currency.mappers.InstituitionMapper;
import student.currency.models.Instituition;
import student.currency.repositories.InstituitionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstituitionService {

    @Autowired
    private InstituitionRepository instituitionRepository;

    @Autowired
    private InstituitionMapper instituitionMapper;

    public List<InstituitionResponseDTO> findAll() {
        return instituitionRepository.findAll().stream()
                .map(instituitionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public InstituitionResponseDTO findById(Long id) {
        Instituition instituition = instituitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada com ID: " + id));
        return instituitionMapper.toResponseDTO(instituition);
    }

    public InstituitionResponseDTO save(InstituitionRequestDTO dto) {
        if (instituitionRepository.existsByName(dto.getName())) {
            throw new ValidationException("Instituição já cadastrada com o nome: " + dto.getName());
        }

        Instituition instituition = instituitionMapper.toEntity(dto);
        Instituition savedInstituition = instituitionRepository.save(instituition);
        return instituitionMapper.toResponseDTO(savedInstituition);
    }

    public void delete(Long id) {
        if (!instituitionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Instituição não encontrada com ID: " + id);
        }
        instituitionRepository.deleteById(id);
    }

    public InstituitionResponseDTO update(Long id, InstituitionRequestDTO dto) {
        Instituition instituition = instituitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada com ID: " + id));

        instituitionMapper.updateEntity(dto, instituition);
        Instituition updatedInstituition = instituitionRepository.save(instituition);
        return instituitionMapper.toResponseDTO(updatedInstituition);
    }
}