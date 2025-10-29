package student.currency.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import student.currency.models.Advantage;
import student.currency.repositories.AdvantageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AdvantageService {

    @Autowired
    private AdvantageRepository advantageRepository;

    public List<Advantage> findAll() {
        return advantageRepository.findAll();
    }

    public Optional<Advantage> findById(Long id) {
        return advantageRepository.findById(id);
    }

    public Advantage save(Advantage advantage) {
        return advantageRepository.save(advantage);
    }

    public void delete(Long id) {
        advantageRepository.deleteById(id);
    }

    public Advantage update(Long id, Advantage advantage) {
        advantage.setId(id);
        return advantageRepository.save(advantage);
    }

    public List<Advantage> findByCompanyId(Long companyId) {
        return advantageRepository.findByCompanyId(companyId);
    }
}