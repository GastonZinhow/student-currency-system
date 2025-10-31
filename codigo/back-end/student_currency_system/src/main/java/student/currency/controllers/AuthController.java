package student.currency.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import student.currency.dtos.authrequest.AuthRequestDTO;
import student.currency.dtos.authrequest.CompanyLoginResponseDTO;
import student.currency.dtos.authrequest.ProfessorLoginResponseDTO;
import student.currency.dtos.authrequest.StudentLoginResponseDTO;
import student.currency.exceptions.ResourceNotFoundException;
import student.currency.models.Company;
import student.currency.models.Professor;
import student.currency.models.Student;
import student.currency.services.CompanyService;
import student.currency.services.ProfessorService;
import student.currency.services.StudentService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private CompanyService companyService;

    @PostMapping("/login/student")
    public ResponseEntity<StudentLoginResponseDTO> loginStudent(@RequestBody AuthRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));

            if (!authentication.isAuthenticated()) {
                throw new BadCredentialsException("Credenciais inválidas");
            }

            Student student = studentService.findByLogin(request.getLogin());

            if (student == null) {
                throw new ResourceNotFoundException("Aluno não encontrado com login: " + request.getLogin());
            }

            StudentLoginResponseDTO response = new StudentLoginResponseDTO();
            response.setMessage("Login realizado com sucesso");
            response.setLogin(student.getLogin());
            response.setRole("STUDENT");
            response.setStudentId(student.getId());
            response.setName(student.getName());
            response.setEmail(student.getEmail());
            response.setCoins(student.getCoins());

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Login ou senha inválidos");
            throw new RuntimeException("Login ou senha inválidos");
        }
    }

    @PostMapping("/login/professor")
    public ResponseEntity<ProfessorLoginResponseDTO> loginProfessor(@RequestBody AuthRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));

            if (!authentication.isAuthenticated()) {
                throw new BadCredentialsException("Credenciais inválidas");
            }

            Professor professor = professorService.findByLogin(request.getLogin());

            if (professor == null) {
                throw new ResourceNotFoundException("Professor não encontrado com login: " + request.getLogin());
            }

            ProfessorLoginResponseDTO response = new ProfessorLoginResponseDTO();
            response.setMessage("Login realizado com sucesso");
            response.setLogin(professor.getLogin());
            response.setRole("PROFESSOR");
            response.setProfessorId(professor.getId());
            response.setName(professor.getName());
            response.setDepartment(professor.getDepartment());
            response.setCoins(professor.getCoins());

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Login ou senha inválidos");
        }
    }

    @PostMapping("/login/company")
    public ResponseEntity<CompanyLoginResponseDTO> loginCompany(@RequestBody AuthRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));

            if (!authentication.isAuthenticated()) {
                throw new BadCredentialsException("Credenciais inválidas");
            }

            Company company = companyService.findByLogin(request.getLogin());

            if (company == null) {
                throw new ResourceNotFoundException("Empresa não encontrada com login: " + request.getLogin());
            }

            CompanyLoginResponseDTO response = new CompanyLoginResponseDTO();
            response.setMessage("Login realizado com sucesso");
            response.setLogin(company.getLogin());
            response.setRole("COMPANY");
            response.setCompanyId(company.getId());
            response.setName(company.getName());
            response.setEmail(company.getEmail());

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Login ou senha inválidos");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginGeneric(@RequestBody AuthRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));

            if (!authentication.isAuthenticated()) {
                throw new BadCredentialsException("Credenciais inválidas");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login realizado com sucesso");
            response.put("login", request.getLogin());

            Student student = studentService.findByLogin(request.getLogin());
            if (student != null) {
                response.put("role", "STUDENT");
                response.put("userId", student.getId());
                response.put("studentId", student.getId());
                response.put("name", student.getName());
                response.put("coins", student.getCoins());
                return ResponseEntity.ok(response);
            }

            Professor professor = professorService.findByLogin(request.getLogin());
            if (professor != null) {
                response.put("role", "PROFESSOR");
                response.put("userId", professor.getId());
                response.put("professorId", professor.getId());
                response.put("name", professor.getName());
                response.put("coins", professor.getCoins());
                return ResponseEntity.ok(response);
            }

            Company company = companyService.findByLogin(request.getLogin());
            if (company != null) {
                response.put("role", "COMPANY");
                response.put("userId", company.getId());
                response.put("companyId", company.getId());
                response.put("name", company.getName());
                return ResponseEntity.ok(response);
            }

            throw new ResourceNotFoundException("Usuário não encontrado");

        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Login ou senha inválidos");
        }
    }
}