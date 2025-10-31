package student.currency.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import student.currency.dtos.student.StudentRequestDTO;
import student.currency.dtos.student.StudentResponseDTO;
import student.currency.exceptions.ResourceNotFoundException;
import student.currency.exceptions.ValidationException;
import student.currency.mappers.StudentMapper;
import student.currency.models.Instituition;
import student.currency.models.Student;
import student.currency.repositories.InstituitionRepository;
import student.currency.repositories.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstituitionRepository instituitionRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<StudentResponseDTO> findAll() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public StudentResponseDTO findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com ID: " + id));
        return studentMapper.toResponseDTO(student);
    }

    public StudentResponseDTO save(StudentRequestDTO dto) {
        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new ValidationException("Email já cadastrado: " + dto.getEmail());
        }
        if (studentRepository.existsByCpf(dto.getCpf())) {
            throw new ValidationException("CPF já cadastrado: " + dto.getCpf());
        }

        Instituition instituition = instituitionRepository.findById(dto.getInstituitionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Instituição não encontrada com ID: " + dto.getInstituitionId()));

        Student student = studentMapper.toEntity(dto, instituition);
        student.setPassword(passwordEncoder.encode(dto.getPassword()));
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toResponseDTO(savedStudent);
    }

    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aluno não encontrado com ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    public StudentResponseDTO update(Long id, StudentRequestDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com ID: " + id));

        Instituition instituition = instituitionRepository.findById(dto.getInstituitionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Instituição não encontrada com ID: " + dto.getInstituitionId()));

        studentMapper.updateEntity(dto, student, instituition);
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            student.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toResponseDTO(updatedStudent);
    }

    public Student findByLogin(String login) {
        return studentRepository.findByLogin(login).orElse(null);
    }
}