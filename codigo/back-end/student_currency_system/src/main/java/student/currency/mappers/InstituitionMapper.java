package student.currency.mappers;

import org.springframework.stereotype.Component;

import student.currency.dtos.instituition.InstituitionRequestDTO;
import student.currency.dtos.instituition.InstituitionResponseDTO;
import student.currency.models.Instituition;

@Component
public class InstituitionMapper {

    public Instituition toEntity(InstituitionRequestDTO dto) {
        Instituition instituition = new Instituition();
        instituition.setName(dto.getName());
        return instituition;
    }

    public InstituitionResponseDTO toResponseDTO(Instituition instituition) {
        InstituitionResponseDTO dto = new InstituitionResponseDTO();
        dto.setId(instituition.getId());
        dto.setName(instituition.getName());
        return dto;
    }

    public void updateEntity(InstituitionRequestDTO dto, Instituition instituition) {
        instituition.setName(dto.getName());
    }
}