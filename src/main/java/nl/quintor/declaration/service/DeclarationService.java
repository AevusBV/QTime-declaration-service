package nl.quintor.declaration.service;

import nl.quintor.declaration.model.Declaration;
import nl.quintor.declaration.repository.DeclarationRepository;
import org.dom4j.dtd.Decl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeclarationService {

    private DeclarationRepository declarationRepository;

    public DeclarationService(DeclarationRepository declarationRepository) {
        this.declarationRepository = declarationRepository;
    }

    public List<Declaration> findAllByEmployee(String employee) {
        return declarationRepository.findAllByEmployee(employee);
    }

    public List<Declaration> findAll() {
        return declarationRepository.findAll();
    }

    public Optional<Declaration> findById(long id) {
        return declarationRepository.findById(id);
    }

    public Declaration save(Declaration declaration) {
        return declarationRepository.save(declaration);
    }
}
