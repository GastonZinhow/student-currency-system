package student.currency.mappers;

import org.springframework.stereotype.Component;
import student.currency.models.Professor;
import student.currency.dtos.professor.ProfessorRequestDTO;
import student.currency.dtos.professor.ProfessorResponseDTO;
import student.currency.models.Instituition;

@Component
public class ProfessorMapper {

    private final InstituitionMapper instituitionMapper;

    public ProfessorMapper(InstituitionMapper instituitionMapper) {
        this.instituitionMapper = instituitionMapper;
    }

    public Professor toEntity(ProfessorRequestDTO dto, Instituition instituition) {
        Professor professor = new Professor();
        professor.setLogin(dto.getLogin());
        professor.setPassword(dto.getPassword());
        professor.setCpf(dto.getCpf());
        professor.setDepartment(dto.getDepartment());
        professor.setName(dto.getName());
        professor.setInstituition(instituition);
        professor.setRole("PROFESSOR");
        return professor;
    }

    public ProfessorResponseDTO toResponseDTO(Professor professor) {
        ProfessorResponseDTO dto = new ProfessorResponseDTO();
        dto.setId(professor.getId());
        dto.setLogin(professor.getLogin());
        dto.setCpf(professor.getCpf());
        dto.setDepartment(professor.getDepartment());
        dto.setCoins(professor.getCoins());
        dto.setName(professor.getName());
        dto.setRole(professor.getRole());
        if (professor.getInstituition() != null) {
            dto.setInstituition(instituitionMapper.toResponseDTO(professor.getInstituition()));
        }
        return dto;
    }

    public void updateEntity(ProfessorRequestDTO dto, Professor professor, Instituition instituition) {
        professor.setLogin(dto.getLogin());
        professor.setCpf(dto.getCpf());
        professor.setDepartment(dto.getDepartment());
        professor.setName(dto.getName());
        professor.setInstituition(instituition);
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            professor.setPassword(dto.getPassword());
        }
    }
}