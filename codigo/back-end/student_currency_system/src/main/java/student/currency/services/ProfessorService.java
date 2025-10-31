package student.currency.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import student.currency.dtos.professor.ProfessorRequestDTO;
import student.currency.dtos.professor.ProfessorResponseDTO;
import student.currency.exceptions.ResourceNotFoundException;
import student.currency.exceptions.ValidationException;
import student.currency.mappers.ProfessorMapper;
import student.currency.models.Instituition;
import student.currency.models.Professor;
import student.currency.repositories.InstituitionRepository;
import student.currency.repositories.ProfessorRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private InstituitionRepository instituitionRepository;

    @Autowired
    private ProfessorMapper professorMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Integer COINS_PER_SEMESTER = 1000;

    public List<ProfessorResponseDTO> findAll() {
        return professorRepository.findAll().stream()
                .map(professorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProfessorResponseDTO findById(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado com ID: " + id));
        return professorMapper.toResponseDTO(professor);
    }

    public ProfessorResponseDTO save(ProfessorRequestDTO dto) {
        if (professorRepository.existsByCpf(dto.getCpf())) {
            throw new ValidationException("CPF já cadastrado: " + dto.getCpf());
        }

        Instituition instituition = instituitionRepository.findById(dto.getInstituitionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Instituição não encontrada com ID: " + dto.getInstituitionId()));

        Professor professor = professorMapper.toEntity(dto, instituition);
        professor.setPassword(passwordEncoder.encode(dto.getPassword()));

        Professor savedProfessor = professorRepository.save(professor);
        return professorMapper.toResponseDTO(savedProfessor);
    }

    public void delete(Long id) {
        if (!professorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Professor não encontrado com ID: " + id);
        }
        professorRepository.deleteById(id);
    }

    public ProfessorResponseDTO update(Long id, ProfessorRequestDTO dto) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado com ID: " + id));

        Instituition instituition = instituitionRepository.findById(dto.getInstituitionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Instituição não encontrada com ID: " + dto.getInstituitionId()));

        professorMapper.updateEntity(dto, professor, instituition);
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            professor.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Professor updatedProfessor = professorRepository.save(professor);
        return professorMapper.toResponseDTO(updatedProfessor);
    }

    public Professor findByLogin(String login) {
        return professorRepository.findByLogin(login);
    }

    // Adiciona moedas semestrais para um professor específico
    @Transactional
    public ProfessorResponseDTO addSemesterCoins(Long professorId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado com ID: " + professorId));

        // adiciona 1.000 moedas ao saldo atual
        professor.setCoins(professor.getCoins() + COINS_PER_SEMESTER);
        professor.setLastCoinReload(LocalDateTime.now());

        Professor updatedProfessor = professorRepository.save(professor);
        return professorMapper.toResponseDTO(updatedProfessor);
    }

    // Adiciona moedas semestrais para todos professores
    @Transactional
    public void addSemesterCoinsToAll() {
        List<Professor> professors = professorRepository.findAll();

        for (Professor professor : professors) {
            professor.setCoins(professor.getCoins() + COINS_PER_SEMESTER);
            professor.setLastCoinReload(LocalDateTime.now());
            professorRepository.save(professor);
        }
    }

    // Adiciona moedas semestrais apenas para professores elegíveis (que não
    // receberam nos últimos 6 meses)
    @Transactional
    public int addSemesterCoinsToEligible() {
        List<Professor> professors = professorRepository.findAll();
        int count = 0;
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);

        for (Professor professor : professors) {
            if (professor.getLastCoinReload() == null ||
                    professor.getLastCoinReload().isBefore(sixMonthsAgo)) {

                professor.setCoins(professor.getCoins() + COINS_PER_SEMESTER);
                professor.setLastCoinReload(LocalDateTime.now());
                professorRepository.save(professor);
                count++;
            }
        }

        return count;
    }
}