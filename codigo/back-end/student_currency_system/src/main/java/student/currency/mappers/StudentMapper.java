package student.currency.mappers;

import org.springframework.stereotype.Component;
import student.currency.models.Student;
import student.currency.dtos.student.StudentRequestDTO;
import student.currency.dtos.student.StudentResponseDTO;
import student.currency.models.Instituition;

@Component
public class StudentMapper {

    private final InstituitionMapper instituitionMapper;

    public StudentMapper(InstituitionMapper instituitionMapper) {
        this.instituitionMapper = instituitionMapper;
    }

    public Student toEntity(StudentRequestDTO dto, Instituition instituition) {
        Student student = new Student();
        student.setLogin(dto.getLogin());
        student.setPassword(dto.getPassword());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setCpf(dto.getCpf());
        student.setRg(dto.getRg());
        student.setAddress(dto.getAddress());
        student.setCourse(dto.getCourse());
        student.setInstituition(instituition);
        student.setCoins(0);
        student.setRole("STUDENT");
        return student;
    }

    public StudentResponseDTO toResponseDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setLogin(student.getLogin());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setCpf(student.getCpf());
        dto.setRg(student.getRg());
        dto.setAddress(student.getAddress());
        dto.setCourse(student.getCourse());
        dto.setCoins(student.getCoins());
        dto.setRole(student.getRole());
        if (student.getInstituition() != null) {
            dto.setInstituition(instituitionMapper.toResponseDTO(student.getInstituition()));
        }
        return dto;
    }

    public void updateEntity(StudentRequestDTO dto, Student student, Instituition instituition) {
        student.setLogin(dto.getLogin());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setCpf(dto.getCpf());
        student.setRg(dto.getRg());
        student.setAddress(dto.getAddress());
        student.setCourse(dto.getCourse());
        student.setInstituition(instituition);
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            student.setPassword(dto.getPassword());
        }
    }
}